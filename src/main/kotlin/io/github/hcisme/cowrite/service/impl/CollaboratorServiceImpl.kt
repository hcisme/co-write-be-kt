package io.github.hcisme.cowrite.service.impl

import io.github.hcisme.cowrite.entity.enums.DocRoleEnum
import io.github.hcisme.cowrite.entity.enums.ResponseCodeEnum
import io.github.hcisme.cowrite.entity.pojo.Collaborator
import io.github.hcisme.cowrite.entity.pojo.Document
import io.github.hcisme.cowrite.entity.pojo.User
import io.github.hcisme.cowrite.entity.query.CollaboratorQuery
import io.github.hcisme.cowrite.entity.query.DocumentQuery
import io.github.hcisme.cowrite.entity.query.UserQuery
import io.github.hcisme.cowrite.exception.BusinessException
import io.github.hcisme.cowrite.mappers.CollaboratorMapper
import io.github.hcisme.cowrite.mappers.DocumentMapper
import io.github.hcisme.cowrite.mappers.UserMapper
import io.github.hcisme.cowrite.service.CollaboratorService
import org.springframework.stereotype.Service
import java.time.LocalDateTime

/**
 * 接口实现
 */
@Service("collaboratorService")
class CollaboratorServiceImpl(
    private val documentMapper: DocumentMapper<Document, DocumentQuery>,
    private val userMapper: UserMapper<User, UserQuery>,
    private val collaboratorMapper: CollaboratorMapper<Collaborator, CollaboratorQuery>
) : CollaboratorService {

    override fun addCollaborator(operatorId: String, docId: String, collaboratorId: String, role: Int) {
        // 不能邀请自己，也不能邀请所有者
        val doc = documentMapper.selectById(docId) ?: throw BusinessException("文档不存在")
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

    override fun checkPermission(docId: String, userId: String) {
        collaboratorMapper.selectByDocIdAndUserId(docId = docId, userId = userId)
            ?: throw BusinessException(ResponseCodeEnum.CODE_500)
    }
}
