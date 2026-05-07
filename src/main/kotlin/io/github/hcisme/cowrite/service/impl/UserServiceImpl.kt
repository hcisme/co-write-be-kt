package io.github.hcisme.cowrite.service.impl

import io.github.hcisme.cowrite.entity.pojo.User
import io.github.hcisme.cowrite.entity.query.UserQuery
import io.github.hcisme.cowrite.entity.vo.TokenUserInfoVO
import io.github.hcisme.cowrite.exception.BusinessException
import io.github.hcisme.cowrite.mappers.UserMapper
import io.github.hcisme.cowrite.redis.RedisUtils
import io.github.hcisme.cowrite.redis.saveTokenUserInfo
import io.github.hcisme.cowrite.service.UserService
import io.github.hcisme.cowrite.utils.StringTools
import org.springframework.stereotype.Service
import java.time.LocalDateTime

/**
 * 接口实现
 */
@Service("userService")
class UserServiceImpl(
    private val userMapper: UserMapper<User, UserQuery>,
    private val redisUtils: RedisUtils
) : UserService {

    override fun register(email: String, username: String, password: String) {
        val user = userMapper.selectByEmail(email)
        if (user != null) {
            throw BusinessException("邮箱账号已存在")
        }

        val date = LocalDateTime.now()
        val id = StringTools.generateUserId()

        val newUser = User().apply {
            this.id = id
            this.email = email
            this.username = username
            this.password = StringTools.encodeMd5(password)
            this.createdTime = date
            this.updatedTime = date
        }
        userMapper.insert(newUser)
    }

    override fun login(email: String, password: String): TokenUserInfoVO {
        val user = userMapper.selectByEmail(email) ?: throw BusinessException("邮箱账号不存在")

        if (user.password != StringTools.encodeMd5(password)) {
            throw BusinessException("账号或密码错误")
        }

        val tokenUserInfo = TokenUserInfoVO().apply {
            this.id = user.id
            this.username = user.username
            this.email = user.email
            this.createdTime = user.createdTime
            this.updatedTime = user.updatedTime
        }
        redisUtils.saveTokenUserInfo(tokenUserInfo)
        return tokenUserInfo
    }
}
