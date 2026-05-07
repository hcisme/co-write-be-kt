package io.github.hcisme.cowrite.entity.pojo

import java.io.Serializable
import java.time.LocalDateTime

data class Collaborator(

    /**
     * 文档关系表id
     */
    var id: Long? = null,

    /**
     * 关联文档表id
     */
    var docId: String? = null,

    /**
     * 关联用户表id
     */
    var userId: String? = null,

    /**
     * 0:持有者 1:可编辑 2: 只读
     */
    var role: Int? = null,

    /**
     * 创建时间
     */
    var createdTime: LocalDateTime? = null,

    // 关联字段
    var docTitle: String? = null,
    var username: String? = null,
    var email: String? = null
) : Serializable
