package io.github.hcisme.cowrite.interceptor

import jakarta.annotation.Resource
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class InterceptorConfig : WebMvcConfigurer {
    @Resource
    private lateinit var accessInterceptor: AccessInterceptor

    @Resource
    private lateinit var logInterceptor: LogInterceptor

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry
            .addInterceptor(logInterceptor)
            .addPathPatterns("/**")

        registry
            .addInterceptor(accessInterceptor)
            .addPathPatterns("/**")
    }
}
