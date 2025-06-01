package com.meguru.chatproject.common.exception;

import lombok.AllArgsConstructor;

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
    USER_NEED_LOGIN(1001, "请先登录"),
    USER_POWER_ERROR(1001, "无权限操作"),
    USERNAME_HAS_SENSITIVE_WORD(1002, "用户名含有敏感词"),
    USERNAME_HAS_EXIST(1003, "用户名已存在"),
    LOGIN_ERROR(1004, "用户名或密码错误"),
    USERNAME_IS_NULL(1005, "用户名或密码不能为空"),
    PASSWORD_NOT_EQUAL(1006, "两次密码不一致"),
    VERIFICATION_CODE_ERROR(1007, "验证码错误"),
    //===================================email==================================
    EMAIL_CONFIGURATION_EXCEPTION(1004, "邮箱配置异常"),
    EMAIL_VERIFICATION_CODE_FAILED(1005, "重置验证码失败"),
    EMAIL_SENDING_EXCEPTION(1006, "邮件发送异常"),
    //===============================emoji===============================
    EMOJI_NOT_EXIST(1001, "表情不存在"),
    EMOJI_HAS_EXIST(1002, "表情已存在"),
    EMOJI_OUT_OF_RANGE(1003, "最多只能添加30个表情~"),
    //==================================friend==================================
    FRIEND_HAS_EXIST(1001, "已经是好友"),
    FRIEND_NOT_EXIST(1002, "好友不存在"),
    FRIEND_RECORD_NOT_EXIST(1002, "申请记录不存在"),
    //==================================chat==================================
    TOTAL_GROUP(1001, "全员群无需邀请好友"),
    GROUP_NOT_EXIST(1001, "您已经被移除该群"),
    GROUP_ID_ERROR(1002, "房间号有误"),
    GROUP_NUMBER_LIMIT(1003, "每个人只能创建一个群"),
    GROUP_FRIEND_ERROR(1004, "房间创建失败，好友数量不对"),
    MESSAGE_NOT_EXIST(1003, "消息不存在"),
    MESSAGE_OUT_TIME(1004, "超过2分钟的消息不能撤回"),
    MESSAGE_NOT_RECALL(1005, "消息无法撤回"),
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
