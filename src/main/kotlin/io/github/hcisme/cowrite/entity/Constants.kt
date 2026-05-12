package io.github.hcisme.cowrite.entity

object Constants {
    private const val REDIS_KEY_PREFIX = "cowrite"

    const val INTERNAL_SECRET_KEY = "Internal-Secret"
    const val INTERNAL_SECRET = "d0424d61-d3cf-4f67-84ff-a76fe42e5a49"

    const val REDIS_KEY_CAPTCHA_KEY = "$REDIS_KEY_PREFIX:captcha:"

    const val TOKEN_KEY = "token"

    /**
     * redis token key
     */
    const val REDIS_KEY_TOKEN = "$REDIS_KEY_PREFIX:$TOKEN_KEY:"

    /**
     * 密码正则
     *
     *  密码必须是8到18个字符，至少包含一个数字，至少包含一个字母，可以包含特殊字符 ~!@#$%^&*_
     */
    const val REGEX_PASSWORD = "^(?=.*\\d)(?=.*[a-zA-Z])[\\da-zA-Z~!@#$%^&*_]{8,18}$"

    /**
     * 单位 ms
     *
     * 一分钟
     */
    const val REDIS_TIME_1MIN = 60000L

    /**
     * 单位 ms
     *
     * 一天
     */
    const val REDIS_KEY_EXPIRES_ONE_DAY = REDIS_TIME_1MIN * 60 * 24

    /**
     * token 失效时间 15天 单位 ms
     */
    const val REDIS_KEY_TOKEN_EXPIRES_15_DAY = REDIS_KEY_EXPIRES_ONE_DAY * 15

    const val USERID_PREFIX_KEY = "U"

    const val LENGTH_11 = 11
}