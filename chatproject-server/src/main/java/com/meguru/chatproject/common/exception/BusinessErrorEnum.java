package com.meguru.chatproject.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Description: 业务校验异常码
 *
 * @author Meguru
 * @since 2025-05-29
 */
@AllArgsConstructor
public enum BusinessErrorEnum implements ErrorEnum {
    //====================OSS====================
    OSS_ERROR(1001, "场景有误"),
    //========================================
    BUSINESS_ERROR(1001, "{0}"),
    //==================================user==================================
    USER_POWER_ERROR(1001, "无权限操作"),
    USERNAME_HAS_SENSITIVE_WORD(1002, "用户名含有敏感词"),
    USERNAME_HAS_EXIST(1003, "用户名已存在"),
    EMAIL_CONFIGURATION_EXCEPTION(1004, "邮箱配置异常"),
    EMAIL_VERIFICATION_CODE_FAILED(1005, "重置验证码失败"),
    EMAIL_SENDING_EXCEPTION(1006, "邮件发送异常"),
    //==================================chat==================================
    SYSTEM_ERROR(1001, "系统出小差了，请稍后再试哦~~"),
    ;
    private Integer code;
    private String msg;

    @Override
    public Integer getErrorCode() {
        return code;
    }

    @Override
    public String getErrorMsg() {
        return msg;
    }
}
