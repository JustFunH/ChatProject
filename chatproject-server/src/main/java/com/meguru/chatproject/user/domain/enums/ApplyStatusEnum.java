package com.meguru.chatproject.user.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Description : 申请状态枚举
 *
 * @author Meguru
 * @since 2025-05-28
 */
@Getter
@AllArgsConstructor
public enum ApplyStatusEnum {

    WAIT_APPROVAL(1, "待审批"),

    AGREE(2, "同意");

    private final Integer code;

    private final String desc;
}
