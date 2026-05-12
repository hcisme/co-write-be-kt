package io.github.hcisme.cowrite.service

import io.github.hcisme.cowrite.entity.pojo.Collaborator
import io.github.hcisme.cowrite.entity.vo.DocInCollaboratorVO

/**
 * 接口
 */
interface CollaboratorService {

    fun addCollaborator(operatorId: String, docId: String, collaboratorId: String, role: Int)

    fun checkPermission(docId: String, userId: String): Collaborator

    fun getCollaboratorsByDocId(docId: String, order: String): List<DocInCollaboratorVO>

    fun updateRole(operatorId: String, docId: String, collaboratorId: String, role: Int)

    fun deleteMember(operatorId: String, docId: String, collaboratorId: String)
}
