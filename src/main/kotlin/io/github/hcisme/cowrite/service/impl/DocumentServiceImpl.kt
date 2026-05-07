package io.github.hcisme.cowrite.service.impl

import io.github.hcisme.cowrite.entity.enums.DeleteStatusEnum
import io.github.hcisme.cowrite.entity.enums.DocRoleEnum
import io.github.hcisme.cowrite.entity.enums.PageSizeEnum
import io.github.hcisme.cowrite.entity.pojo.Collaborator
import io.github.hcisme.cowrite.entity.pojo.Document
import io.github.hcisme.cowrite.entity.query.CollaboratorQuery
import io.github.hcisme.cowrite.entity.query.DocumentQuery
import io.github.hcisme.cowrite.entity.query.SimplePage
import io.github.hcisme.cowrite.entity.vo.PaginationResultVO
import io.github.hcisme.cowrite.exception.BusinessException
import io.github.hcisme.cowrite.mappers.CollaboratorMapper
import io.github.hcisme.cowrite.mappers.DocumentMapper
import io.github.hcisme.cowrite.service.DocumentService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

/**
 * 接口实现
 */
@Service("documentService")
class DocumentServiceImpl(
    private val documentMapper: DocumentMapper<Document, DocumentQuery>,
    private val collaboratorMapper: CollaboratorMapper<Collaborator, CollaboratorQuery>
) : DocumentService {

    /**
     * 根据条件查询列表
     */
    override fun findListByParam(param: DocumentQuery): List<Document> {
        return documentMapper.selectList(param)
    }

    /**
     * 根据条件查询数量
     */
    override fun findCountByParam(param: DocumentQuery): Int {
        return documentMapper.selectCount(param)
    }

    /**
     * 分页查询
     */
    override fun findListByPage(param: DocumentQuery): PaginationResultVO<Document> {
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
    override fun add(bean: Document): Int {
        return documentMapper.insert(bean)
    }

    /**
     * 新增 (或更新)
     */
    override fun addOrUpdate(bean: Document): Int {
        return documentMapper.insertOrUpdate(bean)
    }

    /**
     * 批量新增
     */
    override fun addBatch(list: List<Document>): Int {
        return documentMapper.insertBatch(list)
    }

    /**
     * 批量新增 (或更新)
     */
    override fun addOrUpdateBatch(list: List<Document>): Int {
        return documentMapper.insertOrUpdateBatch(list)
    }

    /**
     * 多条件更新
     */
    override fun updateByParam(bean: Document, param: DocumentQuery): Int {
        return documentMapper.updateByParam(bean, param)
    }

    /**
     * 多条件删除
     */
    override fun deleteByParam(param: DocumentQuery): Int {
        return documentMapper.deleteByParam(param)
    }

    /**
     * 根据Id查询对象
     */
    override fun getDocumentById(id: String): Document? {
        return documentMapper.selectById(id)
    }

    /**
     * 根据Id修改
     */
    override fun updateDocumentById(bean: Document, id: String): Int {
        return documentMapper.updateById(bean, id)
    }

    /**
     * 根据Id删除
     */
    override fun deleteDocumentById(id: String): Int {
        return documentMapper.deleteById(id)
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun create(title: String, ownerId: String) {
        val docId = UUID.randomUUID().toString()
        val date = LocalDateTime.now()

        val doc = Document().apply {
            this.id = docId
            this.title = title
            this.ownerId = ownerId
            this.deleted = DeleteStatusEnum.NOT_DELETED.status
            this.createdTime = date
            this.updatedTime = date
        }
        documentMapper.insert(doc)

        val collaborator = Collaborator().apply {
            this.docId = docId
            this.userId = ownerId
            this.role = DocRoleEnum.OWNER.role
            this.createdTime = date
        }
        collaboratorMapper.insert(collaborator)
    }

    override fun getListByUserId(userId: String): List<Collaborator> {
        return collaboratorMapper.selectListByUserId(
            CollaboratorQuery(userId = userId)
        )
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun deleteDocByDocId(docId: String, userId: String) {
        val dbDoc = documentMapper.selectById(docId) ?: throw BusinessException("文档不存在")
        if (dbDoc.ownerId != userId) {
            throw BusinessException("只有文档所有者可以删除该文档")
        }

        documentMapper.updateByParam(
            Document(deleted = DeleteStatusEnum.DELETED.status),
            DocumentQuery(id = docId, ownerId = userId)
        )

        collaboratorMapper.deleteByParam(CollaboratorQuery(docId = docId))
    }
}
