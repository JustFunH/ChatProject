package com.meguru.frequencycontrol.mannager;

import com.meguru.frequencycontrol.domain.TokenBucketDTO;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenBucketManager {
    private final Map<String, TokenBucketDTO> tokenBucketMap = new ConcurrentHashMap<>();

    public void createTokenBucket(String key, long capacity, double refillRate) {
        tokenBucketMap.putIfAbsent(key, new TokenBucketDTO(capacity, refillRate));
    }

    public void removeTokenBucket(String key) {
        tokenBucketMap.remove(key);
    }

    public boolean tryAcquire(String key, int permits) {
        TokenBucketDTO tokenBucket = tokenBucketMap.get(key);
        if (tokenBucket == null) {
            return false;
        }
        return tokenBucket.tryAcquire(permits);
    }

    public void deductionToken(String key, int permits) {
        TokenBucketDTO tokenBucket = tokenBucketMap.get(key);
        if (tokenBucket != null) {
            tokenBucket.deductionToken(permits);
        }
    }
}
