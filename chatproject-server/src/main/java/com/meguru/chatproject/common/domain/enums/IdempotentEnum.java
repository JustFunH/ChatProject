package com.meguru.chatproject.common.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description: 幂等类型
 * @author Meguru
 * @since 2025-05-28
 */
@AllArgsConstructor
@Getter
public enum IdempotentEnum {
    UID(1, "uid"),
    MSG_ID(2, "消息id"),
    ;

    private final Integer type;
    private final String desc;

    private static Map<Integer, IdempotentEnum> cache;

    static {
        cache = Arrays.stream(IdempotentEnum.values()).collect(Collectors.toMap(IdempotentEnum::getType, Function.identity()));
    }

    public static IdempotentEnum of(Integer type) {
        return cache.get(type);
    }
}
