package io.github.hcisme.cowrite.service

/**
 * 接口
 */
interface SnapshotService {

    fun saveSnapshot(docId: String, content: String, binaryState: ByteArray, creatorId: String)
}
