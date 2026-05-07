package io.github.hcisme.cowrite.entity.enums;

enum class DocRoleEnum(val role: Int, val desc: String) {
    OWNER(0, "持有者"),
    EDITOR(1, "可编辑"),
    VIEWER(2, "只读");

    companion object {
        /**
         * 通过角色值获取对应枚举信息
         */
        fun getByRole(role: Int?): DocRoleEnum? {
            return entries.find { it.role == role }
        }
    }

    override fun toString(): String {
        return "DocRoleEnum(role=$role, desc='$desc')"
    }
}