package io.github.hcisme.cowrite.entity.pojo

import java.io.Serializable
import java.time.LocalDateTime

data class Document(

    /**
     * 文档id
     */
    var id: String? = null,

    /**
     * 文档标题
     */
    var title: String? = null,

    /**
     * 文档的创建者
     */
    var ownerId: String? = null,

    /**
     * 软删除标识 0:未删除 1:已删除
     */
    var deleted: Int? = null,

    /**
     * 创建时间
     */
    var createdTime: LocalDateTime? = null,

    /**
     * 更新时间
     */
    var updatedTime: LocalDateTime? = null
) : Serializable
