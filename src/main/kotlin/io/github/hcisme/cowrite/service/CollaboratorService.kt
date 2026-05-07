package io.github.hcisme.cowrite.service

import io.github.hcisme.cowrite.entity.pojo.Collaborator
import io.github.hcisme.cowrite.entity.query.CollaboratorQuery
import io.github.hcisme.cowrite.entity.vo.PaginationResultVO

/**
 * 接口
 */
interface CollaboratorService {
    /**
     * 根据条件查询列表
     */
    fun findListByParam(param: CollaboratorQuery): List<Collaborator>

    /**
     * 根据条件查询数量
     */
    fun findCountByParam(param: CollaboratorQuery): Int

    /**
     * 分页查询
     */
    fun findListByPage(param: CollaboratorQuery): PaginationResultVO<Collaborator>

    /**
     * 新增
     */
    fun add(bean: Collaborator): Int

    /**
     * 新增 (或更新)
     */
    fun addOrUpdate(bean: Collaborator): Int

    /**
     * 批量新增
     */
    fun addBatch(list: List<Collaborator>): Int

    /**
     * 批量新增 (或更新)
     */
    fun addOrUpdateBatch(list: List<Collaborator>): Int

    /**
     * 多条件更新
     */
    fun updateByParam(bean: Collaborator, param: CollaboratorQuery): Int

    /**
     * 多条件删除
     */
    fun deleteByParam(param: CollaboratorQuery): Int

    /**
     * 根据Id查询对象
     */
    fun getCollaboratorById(id: Long): Collaborator?

    /**
     * 根据Id修改
     */
    fun updateCollaboratorById(bean: Collaborator, id: Long): Int

    /**
     * 根据Id删除
     */
    fun deleteCollaboratorById(id: Long): Int

    /**
     * 根据DocId 和 UserId查询对象
     */
    fun getCollaboratorByDocIdAndUserId(docId: String, userId: String): Collaborator?

    /**
     * 根据DocId 和 UserId修改
     */
    fun updateCollaboratorByDocIdAndUserId(bean: Collaborator, docId: String, userId: String): Int

    /**
     * 根据DocId 和 UserId删除
     */
    fun deleteCollaboratorByDocIdAndUserId(docId: String, userId: String): Int

    fun addCollaborator(operatorId: String, docId: String, collaboratorId: String, role: Int)

    fun checkPermission(docId: String, userId: String)
}
