package io.github.hcisme.cowrite.entity.enums;

enum class DeleteStatusEnum(val status: Int, val desc: String) {
    NOT_DELETED(0, "未删除"),
    DELETED(1, "已删除");

    companion object {
        /**
         * 通过状态值获取对应枚举信息
         */
        fun getByStatus(status: Int?): DeleteStatusEnum? {
            return entries.find { it.status == status }
        }
    }

    override fun toString(): String {
        return "DeleteStatusEnum(status=$status, desc='$desc')"
    }
}