package io.github.hcisme.cowrite.entity.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Range

class DeleteMemberDTO {
    @NotBlank(message = "文档ID不能为空")
    var docId: String? = null

    @NotBlank(message = "删除的用户ID不能为空")
    var userId: String? = null
}
