package com.meguru.chatproject.user.domain.vo.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: 验证码请求
 *
 * @author Meguru
 * @since 2025-05-28
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendCodeReq {
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;
}
