package io.github.hcisme.cowrite.entity.dto

import io.github.hcisme.cowrite.entity.Constants
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

class RegisterDTO {

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @Size(max = 128, message = "邮箱长度不能超过128个字符")
    var email: String? = null

    @NotBlank(message = "昵称不能为空")
    @Size(max = 20, message = "昵称长度不能超过20个字符")
    var nickName: String? = null

    @NotBlank(message = "密码不能为空")
    @Size(min = 8, max = 18, message = "密码长度必须在8到18个字符之间")
    @Pattern(regexp = Constants.REGEX_PASSWORD, message = "密码格式不符合要求")
    var password: String? = null

    @NotBlank(message = "验证码标识不能为空")
    var captchaKey: String? = null

    @NotBlank(message = "验证码不能为空")
    var captcha: String? = null
}