package io.github.hcisme.cowrite.utils

import io.github.hcisme.cowrite.entity.Constants
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.random.Random

object StringTools {
    private const val CHARS_ALPHANUMERIC = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"

    fun getRandomNumber(length: Int): String {
        require(length > 0) { "长度必须 > 0" }

        return buildString {
            repeat(length) {
                append(Random.nextInt(0, 10))
            }
        }
    }

    fun generateUserId(): String = Constants.USERID_PREFIX_KEY + getRandomNumber(Constants.LENGTH_11)

    fun encodeMd5(pwd: String): String {
        return try {
            val md = MessageDigest.getInstance("MD5")
            val digest = md.digest(pwd.toByteArray())
            BigInteger(1, digest).toString(16).padStart(32, '0')
        } catch (e: Exception) {
            throw RuntimeException("密码加密失败", e)
        }
    }
}