package io.github.hcisme.cowrite.entity.pojo

import java.io.Serializable
import java.time.LocalDateTime

data class User(

    /**
     * 用户id
     */
    var id: String? = null,

    /**
     * 用户名
     */
    var username: String? = null,

    /**
     * 邮箱
     */
    var email: String? = null,

    /**
     * 密码
     */
    var password: String? = null,

    /**
     * 创建时间
     */
    var createdTime: LocalDateTime? = null,

    /**
     * 更新时间
     */
    var updatedTime: LocalDateTime? = null
) : Serializable
