package com.meguru.chatproject.common.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncoderUtil {
    // 统一使用同一个编码器实例
    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * 加密明文密码
     *
     * @param rawPassword
     * @return
     */
    public static String encode(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    /**
     * 校验明文密码和加密后的密码是否匹配
     *
     * @param rawPassword
     * @param encodedPassword
     * @return
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }
}
