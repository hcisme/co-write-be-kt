package io.github.hcisme.cowrite.service.impl

import io.github.hcisme.cowrite.entity.enums.DeleteStatusEnum
import io.github.hcisme.cowrite.entity.pojo.Collaborator
import io.github.hcisme.cowrite.entity.pojo.Document
import io.github.hcisme.cowrite.entity.pojo.Snapshot
import io.github.hcisme.cowrite.entity.query.CollaboratorQuery
import io.github.hcisme.cowrite.entity.query.DocumentQuery
import io.github.hcisme.cowrite.entity.query.SnapshotQuery
import io.github.hcisme.cowrite.exception.BusinessException
import io.github.hcisme.cowrite.mappers.CollaboratorMapper
import io.github.hcisme.cowrite.mappers.DocumentMapper
import io.github.hcisme.cowrite.mappers.SnapshotMapper
import io.github.hcisme.cowrite.service.SnapshotService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * 接口实现
 */
@Service("snapshotService")
class SnapshotServiceImpl(
    private val snapshotMapper: SnapshotMapper<Snapshot, SnapshotQuery>,
    private val collaboratorMapper: CollaboratorMapper<Collaborator, CollaboratorQuery>,
    private val docMapper: DocumentMapper<Document, DocumentQuery>
) : SnapshotService {

    @Transactional(rollbackFor = [Exception::class])
    override fun saveSnapshot(
        docId: String,
        content: String,
        binaryState: ByteArray,
        creatorId: String
    ) {
        // 校验文档是否存在及状态
        val doc = docMapper.selectById(docId) ?: throw BusinessException("文档不存在，无法保存快照")
        // 已删除的文档不处理
        if (doc.deleted == DeleteStatusEnum.DELETED.status) {
            return
        }

        if (binaryState.isEmpty()) {
            throw BusinessException("快照内容不能为空")
        }

        val lastSnapshot = snapshotMapper.selectLastByDocId(docId)
        if (lastSnapshot != null && lastSnapshot.binaryState.contentEquals(binaryState)) {
            return
        }

        val snapshot = Snapshot(
            docId = docId,
            content = content,
            binaryState = binaryState,
            versionName = "自动保存_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm")),
            creatorId = creatorId,
            createdTime = LocalDateTime.now()
        )
        snapshotMapper.insert(snapshot)

        // 更新主表的 updated_time，方便列表排序
        val updateDoc = Document().apply {
            id = docId
            updatedTime = LocalDateTime.now()
        }
        docMapper.updateById(updateDoc, id = docId)
    }

    override fun getLastSnapshot(userId: String, docId: String): Snapshot? {
        val doc = docMapper.selectById(docId)
        if (doc == null || doc.deleted == DeleteStatusEnum.DELETED.status) {
            throw BusinessException("资源不存在")
        }

        collaboratorMapper.selectByDocIdAndUserId(docId = docId, userId = userId)
            ?: throw BusinessException("非法操作")

        return snapshotMapper.selectLastByDocId(docId)
    }
}
