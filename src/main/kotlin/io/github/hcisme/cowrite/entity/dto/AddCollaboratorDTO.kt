package io.github.hcisme.cowrite.entity.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Range

class AddCollaboratorDTO {
    @NotBlank(message = "文档ID不能为空")
    var docId: String? = null

    @NotBlank(message = "添加用户的ID不能为空")
    var userId: String? = null

    @NotNull(message = "角色不能为空")
    @Range(min = 1, max = 2, message = "角色不合法，只能是1(编辑)或2(只读)")
    var role: Int? = null
}
