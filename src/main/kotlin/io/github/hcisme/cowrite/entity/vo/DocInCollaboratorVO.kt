package io.github.hcisme.cowrite.entity.vo

import java.io.Serializable

data class DocInCollaboratorVO(
    var userId: String? = null,
    var username: String? = null,
    var email: String? = null,
    var role: Int? = null
) : Serializable
