package io.github.hcisme.cowrite.redis

import io.github.hcisme.cowrite.entity.Constants
import io.github.hcisme.cowrite.entity.vo.TokenUserInfoVO
import java.util.*

/**
 * 保存验证码
 */
fun RedisUtils.saveCaptcha(code: String, time: Long = Constants.REDIS_TIME_1MIN * 10L): String {
    val captchaKey = UUID.randomUUID().toString()
    this.setValueAndExpire(Constants.REDIS_KEY_CAPTCHA_KEY + captchaKey, code, time)
    return captchaKey
}

/**
 * 获取验证码
 */
fun RedisUtils.getCaptcha(captchaKey: String) = this.getValue(Constants.REDIS_KEY_CAPTCHA_KEY + captchaKey) as String?

/**
 * 根据 `captchaKey` 清空当前验证码
 */
fun RedisUtils.cleanCaptcha(captchaKey: String) {
    this.delete(Constants.REDIS_KEY_CAPTCHA_KEY + captchaKey)
}

/**
 * 保存登录信息到redis
 */
fun RedisUtils.saveTokenUserInfo(tokenUserInfoVO: TokenUserInfoVO) {
    val token = UUID.randomUUID().toString()
    tokenUserInfoVO.apply {
        this@apply.token = token
        this@apply.expireAt = System.currentTimeMillis() + Constants.REDIS_KEY_TOKEN_EXPIRES_15_DAY

        this@saveTokenUserInfo.setValueAndExpire(
            Constants.REDIS_KEY_TOKEN + token,
            this@apply,
            Constants.REDIS_KEY_TOKEN_EXPIRES_15_DAY.toLong()
        )
    }
}

/**
 * 通过 `token` 获取用户信息
 */
fun RedisUtils.getUserInfoByToken(token: String?): TokenUserInfoVO? {
    return this.getValue(Constants.REDIS_KEY_TOKEN + token) as TokenUserInfoVO?
}

/**
 * 通过 token 清空指定用户redis里的 `token` 信息
 */
fun RedisUtils.cleanTokenByToken(token: String?) {
    this.delete(Constants.REDIS_KEY_TOKEN + token)
}

/**
 * 更新 redis 里的用户信息
 */
fun RedisUtils.updateTokenUserInfo(tokenUserInfoVO: TokenUserInfoVO) {
    this.setValue(
        Constants.REDIS_KEY_TOKEN + tokenUserInfoVO.token,
        tokenUserInfoVO
    )
}
