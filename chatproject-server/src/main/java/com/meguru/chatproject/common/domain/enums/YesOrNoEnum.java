package com.meguru.chatproject.common.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description: ws前端请求类型枚举
 * @author Meguru
 * @since 2025-05-28
 */
@AllArgsConstructor
@Getter
public enum YesOrNoEnum {
    NO(0, "否"),
    YES(1, "是"),
    ;

    private final Integer status;
    private final String desc;

    private static Map<Integer, YesOrNoEnum> cache;

    static {
        cache = Arrays.stream(YesOrNoEnum.values()).collect(Collectors.toMap(YesOrNoEnum::getStatus, Function.identity()));
    }

    public static YesOrNoEnum of(Integer type) {
        return cache.get(type);
    }

    public static Integer toStatus(Boolean bool) {
        return bool ? YES.getStatus() : NO.getStatus();
    }
}
