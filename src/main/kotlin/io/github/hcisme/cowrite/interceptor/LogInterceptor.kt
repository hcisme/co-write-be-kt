package io.github.hcisme.cowrite.interceptor

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
class LogInterceptor : HandlerInterceptor {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        randler: Any
    ): Boolean {
        if (request.requestURI.contains("/api/webjars")) {
            return true
        }

        val currentTime = LocalDateTime.now().format(formatter)
        println("$currentTime ${request.method} ${request.requestURI}")
        return true
    }
}