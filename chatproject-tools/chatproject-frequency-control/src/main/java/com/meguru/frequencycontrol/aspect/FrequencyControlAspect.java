package com.meguru.frequencycontrol.aspect;

import cn.hutool.core.util.StrUtil;
import com.meguru.frequencycontrol.annotation.FrequencyControl;
import com.meguru.frequencycontrol.constant.FrequencyControlConstant;
import com.meguru.frequencycontrol.domain.FixedWindowDTO;
import com.meguru.frequencycontrol.domain.FrequencyControlDTO;
import com.meguru.frequencycontrol.domain.SlidingWindowDTO;
import com.meguru.frequencycontrol.domain.TokenBucketDTO;
import com.meguru.frequencycontrol.service.FrequencyControlUtil;
import com.meguru.frequencycontrol.util.RequestHolder;
import com.meguru.frequencycontrol.util.SpEIUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 频控实现
 */
@Slf4j
@Aspect
@Component
public class FrequencyControlAspect {
    @Around("@annotation(com.meguru.frequencycontrol.annotation.FrequencyControl) || @annotation(com.meguru.frequencycontrol.annotation.FrequencyControlContainer)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        FrequencyControl[] anntotations = method.getAnnotationsByType(FrequencyControl.class);
        String strategy = FrequencyControlConstant.TOTAL_COUNT_WITH_IN_FIX_TIME;
        Map<String, FrequencyControl> keyMap = new HashMap<>();
        for (int i = 0; i < anntotations.length; i++) {
            // 获取频控注解
            FrequencyControl frequencyControl = anntotations[i];
            String prefix = StrUtil.isBlank(frequencyControl.prefixKey()) ? method.toGenericString() + ":index:" + i : frequencyControl.prefixKey();
            String key = "";
            switch (frequencyControl.target()) {
                case EL:
                    key = SpEIUtils.parseSpEl(method, joinPoint.getArgs(), frequencyControl.spEl());
                    break;
                case IP:
                    key = RequestHolder.get().getIp();
                    break;
                case UID:
                    key = RequestHolder.get().getUid().toString();
            }
            keyMap.put(prefix + ":" + key, frequencyControl);
            strategy = frequencyControl.strategy();
        }
        // 将注解的参数转换为编程式调用需要的参数
        if (FrequencyControlConstant.TOTAL_COUNT_WITH_IN_FIX_TIME.equals(strategy)) {
            // 调用编程式注解 固定窗口
            List<FrequencyControlDTO> frequencyControlDTOS = keyMap.entrySet().stream().map(entrySet -> bulidFixedWindowDTO(entrySet.getKey(), entrySet.getValue())).collect(Collectors.toList());
            return FrequencyControlUtil.executeWithFrequencyControlList(strategy, frequencyControlDTOS, joinPoint::proceed);
        } else if (FrequencyControlConstant.TOKEN_BUCKET.equals(strategy)) {
            // 调用编程式注解 令牌桶
            List<FrequencyControlDTO> frequencyControlDTOS = keyMap.entrySet().stream().map(entrySet -> buildTokenBucketDTO(entrySet.getKey(), entrySet.getValue())).collect(Collectors.toList());
            return FrequencyControlUtil.executeWithFrequencyControlList(strategy, frequencyControlDTOS, joinPoint::proceed);
        } else {
            // 调用编程式注解 滑动窗口
            List<FrequencyControlDTO> frequencyControlDTOS = keyMap.entrySet().stream().map(entrySet -> buildSlidingWindowDTO(entrySet.getKey(), entrySet.getValue())).collect(Collectors.toList());
            return FrequencyControlUtil.executeWithFrequencyControlList(strategy, frequencyControlDTOS, joinPoint::proceed);
        }

    }

    /**
     * 将注解参数转换为编程式调用所需要的参数
     *
     * @param key              频率控制eky
     * @param frequencyControl 注解
     * @return
     */
    private SlidingWindowDTO buildSlidingWindowDTO(String key, FrequencyControl frequencyControl) {
        SlidingWindowDTO slidingWindowDTO = new SlidingWindowDTO();
        slidingWindowDTO.setWindowSize(frequencyControl.windowSize());
        slidingWindowDTO.setPeriod(frequencyControl.period());
        slidingWindowDTO.setCount(frequencyControl.count());
        slidingWindowDTO.setUnit(frequencyControl.unit());
        slidingWindowDTO.setKey(key);
        return slidingWindowDTO;
    }

    private TokenBucketDTO buildTokenBucketDTO(String key, FrequencyControl frequencyControl) {
        TokenBucketDTO tokenBucketDTO = new TokenBucketDTO(
                frequencyControl.capacity(),
                frequencyControl.refillRate()
        );
        tokenBucketDTO.setKey(key);
        return tokenBucketDTO;
    }

    private FixedWindowDTO bulidFixedWindowDTO(String key, FrequencyControl frequencyControl) {
        FixedWindowDTO fixedWindowDTO = new FixedWindowDTO();
        fixedWindowDTO.setCount(frequencyControl.count());
        fixedWindowDTO.setTime(frequencyControl.time());
        fixedWindowDTO.setUnit(frequencyControl.unit());
        fixedWindowDTO.setKey(key);
        return fixedWindowDTO;
    }
}
