package io.github.hcisme.cowrite.controller

import io.github.hcisme.cowrite.entity.dto.CheckPermissionDTO
import io.github.hcisme.cowrite.entity.dto.SaveSnapshotDTO
import io.github.hcisme.cowrite.entity.vo.ResponseVO
import io.github.hcisme.cowrite.service.CollaboratorService
import io.github.hcisme.cowrite.service.SnapshotService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/internal/doc")
class InternalController(
    private val collaboratorService: CollaboratorService,
    private val snapshotService: SnapshotService
) : ABaseController() {

    @PostMapping("/check-permission")
    fun checkPermission(@Validated @RequestBody checkPermissionDTO: CheckPermissionDTO): ResponseVO<Any?> {
        collaboratorService.checkPermission(
            docId = checkPermissionDTO.docId!!,
            userId = checkPermissionDTO.userId!!
        )
        return getSuccessResponseVO(null)
    }

    @PostMapping("/snapshot")
    fun saveSnapshot(@Validated @RequestBody saveSnapshotDTO: SaveSnapshotDTO): ResponseVO<Any?> {
        snapshotService.saveSnapshot(
            docId = saveSnapshotDTO.docId!!,
            content = saveSnapshotDTO.content!!,
            binaryState = saveSnapshotDTO.binaryState!!,
            creatorId = saveSnapshotDTO.creatorId!!
        )
        return getSuccessResponseVO(null)
    }
}
