package io.github.hcisme.cowrite.entity.dto

import jakarta.validation.constraints.NotBlank

class CheckPermissionDTO {
    @NotBlank(message = "文档ID不能为空")
    var docId: String? = null
}