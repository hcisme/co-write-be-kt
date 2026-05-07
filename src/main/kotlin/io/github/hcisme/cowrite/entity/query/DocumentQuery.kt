package io.github.hcisme.cowrite.entity.query

import java.time.LocalDateTime

/**
 *  查询对象
 */
data class DocumentQuery(

    /**
     * 文档id
     */
    var id: String? = null,

    var idFuzzy: String? = null,

    /**
     * 文档标题
     */
    var title: String? = null,

    var titleFuzzy: String? = null,

    /**
     * 文档的创建者
     */
    var ownerId: String? = null,

    var ownerIdFuzzy: String? = null,

    /**
     * 软删除标识 0:未删除 1:已删除
     */
    var deleted: Int? = null,

    /**
     * 创建时间
     */
    var createdTime: LocalDateTime? = null,

    var createdTimeStart: String? = null,

    var createdTimeEnd: String? = null,

    /**
     * 更新时间
     */
    var updatedTime: LocalDateTime? = null,

    var updatedTimeStart: String? = null,

    var updatedTimeEnd: String? = null
) : BaseQuery()
