package io.github.hcisme.cowrite.service

import io.github.hcisme.cowrite.entity.pojo.Snapshot
import io.github.hcisme.cowrite.entity.query.SnapshotQuery
import io.github.hcisme.cowrite.entity.vo.PaginationResultVO

/**
 * 接口
 */
interface SnapshotService {
    /**
     * 根据条件查询列表
     */
    fun findListByParam(param: SnapshotQuery): List<Snapshot>

    /**
     * 根据条件查询数量
     */
    fun findCountByParam(param: SnapshotQuery): Int

    /**
     * 分页查询
     */
    fun findListByPage(param: SnapshotQuery): PaginationResultVO<Snapshot>

    /**
     * 新增
     */
    fun add(bean: Snapshot): Int

    /**
     * 新增 (或更新)
     */
    fun addOrUpdate(bean: Snapshot): Int

    /**
     * 批量新增
     */
    fun addBatch(list: List<Snapshot>): Int

    /**
     * 批量新增 (或更新)
     */
    fun addOrUpdateBatch(list: List<Snapshot>): Int

    /**
     * 多条件更新
     */
    fun updateByParam(bean: Snapshot, param: SnapshotQuery): Int

    /**
     * 多条件删除
     */
    fun deleteByParam(param: SnapshotQuery): Int

    /**
     * 根据Id查询对象
     */
    fun getSnapshotById(id: Long): Snapshot?

    /**
     * 根据Id修改
     */
    fun updateSnapshotById(bean: Snapshot, id: Long): Int

    /**
     * 根据Id删除
     */
    fun deleteSnapshotById(id: Long): Int

    fun saveSnapshot(docId: String, content: String, binaryState: ByteArray, creatorId: String)
}
