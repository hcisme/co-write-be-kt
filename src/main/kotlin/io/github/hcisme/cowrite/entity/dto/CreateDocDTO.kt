package io.github.hcisme.cowrite.entity.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

class CreateDocDTO {
    @NotBlank(message = "标题不能为空")
    @Size(max = 50, message = "标题长度不能超过50个字符")
    var title: String? = null
}