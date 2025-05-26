package com.meguru.frequencycontrol.service.strategy;

import com.meguru.chatproject.common.FrequencyControlConstant;
import com.meguru.chatproject.utils.RedisUtils;
import com.meguru.frequencycontrol.domain.SlidingWindowDTO;
import com.meguru.frequencycontrol.service.AbstractFrequencyControlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * 抽象类频控服务
 * -使用redis实现 滑动窗口是一种更加灵活的频率控制策略，它在一个滑动的时间窗口内限制操作的发生次数
 */
@Slf4j
@Service
public class SlidingWindowFrequencyController extends AbstractFrequencyControlService<SlidingWindowDTO> {

    @Override
    protected boolean reachRateLimit(Map<String, SlidingWindowDTO> frequencyControlMap) {
        List<String> frequencyKeys = new ArrayList<>(frequencyControlMap.keySet());
        for (String key : frequencyKeys){
            SlidingWindowDTO controlDTO = frequencyControlMap.get(key);
            Long count = RedisUtils.ZSetGet(key);
            int frequencyControlCount = controlDTO.getCount();
            if(Objects.nonNull(count) && count >= frequencyControlCount) {
                log.warn("滑动窗口频率限制 key:{}, count:{}", key, count);
                return true;
            }
        }
        return false;
    }

    @Override
    protected void addFrequencyControlStatisticsCount(Map<String, SlidingWindowDTO> frequencyControlMap) {
        List<String> frequencyKeys = new ArrayList<>(frequencyControlMap.keySet());
        for (String key : frequencyKeys) {
            SlidingWindowDTO controlDTO = frequencyControlMap.get(key);
            long period = controlDTO.getUnit().toMillis(controlDTO.getPeriod());
            long current = System.currentTimeMillis();
            long length = period * controlDTO.getWindowSize();
            long start = current - length;
            RedisUtils.ZSetAddAndExpire(key, start, length, current);
        }
    }

    @Override
    protected String getStrategyName() {
        return FrequencyControlConstant.SLIDING_WINDOW;
    }
}
