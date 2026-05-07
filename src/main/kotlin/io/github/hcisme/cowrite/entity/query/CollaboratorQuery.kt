package io.github.hcisme.cowrite.entity.query

import java.time.LocalDateTime

/**
 *  查询对象
 */
data class CollaboratorQuery(

    /**
     * 文档关系表id
     */
    var id: Long? = null,

    /**
     * 关联文档表id
     */
    var docId: String? = null,

    var docIdFuzzy: String? = null,

    /**
     * 关联用户表id
     */
    var userId: String? = null,

    var userIdFuzzy: String? = null,

    /**
     * 0:持有者 1:可编辑 2: 只读
     */
    var role: Int? = null,

    /**
     * 创建时间
     */
    var createdTime: LocalDateTime? = null,

    var createdTimeStart: String? = null,

    var createdTimeEnd: String? = null
) : BaseQuery()
