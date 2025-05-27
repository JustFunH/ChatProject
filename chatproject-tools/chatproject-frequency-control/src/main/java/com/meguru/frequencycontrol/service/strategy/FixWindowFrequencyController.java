package com.meguru.frequencycontrol.service.strategy;

import com.meguru.chatproject.utils.RedisUtils;
import com.meguru.frequencycontrol.constant.FrequencyControlConstant;
import com.meguru.frequencycontrol.domain.FixedWindowDTO;
import com.meguru.frequencycontrol.service.AbstractFrequencyControlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 抽象类频控服务 -使用redis实现 固定时间内不超过固定次数的限流类
 */
@Slf4j
@Service
public class FixWindowFrequencyController extends AbstractFrequencyControlService<FixedWindowDTO> {
    @Override
    protected boolean reachRateLimit(Map<String, FixedWindowDTO> frequencyControlMap) {
        List<String> frequencyKeys = new ArrayList<>(frequencyControlMap.keySet());
        List<Integer> countList = RedisUtils.mget(frequencyKeys, Integer.class);
        for (int i = 0; i < frequencyKeys.size(); i++) {
            String key = frequencyKeys.get(i);
            Integer count = countList.get(i);
            int frequencyControlCount = frequencyControlMap.get(key).getCount();
            if (Objects.nonNull(count) && count >= frequencyControlCount) {
                log.warn("计数器频率限制 key:{}, count:{}", key, count);
                return true;
            }
        }
        return false;
    }

    @Override
    protected void addFrequencyControlStatisticsCount(Map<String, FixedWindowDTO> frencyControlMap) {
        frencyControlMap.forEach((k, v) -> RedisUtils.inc(k, v.getTime(), v.getUnit()));
    }

    @Override
    protected String getStrategyName() {
        return FrequencyControlConstant.TOTAL_COUNT_WITH_IN_FIX_TIME;
    }
}
