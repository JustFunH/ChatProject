package com.meguru.frequencycontrol.domain;


import lombok.Data;

/**
 * 限流策略定义
 */
@Data
public class SlidingWindowDTO extends FrequencyControlDTO {
    /**
     * 窗口大小, 默认10s
     */
    private int windowSize;

    /**
     * 窗口最小周期 1s
     */
    private int period;

}
