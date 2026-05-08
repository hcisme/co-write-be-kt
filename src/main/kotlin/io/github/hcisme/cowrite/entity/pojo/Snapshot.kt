package io.github.hcisme.cowrite.entity.pojo

import java.io.Serializable
import java.time.LocalDateTime

data class Snapshot(

    /**
     * 文档快照表id
     */
    var id: Long? = null,

    /**
     * 文档id
     */
    var docId: String? = null,

    /**
     * 存储文档的内容
     */
    var content: String? = null,

    /**
     * 存储 Yjs 的 Uint8Array 二进制快照
     */
    var binaryState: ByteArray? = null,

    /**
     * 版本名字
     */
    var versionName: String? = null,

    /**
     * 创建者的id
     */
    var creatorId: String? = null,

    /**
     * 创建时间
     */
    var createdTime: LocalDateTime? = null
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Snapshot

        if (id != other.id) return false
        if (docId != other.docId) return false
        if (content != other.content) return false
        if (!binaryState.contentEquals(other.binaryState)) return false
        if (versionName != other.versionName) return false
        if (creatorId != other.creatorId) return false
        if (createdTime != other.createdTime) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (docId?.hashCode() ?: 0)
        result = 31 * result + (content?.hashCode() ?: 0)
        result = 31 * result + (binaryState?.contentHashCode() ?: 0)
        result = 31 * result + (versionName?.hashCode() ?: 0)
        result = 31 * result + (creatorId?.hashCode() ?: 0)
        result = 31 * result + (createdTime?.hashCode() ?: 0)
        return result
    }
}
