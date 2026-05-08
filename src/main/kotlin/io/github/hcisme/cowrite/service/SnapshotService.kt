package io.github.hcisme.cowrite.service

import io.github.hcisme.cowrite.entity.pojo.Snapshot

/**
 * 接口
 */
interface SnapshotService {

    fun saveSnapshot(docId: String, content: String, binaryState: ByteArray, creatorId: String)

    fun getLastSnapshot(userId: String, docId: String): Snapshot?
}
