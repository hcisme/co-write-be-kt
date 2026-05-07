package io.github.hcisme.cowrite.entity.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

class LoginDTO {
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @Size(max = 128, message = "邮箱长度不能超过128个字符")
    var email: String? = null

    @NotBlank(message = "密码不能为空")
    var password: String? = null

    @NotBlank(message = "验证码标识不能为空")
    var captchaKey: String? = null

    @NotBlank(message = "验证码不能为空")
    var captcha: String? = null
}