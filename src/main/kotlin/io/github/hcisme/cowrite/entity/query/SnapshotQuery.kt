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
) : BaseQuery()