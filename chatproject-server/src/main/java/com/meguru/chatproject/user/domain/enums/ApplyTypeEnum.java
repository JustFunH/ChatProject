package com.meguru.chatproject.user.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Description : 申请类型枚举
 *
 * @author Meguru
 * @since 2025-05-28
 */
@Getter
@AllArgsConstructor
public enum ApplyTypeEnum {

    ADD_FRIEND(1, "加好友");

    private final Integer code;

    private final String desc;
}
