package io.github.hcisme.cowrite.controller

import io.github.hcisme.cowrite.annotation.Access
import io.github.hcisme.cowrite.entity.dto.AddOrUpdateMemberDTO
import io.github.hcisme.cowrite.entity.dto.CheckPermissionDTO
import io.github.hcisme.cowrite.entity.dto.CreateDocDTO
import io.github.hcisme.cowrite.entity.dto.DeleteMemberDTO
import io.github.hcisme.cowrite.entity.pojo.Collaborator
import io.github.hcisme.cowrite.entity.vo.DocInCollaboratorVO
import io.github.hcisme.cowrite.entity.vo.ResponseVO
import io.github.hcisme.cowrite.service.CollaboratorService
import io.github.hcisme.cowrite.service.DocumentService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/doc")
class DocController(
    private val documentService: DocumentService,
    private val collaboratorService: CollaboratorService
) : ABaseController() {

    /**
     * 创建文档
     */
    @Access
    @PostMapping("/create")
    fun create(@Validated @RequestBody createDocDTO: CreateDocDTO): ResponseVO<Any?> {
        val user = getUserInfoByToken()!!
        val title = createDocDTO.title!!
        documentService.create(title = title, ownerId = user.id!!)
        return getSuccessResponseVO(null)
    }

    /**
     * 获取参加文档列表 (包括自己创建的和别人分享给我的)
     */
    @Access
    @GetMapping("/list")
    fun getList(): ResponseVO<List<Collaborator>> {
        val user = getUserInfoByToken()!!
        val list = documentService.getListByUserId(userId = user.id!!, orderBy = "created_time desc")
        return getSuccessResponseVO(list)
    }

    /**
     * 删除文档 (软删除)
     */
    @Access
    @DeleteMapping("/{docId}")
    fun delete(@PathVariable docId: String): ResponseVO<Any?> {
        val user = getUserInfoByToken()!!
        documentService.deleteDocByDocId(docId = docId, userId = user.id!!)
        return getSuccessResponseVO(null)
    }

    /**
     * 邀请别人加入你的文档
     */
    @Access
    @PostMapping("/share")
    fun share(@Validated @RequestBody addDTO: AddOrUpdateMemberDTO): ResponseVO<Any?> {
        val user = getUserInfoByToken()!!

        collaboratorService.addCollaborator(
            operatorId = user.id!!,
            docId = addDTO.docId!!,
            collaboratorId = addDTO.userId!!,
            role = addDTO.role!!
        )
        return getSuccessResponseVO(null)
    }

    /**
     * 进入文档时 检查权限
     */
    @Access
    @PostMapping("/check-permission")
    fun checkPermission(@Validated @RequestBody checkPermissionDTO: CheckPermissionDTO): ResponseVO<Collaborator?> {
        val user = getUserInfoByToken()!!
        val collaborator = collaboratorService.checkPermission(
            docId = checkPermissionDTO.docId!!,
            userId = user.id!!
        )
        return getSuccessResponseVO(collaborator)
    }

    /**
     * 查询一个文档内的所有成员
     */
    @Access
    @GetMapping("/collaborators/{docId}")
    fun getCollaboratorsByDocId(@PathVariable docId: String): ResponseVO<List<DocInCollaboratorVO>> {
        val list = collaboratorService.getCollaboratorsByDocId(docId = docId, order = "role")
        return getSuccessResponseVO(list)
    }

    /**
     * 修改文档内成员的权限
     */
    @Access
    @PostMapping("/collaborator/role")
    fun putCollaboratorRole(@Validated @RequestBody updateDTO: AddOrUpdateMemberDTO): ResponseVO<Any?> {
        val user = getUserInfoByToken()!!
        collaboratorService.updateRole(
            operatorId = user.id!!,
            docId = updateDTO.docId!!,
            collaboratorId = updateDTO.userId!!,
            role = updateDTO.role!!
        )
        return getSuccessResponseVO(null)
    }

    /**
     * 自己退出 或者 文档的拥有者删人
     */
    @Access
    @DeleteMapping("/collaborator")
    fun deleteMember(@Validated @RequestBody deleteMemberDTO: DeleteMemberDTO): ResponseVO<Any?> {
        val user = getUserInfoByToken()!!
        collaboratorService.deleteMember(
            operatorId = user.id!!,
            docId = deleteMemberDTO.docId!!,
            collaboratorId = deleteMemberDTO.userId!!
        )
        return getSuccessResponseVO(null)
    }
}
