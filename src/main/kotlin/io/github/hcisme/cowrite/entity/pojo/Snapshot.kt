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
) : Serializable
