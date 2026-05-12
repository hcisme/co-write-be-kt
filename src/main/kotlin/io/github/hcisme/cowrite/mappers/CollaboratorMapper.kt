package io.github.hcisme.cowrite.mappers

import io.github.hcisme.cowrite.entity.vo.DocInCollaboratorVO
import org.apache.ibatis.annotations.Param

/**
 * 
 */
interface CollaboratorMapper<T, P> : BaseMapper<T, P> {
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

	/**
	 * 根据DocId 和 UserId获取对象
	 */
	fun selectByDocIdAndUserId(@Param("docId") docId: String, @Param("userId") userId: String): T?

	/**
	 * 根据DocId 和 UserId更新
	 */
	fun updateByDocIdAndUserId(@Param("bean") t: T, @Param("docId") docId: String, @Param("userId") userId: String): Int

	/**
	 * 根据DocId 和 UserId删除
	 */
	fun deleteByDocIdAndUserId(@Param("docId") docId: String, @Param("userId") userId: String): Int

	fun selectListByUserId(@Param("query") p: P): List<T>

	fun selectCollaboratorsByDocId(@Param("query") p: P): List<DocInCollaboratorVO>
}
