package io.github.hcisme.cowrite.service

import io.github.hcisme.cowrite.entity.pojo.Collaborator
import io.github.hcisme.cowrite.entity.pojo.Document
import io.github.hcisme.cowrite.entity.query.DocumentQuery
import io.github.hcisme.cowrite.entity.vo.PaginationResultVO

/**
 * 接口
 */
interface DocumentService {
    /**
     * 根据条件查询列表
     */
    fun findListByParam(param: DocumentQuery): List<Document>

    /**
     * 根据条件查询数量
     */
    fun findCountByParam(param: DocumentQuery): Int

    /**
     * 分页查询
     */
    fun findListByPage(param: DocumentQuery): PaginationResultVO<Document>

    /**
     * 新增
     */
    fun add(bean: Document): Int

    /**
     * 新增 (或更新)
     */
    fun addOrUpdate(bean: Document): Int

    /**
     * 批量新增
     */
    fun addBatch(list: List<Document>): Int

    /**
     * 批量新增 (或更新)
     */
    fun addOrUpdateBatch(list: List<Document>): Int

    /**
     * 多条件更新
     */
    fun updateByParam(bean: Document, param: DocumentQuery): Int

    /**
     * 多条件删除
     */
    fun deleteByParam(param: DocumentQuery): Int

    /**
     * 根据Id查询对象
     */
    fun getDocumentById(id: String): Document?

    /**
     * 根据Id修改
     */
    fun updateDocumentById(bean: Document, id: String): Int

    /**
     * 根据Id删除
     */
    fun deleteDocumentById(id: String): Int


    fun create(title: String, ownerId: String)

    fun getListByUserId(userId: String): List<Collaborator>

    fun deleteDocByDocId(docId: String, userId: String)
}
