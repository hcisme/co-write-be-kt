package io.github.hcisme.cowrite.service.impl

import io.github.hcisme.cowrite.entity.enums.DocRoleEnum
import io.github.hcisme.cowrite.entity.enums.PageSizeEnum
import io.github.hcisme.cowrite.entity.enums.ResponseCodeEnum
import io.github.hcisme.cowrite.entity.pojo.Collaborator
import io.github.hcisme.cowrite.entity.pojo.Document
import io.github.hcisme.cowrite.entity.pojo.User
import io.github.hcisme.cowrite.entity.query.CollaboratorQuery
import io.github.hcisme.cowrite.entity.query.DocumentQuery
import io.github.hcisme.cowrite.entity.query.SimplePage
import io.github.hcisme.cowrite.entity.query.UserQuery
import io.github.hcisme.cowrite.entity.vo.PaginationResultVO
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

    /**
     * 根据条件查询列表
     */
    override fun findListByParam(param: CollaboratorQuery): List<Collaborator> {
        return collaboratorMapper.selectList(param)
    }

    /**
     * 根据条件查询数量
     */
    override fun findCountByParam(param: CollaboratorQuery): Int {
        return collaboratorMapper.selectCount(param)
    }

    /**
     * 分页查询
     */
    override fun findListByPage(param: CollaboratorQuery): PaginationResultVO<Collaborator> {
        val count = findCountByParam(param)
        val pageSizeEnum = if (param.pageSize == null) PageSizeEnum.SIZE15.size else param.pageSize!!
        val page = SimplePage(param.page, count, pageSizeEnum)
        param.simplePage = page
        val list = findListByParam(param)
        val result = PaginationResultVO(count, page.pageSize, page.page, page.pageTotal, list)
        return result
    }

    /**
     * 新增
     */
    override fun add(bean: Collaborator): Int {
        return collaboratorMapper.insert(bean)
    }

    /**
     * 新增 (或更新)
     */
    override fun addOrUpdate(bean: Collaborator): Int {
        return collaboratorMapper.insertOrUpdate(bean)
    }

    /**
     * 批量新增
     */
    override fun addBatch(list: List<Collaborator>): Int {
        return collaboratorMapper.insertBatch(list)
    }

    /**
     * 批量新增 (或更新)
     */
    override fun addOrUpdateBatch(list: List<Collaborator>): Int {
        return collaboratorMapper.insertOrUpdateBatch(list)
    }

    /**
     * 多条件更新
     */
    override fun updateByParam(bean: Collaborator, param: CollaboratorQuery): Int {
        return collaboratorMapper.updateByParam(bean, param)
    }

    /**
     * 多条件删除
     */
    override fun deleteByParam(param: CollaboratorQuery): Int {
        return collaboratorMapper.deleteByParam(param)
    }

    /**
     * 根据Id查询对象
     */
    override fun getCollaboratorById(id: Long): Collaborator? {
        return collaboratorMapper.selectById(id)
    }

    /**
     * 根据Id修改
     */
    override fun updateCollaboratorById(bean: Collaborator, id: Long): Int {
        return collaboratorMapper.updateById(bean, id)
    }

    /**
     * 根据Id删除
     */
    override fun deleteCollaboratorById(id: Long): Int {
        return collaboratorMapper.deleteById(id)
    }

    /**
     * 根据DocId 和 UserId查询对象
     */
    override fun getCollaboratorByDocIdAndUserId(docId: String, userId: String): Collaborator? {
        return collaboratorMapper.selectByDocIdAndUserId(docId, userId)
    }

    /**
     * 根据DocId 和 UserId修改
     */
    override fun updateCollaboratorByDocIdAndUserId(bean: Collaborator, docId: String, userId: String): Int {
        return collaboratorMapper.updateByDocIdAndUserId(bean, docId, userId)
    }

    /**
     * 根据DocId 和 UserId删除
     */
    override fun deleteCollaboratorByDocIdAndUserId(docId: String, userId: String): Int {
        return collaboratorMapper.deleteByDocIdAndUserId(docId, userId)
    }

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
