package io.github.hcisme.cowrite.service.impl

import io.github.hcisme.cowrite.entity.enums.DeleteStatusEnum
import io.github.hcisme.cowrite.entity.enums.PageSizeEnum
import io.github.hcisme.cowrite.entity.pojo.Document
import io.github.hcisme.cowrite.entity.pojo.Snapshot
import io.github.hcisme.cowrite.entity.pojo.User
import io.github.hcisme.cowrite.entity.query.DocumentQuery
import io.github.hcisme.cowrite.entity.query.SimplePage
import io.github.hcisme.cowrite.entity.query.SnapshotQuery
import io.github.hcisme.cowrite.entity.query.UserQuery
import io.github.hcisme.cowrite.entity.vo.PaginationResultVO
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

    /**
     * 根据条件查询列表
     */
    override fun findListByParam(param: SnapshotQuery): List<Snapshot> {
        return snapshotMapper.selectList(param)
    }

    /**
     * 根据条件查询数量
     */
    override fun findCountByParam(param: SnapshotQuery): Int {
        return snapshotMapper.selectCount(param)
    }

    /**
     * 分页查询
     */
    override fun findListByPage(param: SnapshotQuery): PaginationResultVO<Snapshot> {
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
    override fun add(bean: Snapshot): Int {
        return snapshotMapper.insert(bean)
    }

    /**
     * 新增 (或更新)
     */
    override fun addOrUpdate(bean: Snapshot): Int {
        return snapshotMapper.insertOrUpdate(bean)
    }

    /**
     * 批量新增
     */
    override fun addBatch(list: List<Snapshot>): Int {
        return snapshotMapper.insertBatch(list)
    }

    /**
     * 批量新增 (或更新)
     */
    override fun addOrUpdateBatch(list: List<Snapshot>): Int {
        return snapshotMapper.insertOrUpdateBatch(list)
    }

    /**
     * 多条件更新
     */
    override fun updateByParam(bean: Snapshot, param: SnapshotQuery): Int {
        return snapshotMapper.updateByParam(bean, param)
    }

    /**
     * 多条件删除
     */
    override fun deleteByParam(param: SnapshotQuery): Int {
        return snapshotMapper.deleteByParam(param)
    }

    /**
     * 根据Id查询对象
     */
    override fun getSnapshotById(id: Long): Snapshot? {
        return snapshotMapper.selectById(id)
    }

    /**
     * 根据Id修改
     */
    override fun updateSnapshotById(bean: Snapshot, id: Long): Int {
        return snapshotMapper.updateById(bean, id)
    }

    /**
     * 根据Id删除
     */
    override fun deleteSnapshotById(id: Long): Int {
        return snapshotMapper.deleteById(id)
    }

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
