package io.github.hcisme.cowrite.service

import io.github.hcisme.cowrite.entity.vo.TokenUserInfoVO

/**
 * 接口
 */
interface UserService {

    /**
     * 用户注册
     */
    fun register(email: String, username: String, password: String)

    /**
     * 用户登录
     */
    fun login(email: String, password: String): TokenUserInfoVO
}
