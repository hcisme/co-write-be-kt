package io.github.hcisme.cowrite.service.impl

import io.github.hcisme.cowrite.entity.Constants
import io.github.hcisme.cowrite.entity.enums.DeleteStatusEnum
import io.github.hcisme.cowrite.entity.enums.DocRoleEnum
import io.github.hcisme.cowrite.entity.enums.ResponseCodeEnum
import io.github.hcisme.cowrite.entity.pojo.Collaborator
import io.github.hcisme.cowrite.entity.pojo.Document
import io.github.hcisme.cowrite.entity.pojo.User
import io.github.hcisme.cowrite.entity.query.CollaboratorQuery
import io.github.hcisme.cowrite.entity.query.DocumentQuery
import io.github.hcisme.cowrite.entity.query.UserQuery
import io.github.hcisme.cowrite.entity.vo.DocInCollaboratorVO
import io.github.hcisme.cowrite.exception.BusinessException
import io.github.hcisme.cowrite.mappers.CollaboratorMapper
import io.github.hcisme.cowrite.mappers.DocumentMapper
import io.github.hcisme.cowrite.mappers.UserMapper
import io.github.hcisme.cowrite.service.CollaboratorService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForEntity
import java.time.LocalDateTime

/**
 * 接口实现
 */
@Service("collaboratorService")
class CollaboratorServiceImpl(
    private val documentMapper: DocumentMapper<Document, DocumentQuery>,
    private val userMapper: UserMapper<User, UserQuery>,
    private val collaboratorMapper: CollaboratorMapper<Collaborator, CollaboratorQuery>,
    private val docMapper: DocumentMapper<Document, DocumentQuery>,
    @Value($$"${node.server.url}")
    private val nodeServerUrl: String
) : CollaboratorService {
    private val log = LoggerFactory.getLogger(CollaboratorServiceImpl::class.java)

    override fun addCollaborator(operatorId: String, docId: String, collaboratorId: String, role: Int) {
        val doc = checkDocExistWithLock(docId = docId)
        // 不能邀请自己，也不能邀请所有者
        if (doc.ownerId == collaboratorId || operatorId == collaboratorId) {
            throw BusinessException("操作非法：该用户已经是文档所有者")
        }

        // 只有所有者能邀请人
        if (doc.ownerId != operatorId) {
            throw BusinessException("只有文档所有者可以邀请协作者")
        }

        // 校验被邀请人是否存在
        userMapper.selectById(collaboratorId) ?: throw BusinessException("邀请的用户不存在")

        // 校验是否已经是协作者（防止唯一索引报错）
        val existing = collaboratorMapper.selectByDocIdAndUserId(docId = docId, userId = collaboratorId)
        if (existing != null) {
            throw BusinessException("该用户已经是协作者了")
        }

        // 角色合法性强制校验（防止传 0）
        if (role != DocRoleEnum.EDITOR.role && role != DocRoleEnum.VIEWER.role) {
            throw BusinessException("非法的协作角色")
        }

        val newCollaborator = Collaborator().apply {
            this.docId = docId
            this.userId = collaboratorId
            this.role = role
            this.createdTime = LocalDateTime.now()
        }
        collaboratorMapper.insert(newCollaborator)
    }

    override fun checkPermission(docId: String, userId: String): Collaborator {
        checkDocExistWithLock(docId = docId)

        return collaboratorMapper.selectByDocIdAndUserId(docId = docId, userId = userId)
            ?: throw BusinessException(ResponseCodeEnum.CODE_500)
    }

    override fun getCollaboratorsByDocId(docId: String, order: String): List<DocInCollaboratorVO> {
        checkDocExistWithLock(docId = docId)

        val list = collaboratorMapper.selectCollaboratorsByDocId(
            CollaboratorQuery(docId = docId).apply { this.orderBy = order }
        )
        return list
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun updateRole(
        operatorId: String,
        docId: String,
        collaboratorId: String,
        role: Int
    ) {
        val doc = checkDocExistWithLock(docId = docId)
        // 只有文档当前拥有者有权管理成员
        if (doc.ownerId != operatorId) {
            throw BusinessException("只有文档拥有者可以修改权限")
        }

        // 角色合法性检查
        val targetRole = DocRoleEnum.getByRole(role) ?: throw BusinessException("无效的角色参数")

        val isTargetOwner = collaboratorId == doc.ownerId
        if (targetRole == DocRoleEnum.OWNER) {
            if (isTargetOwner) return
            // 权限转让

            // 新主升级
            val updateRows = collaboratorMapper.updateByDocIdAndUserId(
                Collaborator(role = DocRoleEnum.OWNER.role),
                docId = docId,
                userId = collaboratorId
            )
            if (updateRows == 0) {
                throw BusinessException("该用户不在协作列表中")
            }

            // 旧主降级
            collaboratorMapper.updateByDocIdAndUserId(
                Collaborator(role = DocRoleEnum.EDITOR.role),
                docId = docId,
                userId = doc.ownerId!!
            )

            // 更新文档主表：修改 owner_id
            documentMapper.updateById(
                Document(ownerId = collaboratorId, updatedTime = LocalDateTime.now()),
                id = docId
            )
        } else {
            // 普通权限变更
            if (isTargetOwner) {
                throw BusinessException("不能修改自己的权限")
            }

            val updateRows = collaboratorMapper.updateByDocIdAndUserId(
                Collaborator(role = role),
                docId = docId,
                userId = collaboratorId
            )
            if (updateRows == 0) {
                throw BusinessException("该用户不在协作列表中")
            }
        }
        notifyNodeSyncPermission(docId, collaboratorId, role, "UPDATE")
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun deleteMember(operatorId: String, docId: String, collaboratorId: String) {
        val doc = checkDocExistWithLock(docId = docId)

        val isOwner = doc.ownerId == operatorId
        val isSelfLeaving = operatorId == collaboratorId

        // 如果不是拥有者在踢人，也不是成员在自退
        if (!isOwner && !isSelfLeaving) {
            throw BusinessException("非法操作")
        }

        // 拥有者不能离开或被移除
        if (doc.ownerId == collaboratorId) {
            throw BusinessException("拥有者不能退出或被移除")
        }

        val rows = collaboratorMapper.deleteByDocIdAndUserId(
            docId = docId,
            userId = collaboratorId
        )

        if (rows == 0) {
            throw BusinessException("该用户不在协作列表中")
        }
        notifyNodeSyncPermission(docId, collaboratorId, null, "DELETE")
    }

    /**
     * 通过文档id检查文档是否存在
     */
    private fun checkDocExistWithLock(docId: String): Document {
        val document = docMapper.selectById(docId)
        if (document == null || document.deleted == DeleteStatusEnum.DELETED.status) {
            throw BusinessException("文档不存在")
        }
        return document
    }

    private fun notifyNodeSyncPermission(docId: String, userId: String, role: Int?, action: String) {
        val client = RestTemplate()
        val headers = HttpHeaders()
        headers.set(Constants.INTERNAL_SECRET_KEY, Constants.INTERNAL_SECRET)
        headers.contentType = MediaType.APPLICATION_JSON

        val body = mapOf(
            "docId" to docId,
            "userId" to userId,
            "role" to role,
            "action" to action
        )

        val request = HttpEntity(body, headers)
        try {
            client.postForEntity<String>("$nodeServerUrl/internal/sync-permission", request)
        } catch (e: Exception) {
            log.error("通知 Node.js 同步权限失败: ${e.message}")
        }
    }
}
