package com.meguru.frequencycontrol.service.strategy;

import com.meguru.chatproject.common.FrequencyControlConstant;
import com.meguru.frequencycontrol.domain.TokenBucketDTO;
import com.meguru.frequencycontrol.mannager.TokenBucketManager;
import com.meguru.frequencycontrol.service.AbstractFrequencyControlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 抽象类频控服务
 * -使用redis实现 维护一个令牌桶来限制操作的发生次数
 */
@Slf4j
@Service
public class TokenBucketFrequencyController extends AbstractFrequencyControlService<TokenBucketDTO> {
    @Autowired
    private TokenBucketManager tokenBucketManager;

    @Override
    protected boolean reachRateLimit(Map<String, TokenBucketDTO> frequencyControlMap) {
        for (String key : frequencyControlMap.keySet()) {
            if (!tokenBucketManager.tryAcquire(key, 1)) {
                log.warn("令牌桶频率限制 key:{}", key);
                return true;
            }
        }
        return false;
    }

    @Override
    protected void addFrequencyControlStatisticsCount(Map<String, TokenBucketDTO> frequencyControlMap) {
        List<String> frequencyKeys = new ArrayList<>(frequencyControlMap.keySet());
        for (String key : frequencyKeys) {
            TokenBucketDTO controlDTO = frequencyControlMap.get(key);
            tokenBucketManager.createTokenBucket(key, controlDTO.getCapacity(), controlDTO.getRefillRate());
            tokenBucketManager.deductionToken(key, 1);
        }
    }

    @Override
    protected String getStrategyName() {
        return FrequencyControlConstant.TOKEN_BUCKET;
    }
}
