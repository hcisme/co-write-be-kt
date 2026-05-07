package io.github.hcisme.cowrite.mappers

import org.apache.ibatis.annotations.Param

/**
 * 
 */
interface UserMapper<T, P> : BaseMapper<T, P> {
	/**
	 * 根据Id获取对象
	 */
	fun selectById(@Param("id") id: String): T?

	/**
	 * 根据Id更新
	 */
	fun updateById(@Param("bean") t: T, @Param("id") id: String): Int

	/**
	 * 根据Id删除
	 */
	fun deleteById(@Param("id") id: String): Int

	/**
	 * 根据Email获取对象
	 */
	fun selectByEmail(@Param("email") email: String): T?

	/**
	 * 根据Email更新
	 */
	fun updateByEmail(@Param("bean") t: T, @Param("email") email: String): Int

	/**
	 * 根据Email删除
	 */
	fun deleteByEmail(@Param("email") email: String): Int

}
