package io.github.hcisme.cowrite.entity.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

class SaveSnapshotDTO {
    @NotBlank(message = "文档ID不能为空")
    var docId: String? = null

    @NotBlank(message = "创建人ID不能为空")
    var creatorId: String? = null

    @NotBlank(message = "文档内容不能为空")
    var content: String? = null

    @NotNull(message = "二进制状态不能为空")
    var binaryState: ByteArray? = null
}