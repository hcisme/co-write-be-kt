package io.github.hcisme.cowrite.controller

import io.github.hcisme.cowrite.entity.dto.LoginDTO
import io.github.hcisme.cowrite.entity.dto.RegisterDTO
import io.github.hcisme.cowrite.entity.vo.ResponseVO
import io.github.hcisme.cowrite.entity.vo.TokenUserInfoVO
import io.github.hcisme.cowrite.exception.BusinessException
import io.github.hcisme.cowrite.redis.RedisUtils
import io.github.hcisme.cowrite.redis.cleanCaptcha
import io.github.hcisme.cowrite.redis.getCaptcha
import io.github.hcisme.cowrite.redis.saveCaptcha
import io.github.hcisme.cowrite.service.UserService
import io.springboot.captcha.ArithmeticCaptcha
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
class UserController(
    private val userService: UserService,
    private val redisUtils: RedisUtils
) : ABaseController() {

    @GetMapping("/captcha")
    fun getCheckCode(): ResponseVO<Any> {
        val map = HashMap<String, Any>()
        val captcha = ArithmeticCaptcha(100, 42)
        val code = captcha.text()

        val captchaKey = redisUtils.saveCaptcha(code)
        map["captcha"] = captcha.toBase64()
        map["captchaKey"] = captchaKey

        return getSuccessResponseVO(map)
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    fun register(@Validated @RequestBody registerDTO: RegisterDTO): ResponseVO<Any?> {
        val captchaKey = registerDTO.captchaKey!!

        try {
            val captcha = redisUtils.getCaptcha(captchaKey) ?: throw BusinessException("验证码过期 请重新获取")
            if (!registerDTO.captcha!!.equals(other = captcha, ignoreCase = true)) {
                throw BusinessException("图片验证码不正确")
            }

            userService.register(registerDTO.email!!, registerDTO.nickName!!, registerDTO.password!!)
            return getSuccessResponseVO(null)
        } finally {
            redisUtils.cleanCaptcha(captchaKey)
        }
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    fun login(@Validated @RequestBody loginDTO: LoginDTO): ResponseVO<TokenUserInfoVO> {
        val captchaKey = loginDTO.captchaKey!!

        try {
            val captcha = redisUtils.getCaptcha(captchaKey) ?: throw BusinessException("验证码过期 请重新获取")
            if (!loginDTO.captcha!!.equals(other = captcha, ignoreCase = true)) {
                throw BusinessException("图片验证码不正确")
            }

            val userVO = userService.login(loginDTO.email!!, loginDTO.password!!)
            return getSuccessResponseVO(userVO)
        } finally {
            redisUtils.cleanCaptcha(captchaKey)
        }
    }
}
