package io.github.hcisme.cowrite.entity.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Range

class AddOrUpdateMemberDTO {
    @NotBlank(message = "文档ID不能为空")
    var docId: String? = null

    @NotBlank(message = "用户的ID不能为空")
    var userId: String? = null

    @NotNull(message = "角色不能为空")
    @Range(min = 0, max = 2)
    var role: Int? = null
}
