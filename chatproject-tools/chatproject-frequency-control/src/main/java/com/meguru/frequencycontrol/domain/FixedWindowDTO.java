package com.meguru.frequencycontrol.domain;

import lombok.Data;

/**
 * 限流策略定义
 */
@Data
public class FixedWindowDTO extends FrequencyControlDTO {
    /**
     * 频控时间范围, 默认单位秒
     */
    private Integer time;
}
