package io.github.hcisme.cowrite.service.impl

import io.github.hcisme.cowrite.entity.enums.DeleteStatusEnum
import io.github.hcisme.cowrite.entity.enums.DocRoleEnum
import io.github.hcisme.cowrite.entity.pojo.Collaborator
import io.github.hcisme.cowrite.entity.pojo.Document
import io.github.hcisme.cowrite.entity.query.CollaboratorQuery
import io.github.hcisme.cowrite.entity.query.DocumentQuery
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
