package com.meguru.chatproject.user.service;

/**
 * <p>
 * 邮件 服务类
 * </p>
 *
 * @author Meguru
 * @since 2025-05-28
 */
public interface ICaptchaService {
    void sendEmailCaptcha(String email);
}
