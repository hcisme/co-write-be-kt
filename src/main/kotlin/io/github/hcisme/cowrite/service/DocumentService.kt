package io.github.hcisme.cowrite.service

import io.github.hcisme.cowrite.entity.pojo.Collaborator

/**
 * 接口
 */
interface DocumentService {

    fun create(title: String, ownerId: String)

    fun getListByUserId(userId: String): List<Collaborator>

    fun deleteDocByDocId(docId: String, userId: String)
}
