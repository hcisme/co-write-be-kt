package io.github.hcisme.cowrite.controller

import io.github.hcisme.cowrite.annotation.Access
import io.github.hcisme.cowrite.entity.dto.CheckPermissionDTO
import io.github.hcisme.cowrite.entity.dto.SaveSnapshotDTO
import io.github.hcisme.cowrite.entity.pojo.Collaborator
import io.github.hcisme.cowrite.entity.pojo.Snapshot
import io.github.hcisme.cowrite.entity.vo.ResponseVO
import io.github.hcisme.cowrite.service.CollaboratorService
import io.github.hcisme.cowrite.service.SnapshotService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/internal/doc")
class InternalController(
    private val collaboratorService: CollaboratorService,
    private val snapshotService: SnapshotService
) : ABaseController() {

    @Access
    @PostMapping("/check-permission")
    fun checkPermission(@Validated @RequestBody checkPermissionDTO: CheckPermissionDTO): ResponseVO<Collaborator?> {
        val user = getUserInfoByToken()!!
        val collaborator= collaboratorService.checkPermission(
            docId = checkPermissionDTO.docId!!,
            userId = user.id!!
        )
        return getSuccessResponseVO(collaborator)
    }

    /**
     * node端 定期保存
     */
    @PostMapping("/saveSnapshot")
    fun saveSnapshot(@Validated @RequestBody saveSnapshotDTO: SaveSnapshotDTO): ResponseVO<Any?> {
        snapshotService.saveSnapshot(
            docId = saveSnapshotDTO.docId!!,
            content = saveSnapshotDTO.content!!,
            binaryState = saveSnapshotDTO.binaryState!!,
            creatorId = saveSnapshotDTO.creatorId!!
        )
        return getSuccessResponseVO(null)
    }

    @Access
    @GetMapping("/getLastSnapshot")
    fun getLastSnapshot(@RequestParam docId: String): ResponseVO<Snapshot?> {
        val user = getUserInfoByToken()!!
        val snapshot = snapshotService.getLastSnapshot(userId = user.id!!, docId = docId)
        return getSuccessResponseVO(snapshot)
    }
}
