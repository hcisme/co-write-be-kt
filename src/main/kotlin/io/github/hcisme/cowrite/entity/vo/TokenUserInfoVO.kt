package io.github.hcisme.cowrite.entity.vo

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable
import java.time.LocalDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
class TokenUserInfoVO : Serializable {
    var id: String? = null
    var username: String? = null
    var email: String? = null
    var createdTime: LocalDateTime? = null
    var updatedTime: LocalDateTime? = null
    var token: String? = null
    var expireAt: Long? = null
}