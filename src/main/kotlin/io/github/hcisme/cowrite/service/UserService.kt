package io.github.hcisme.cowrite.service

import io.github.hcisme.cowrite.entity.pojo.User
import io.github.hcisme.cowrite.entity.query.UserQuery
import io.github.hcisme.cowrite.entity.vo.PaginationResultVO
import io.github.hcisme.cowrite.entity.vo.TokenUserInfoVO

/**
 * 接口
 */
interface UserService {
	/**
	 * 根据条件查询列表
	 */
	fun findListByParam(param: UserQuery): List<User>

	/**
	 * 根据条件查询数量
	 */
	fun findCountByParam(param: UserQuery): Int

	/**
	 * 分页查询
	 */
	fun findListByPage(param: UserQuery): PaginationResultVO<User>

	/**
	 * 新增
	 */
	fun add(bean: User): Int

	/**
	 * 新增 (或更新)
	 */
	fun addOrUpdate(bean: User): Int

	/**
	 * 批量新增
	 */
	fun addBatch(list: List<User>): Int

	/**
	 * 批量新增 (或更新)
	 */
	fun addOrUpdateBatch(list: List<User>): Int

	/**
	 * 多条件更新
	 */
	fun updateByParam(bean: User, param: UserQuery): Int

	/**
	 * 多条件删除
	 */
	fun deleteByParam(param: UserQuery): Int

	/**
	 * 根据Id查询对象
	 */
	fun getUserById(id: String): User?

	/**
	 * 根据Id修改
	 */
	fun updateUserById(bean: User, id: String): Int

	/**
	 * 根据Id删除
	 */
	fun deleteUserById(id: String): Int

	/**
	 * 根据Email查询对象
	 */
	fun getUserByEmail(email: String): User?

	/**
	 * 根据Email修改
	 */
	fun updateUserByEmail(bean: User, email: String): Int

	/**
	 * 根据Email删除
	 */
	fun deleteUserByEmail(email: String): Int

	/**
	 * 用户注册
	 */
	fun register(email: String, username: String, password: String)

	/**
	 * 用户登录
	 */
	fun login(email: String, password: String): TokenUserInfoVO
}
