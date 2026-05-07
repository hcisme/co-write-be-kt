package io.github.hcisme.cowrite.mappers

import org.apache.ibatis.annotations.Param

/**
 * 
 */
interface SnapshotMapper<T, P> : BaseMapper<T, P> {
	/**
	 * 根据Id获取对象
	 */
	fun selectById(@Param("id") id: Long): T?

	/**
	 * 根据Id更新
	 */
	fun updateById(@Param("bean") t: T, @Param("id") id: Long): Int

	/**
	 * 根据Id删除
	 */
	fun deleteById(@Param("id") id: Long): Int

	fun selectLastByDocId(@Param("docId") docId: String): T?
}
