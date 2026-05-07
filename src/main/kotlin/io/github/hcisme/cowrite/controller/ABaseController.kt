package io.github.hcisme.cowrite.controller

import io.github.hcisme.cowrite.entity.Constants
import io.github.hcisme.cowrite.entity.enums.ResponseCodeEnum
import io.github.hcisme.cowrite.entity.vo.ResponseVO
import io.github.hcisme.cowrite.entity.vo.TokenUserInfoVO
import io.github.hcisme.cowrite.exception.BusinessException
import io.github.hcisme.cowrite.redis.RedisUtils
import io.github.hcisme.cowrite.redis.getUserInfoByToken
import jakarta.annotation.Resource
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

open class ABaseController {
    @Resource
    private lateinit var redisUtils: RedisUtils

    companion object {
        const val STATUS_SUCCESS = "success"
        const val STATUS_ERROR = "error"
    }

    protected fun <T> getSuccessResponseVO(t: T): ResponseVO<T> {
        val responseVO = ResponseVO<T>()
        responseVO.status = STATUS_SUCCESS
        responseVO.code = ResponseCodeEnum.CODE_200.code
        responseVO.info = ResponseCodeEnum.CODE_200.msg
        responseVO.data = t
        return responseVO
    }

    protected fun <T> getBusinessErrorResponseVO(e: BusinessException, t: T): ResponseVO<T> {
        val vo = ResponseVO<T>()
        vo.status = STATUS_ERROR
        vo.code = e.code ?: ResponseCodeEnum.CODE_600.code
        vo.info = e.message
        vo.data = t
        return vo
    }

    protected fun <T> getServerErrorResponseVO(t: T): ResponseVO<T> {
        val vo = ResponseVO<T>()
        vo.status = STATUS_ERROR
        vo.code = ResponseCodeEnum.CODE_500.code
        vo.info = ResponseCodeEnum.CODE_500.msg
        vo.data = t
        return vo
    }

    protected fun getUserInfoByToken(): TokenUserInfoVO? {
        val request = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
        val token: String? = request.getHeader(Constants.TOKEN_KEY)
        return redisUtils.getUserInfoByToken(token)
    }
}
