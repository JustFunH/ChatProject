package com.meguru.frequencycontrol.domain;

import com.meguru.frequencycontrol.exception.CommonErrorEnum;
import com.meguru.frequencycontrol.exception.FrequencyControlException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

@Data
@Slf4j
public class TokenBucketDTO extends FrequencyControlDTO {
    private final long capacity; // 令牌桶容量
    private final double refillRate; // 令牌桶填充速率
    private double tokens; // 当前令牌数量
    private long lastRefillTime; // 上次填充时间

    private final ReentrantLock lock = new ReentrantLock();

    public TokenBucketDTO(long capacity, double refillRate) {
        if (capacity <= 0 || refillRate <= 0) {
            throw new FrequencyControlException(CommonErrorEnum.CAPACITY_REFILL_ERROR);
        }
        this.capacity = capacity;
        this.refillRate = refillRate;
        this.tokens = capacity;
        this.lastRefillTime = System.nanoTime();
    }


    public boolean tryAcquire(int permits) {
        lock.lock();
        try {
            refillToken();
            return tokens >= permits;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 获取令牌
     *
     * @param permits
     */
    public void deductionToken(int permits) {
        lock.lock();
        try {
            tokens -= permits;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 补充令牌
     */
    public void refillToken() {
        long currentTime = System.nanoTime();
        double elapsedTime = (currentTime - lastRefillTime) / 1e9;
        double tokenToAdd = elapsedTime * refillRate;
        log.info("补充令牌: {}", tokenToAdd);
        tokens = Math.min(capacity, tokens + tokenToAdd);
        log.info("当前令牌: {}", tokens);
        lastRefillTime = currentTime;
    }
}
