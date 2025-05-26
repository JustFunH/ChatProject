package com.meguru.frequencycontrol.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CommonErrorEnum implements ErrorEnum {
    //==================================common==================================
    BUSINESS_ERROR(1001, "{0}"),
    //==================================user==================================
    //==================================chat==================================
    SYSTEM_ERROR(1001, "系统出小差了，请稍后再试哦~~"),
    CAPACITY_REFILL_ERROR(1001, "容量和补充速率必须为正数"),
    FREQUENCY_CONTROL_KEY_NULL(1002, "限流策略的Key字段不允许出现空值"), // 新增这一行

    PARAM_VALID(-2, "参数校验失败{0}"),
    FREQUENCY_LIMIT(-3, "请求太频繁了，请稍后再试哦~~"),
    LOCK_LIMIT(-4, "请求太频繁了，请稍后再试哦~~"),
    ;

    private final Integer code;
    private final String msg;

    @Override
    public Integer getErrorCode() {
        return this.code;
    }

    @Override
    public String getErrorMsg() {
        return this.msg;
    }
}
