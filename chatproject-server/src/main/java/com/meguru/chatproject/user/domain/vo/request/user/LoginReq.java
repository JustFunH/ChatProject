package com.meguru.chatproject.user.domain.vo.request.user;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Description: 登录请求
 *
 * @author Meguru
 * @since 2025-05-28
 */
@Data
public class LoginReq {
    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度6-20位")
    private String password;
}
