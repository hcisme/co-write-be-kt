package io.github.hcisme.cowrite.entity.query

import java.time.LocalDateTime

/**
 *  查询对象
 */
data class SnapshotQuery(

    /**
     * 文档快照表id
     */
    var id: Long? = null,

    /**
     * 文档id
     */
    var docId: String? = null,

    var docIdFuzzy: String? = null,

    /**
     * 存储文档的内容
     */
    var content: String? = null,

    var contentFuzzy: String? = null,

    /**
     * 存储 Yjs 的 Uint8Array 二进制快照
     */
    var binaryState: ByteArray? = null,

    /**
     * 版本名字
     */
    var versionName: String? = null,

    var versionNameFuzzy: String? = null,

    /**
     * 创建者的id
     */
    var creatorId: String? = null,

    var creatorIdFuzzy: String? = null,

    /**
     * 创建时间
     */
    var createdTime: LocalDateTime? = null,

    var createdTimeStart: String? = null,

    var createdTimeEnd: String? = null
) : BaseQuery() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SnapshotQuery

        if (id != other.id) return false
        if (docId != other.docId) return false
        if (docIdFuzzy != other.docIdFuzzy) return false
        if (content != other.content) return false
        if (contentFuzzy != other.contentFuzzy) return false
        if (!binaryState.contentEquals(other.binaryState)) return false
        if (versionName != other.versionName) return false
        if (versionNameFuzzy != other.versionNameFuzzy) return false
        if (creatorId != other.creatorId) return false
        if (creatorIdFuzzy != other.creatorIdFuzzy) return false
        if (createdTime != other.createdTime) return false
        if (createdTimeStart != other.createdTimeStart) return false
        if (createdTimeEnd != other.createdTimeEnd) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (docId?.hashCode() ?: 0)
        result = 31 * result + (docIdFuzzy?.hashCode() ?: 0)
        result = 31 * result + (content?.hashCode() ?: 0)
        result = 31 * result + (contentFuzzy?.hashCode() ?: 0)
        result = 31 * result + (binaryState?.contentHashCode() ?: 0)
        result = 31 * result + (versionName?.hashCode() ?: 0)
        result = 31 * result + (versionNameFuzzy?.hashCode() ?: 0)
        result = 31 * result + (creatorId?.hashCode() ?: 0)
        result = 31 * result + (creatorIdFuzzy?.hashCode() ?: 0)
        result = 31 * result + (createdTime?.hashCode() ?: 0)
        result = 31 * result + (createdTimeStart?.hashCode() ?: 0)
        result = 31 * result + (createdTimeEnd?.hashCode() ?: 0)
        return result
    }
}