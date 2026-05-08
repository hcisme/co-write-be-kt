package io.github.hcisme.cowrite.interceptor

import io.github.hcisme.cowrite.annotation.Access
import io.github.hcisme.cowrite.entity.Constants
import io.github.hcisme.cowrite.entity.enums.ResponseCodeEnum
import io.github.hcisme.cowrite.exception.BusinessException
import io.github.hcisme.cowrite.redis.RedisUtils
import io.github.hcisme.cowrite.redis.getUserInfoByToken
import jakarta.annotation.Resource
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor

@Component
class AccessInterceptor : HandlerInterceptor {
    @Resource
    private lateinit var redisUtils: RedisUtils

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {
        try {
            if (handler !is HandlerMethod) return true

            val method = handler.method
            val uri = request.requestURI

            if (uri.contains("/internal/")) {
                val secret = request.getHeader(Constants.INTERNAL_SECRET_KEY)
                if (secret == null || secret != Constants.INTERNAL_SECRET) {
                    throw BusinessException("内部非法调用")
                }
            }

            if (method.isAnnotationPresent(Access::class.java)) {
                val access = method.getAnnotation(Access::class.java)
                return checkAccess(access.isRequiredLoginAccess)
            }

            return true
        } catch (e: BusinessException) {
            throw e
        } catch (e: Throwable) {
            throw e
        }
    }

    private fun checkAccess(isRequiredLoginAccess: Boolean): Boolean {
        if (!isRequiredLoginAccess) {
            return true
        }

        val attributes = RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes? ?: return false
        val token = attributes.request.getHeader("token") ?: throw BusinessException(ResponseCodeEnum.CODE_401)
        redisUtils.getUserInfoByToken(token)
            ?: throw BusinessException(ResponseCodeEnum.CODE_401)
        return true
    }
}
