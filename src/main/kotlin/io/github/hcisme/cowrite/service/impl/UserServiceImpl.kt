package io.github.hcisme.cowrite.service.impl

import io.github.hcisme.cowrite.entity.enums.PageSizeEnum
import io.github.hcisme.cowrite.entity.pojo.User
import io.github.hcisme.cowrite.entity.query.SimplePage
import io.github.hcisme.cowrite.entity.query.UserQuery
import io.github.hcisme.cowrite.entity.vo.PaginationResultVO
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

    /**
     * 根据条件查询列表
     */
    override fun findListByParam(param: UserQuery): List<User> {
        return userMapper.selectList(param)
    }

    /**
     * 根据条件查询数量
     */
    override fun findCountByParam(param: UserQuery): Int {
        return userMapper.selectCount(param)
    }

    /**
     * 分页查询
     */
    override fun findListByPage(param: UserQuery): PaginationResultVO<User> {
        val count = findCountByParam(param)
        val pageSizeEnum = if (param.pageSize == null) PageSizeEnum.SIZE15.size else param.pageSize!!
        val page = SimplePage(param.page, count, pageSizeEnum)
        param.simplePage = page
        val list = findListByParam(param)
        val result = PaginationResultVO(count, page.pageSize, page.page, page.pageTotal, list)
        return result
    }

    /**
     * 新增
     */
    override fun add(bean: User): Int {
        return userMapper.insert(bean)
    }

    /**
     * 新增 (或更新)
     */
    override fun addOrUpdate(bean: User): Int {
        return userMapper.insertOrUpdate(bean)
    }

    /**
     * 批量新增
     */
    override fun addBatch(list: List<User>): Int {
        return userMapper.insertBatch(list)
    }

    /**
     * 批量新增 (或更新)
     */
    override fun addOrUpdateBatch(list: List<User>): Int {
        return userMapper.insertOrUpdateBatch(list)
    }

    /**
     * 多条件更新
     */
    override fun updateByParam(bean: User, param: UserQuery): Int {
        return userMapper.updateByParam(bean, param)
    }

    /**
     * 多条件删除
     */
    override fun deleteByParam(param: UserQuery): Int {
        return userMapper.deleteByParam(param)
    }

    /**
     * 根据Id查询对象
     */
    override fun getUserById(id: String): User? {
        return userMapper.selectById(id)
    }

    /**
     * 根据Id修改
     */
    override fun updateUserById(bean: User, id: String): Int {
        return userMapper.updateById(bean, id)
    }

    /**
     * 根据Id删除
     */
    override fun deleteUserById(id: String): Int {
        return userMapper.deleteById(id)
    }

    /**
     * 根据Email查询对象
     */
    override fun getUserByEmail(email: String): User? {
        return userMapper.selectByEmail(email)
    }

    /**
     * 根据Email修改
     */
    override fun updateUserByEmail(bean: User, email: String): Int {
        return userMapper.updateByEmail(bean, email)
    }

    /**
     * 根据Email删除
     */
    override fun deleteUserByEmail(email: String): Int {
        return userMapper.deleteByEmail(email)
    }

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
