package com.meguru.frequencycontrol.service;

import com.meguru.frequencycontrol.domain.FrequencyControlDTO;
import com.meguru.frequencycontrol.exception.CommonErrorEnum;
import com.meguru.frequencycontrol.exception.FrequencyControlException;
import com.meguru.frequencycontrol.util.AsserUtil;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 抽象类频控服务
 *
 * @param <K>
 */
@Slf4j
public abstract class AbstractFrequencyControlService<K extends FrequencyControlDTO> {

    @PostConstruct
    protected void registerMyselfToFactory() {
        FrequencyControlStrategyFactory.registerFrequencyController(getStrategyName(), this);

    }

    /**
     * 频率控制执行方法
     *
     * @param frequencyControlMap
     * @param supplier
     * @return
     * @throws Throwable
     */
    private <T> T executeWithFrequencyControlMap(Map<String, K> frequencyControlMap, SupplierThrowWithoutParam<T> supplier) throws Throwable {
        if (reachRateLimit(frequencyControlMap)) {
            throw new FrequencyControlException(CommonErrorEnum.FREQUENCY_LIMIT);
        }
        try {
            return supplier.get();
        } finally {
            addFrequencyControlStatisticsCount(frequencyControlMap);
        }
    }

    /**
     * 多限流策略的条用方法-编程式调用
     *
     * @param frequencyControlList 频控列表
     * @param supplier             函数式入参-业务逻辑
     * @throws Throwable
     */
    @SuppressWarnings("unchecked")
    public <T> T executeWithFrequencyControlList(List<K> frequencyControlList, SupplierThrowWithoutParam<T> supplier) throws Throwable {
        boolean existsFrequencyControlHasNullKey = frequencyControlList.stream().anyMatch(frequencyControl -> ObjectUtils.isEmpty(frequencyControl.getKey()));
        AsserUtil.isFalse(existsFrequencyControlHasNullKey, CommonErrorEnum.FREQUENCY_CONTROL_KEY_NULL);
        Map<String, K> frequencyControlMap = frequencyControlList.stream().collect(Collectors.groupingBy(K::getKey, Collectors.collectingAndThen(Collectors.toList(), list -> list.get(0))));
        return executeWithFrequencyControlMap(frequencyControlMap, supplier);
    }

    /**
     * 单限流策略的条用方法-编程式调用
     *
     * @param frequencyControl 单个频控对象
     * @param supplier         服务提供者
     * @return
     */
    public <T> T executeWithFrequencyControl(K frequencyControl, SupplierThrowWithoutParam<T> supplier) throws Throwable {
        return executeWithFrequencyControlList(Collections.singletonList(frequencyControl), supplier);
    }

    @FunctionalInterface
    public interface SupplierThrowWithoutParam<T> {
        T get() throws Throwable;
    }

    @FunctionalInterface
    public interface Executor {
        void execute() throws Throwable;
    }

    /**
     * 是否达到限流阈值
     *
     * @param frequencyControlMap
     * @return true：达到限流阈值，false：未达到限流阈值
     */
    protected abstract boolean reachRateLimit(Map<String, K> frequencyControlMap);

    /**
     * 增加限流统计次数
     *
     * @param frequencyControlMap
     */
    protected abstract void addFrequencyControlStatisticsCount(Map<String, K> frequencyControlMap);

    /**
     * 获取策略名称
     *
     * @return
     */
    protected abstract String getStrategyName();
}
