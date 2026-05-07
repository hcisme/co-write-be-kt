package io.github.hcisme.cowrite.service.impl

import io.github.hcisme.cowrite.entity.enums.DeleteStatusEnum
import io.github.hcisme.cowrite.entity.pojo.Document
import io.github.hcisme.cowrite.entity.pojo.Snapshot
import io.github.hcisme.cowrite.entity.pojo.User
import io.github.hcisme.cowrite.entity.query.DocumentQuery
import io.github.hcisme.cowrite.entity.query.SnapshotQuery
import io.github.hcisme.cowrite.entity.query.UserQuery
import io.github.hcisme.cowrite.exception.BusinessException
import io.github.hcisme.cowrite.mappers.SnapshotMapper
import io.github.hcisme.cowrite.mappers.UserMapper
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
    private val userMapper: UserMapper<User, UserQuery>,
    private val docMapper: UserMapper<Document, DocumentQuery>
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
        if (doc.deleted == DeleteStatusEnum.DELETED.status) {
            return // 已删除的文档不处理
        }

        if (binaryState.isEmpty()) {
            throw BusinessException("快照内容不能为空")
        }

        val lastSnapshot = snapshotMapper.selectLastByDocId(docId)
        if (lastSnapshot != null && lastSnapshot.binaryState.contentEquals(binaryState)) {
            // 内容完全一致，更新一下最后更新时间
            lastSnapshot.createdTime = LocalDateTime.now()
            snapshotMapper.updateById(lastSnapshot, id = lastSnapshot.id!!)
            return
        }

        val snapshot = Snapshot().apply {
            this.docId = docId
            this.content = content
            this.binaryState = binaryState
            this.versionName = "自动保存_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"))
            this.creatorId = creatorId
            this.createdTime = LocalDateTime.now()
        }
        snapshotMapper.insert(snapshot)

        // 更新主表的 updated_time，方便列表排序
        val updateDoc = Document().apply {
            id = docId
            updatedTime = LocalDateTime.now()
        }
        docMapper.updateById(updateDoc, id = docId)
    }
}
