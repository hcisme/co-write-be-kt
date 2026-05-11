package io.github.hcisme.cowrite.service

import io.github.hcisme.cowrite.entity.pojo.Collaborator

/**
 * 接口
 */
interface CollaboratorService {

    fun addCollaborator(operatorId: String, docId: String, collaboratorId: String, role: Int)

    fun checkPermission(docId: String, userId: String): Collaborator
}
