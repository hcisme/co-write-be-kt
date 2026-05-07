package io.github.hcisme.cowrite.entity.query

import java.time.LocalDateTime

/**
 *  查询对象
 */
data class UserQuery(

    /**
     * 用户id
     */
    var id: String? = null,

    var idFuzzy: String? = null,

    /**
     * 用户名
     */
    var username: String? = null,

    var usernameFuzzy: String? = null,

    /**
     * 邮箱
     */
    var email: String? = null,

    var emailFuzzy: String? = null,

    /**
     * 密码
     */
    var password: String? = null,

    var passwordFuzzy: String? = null,

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
