package com.meguru.chatproject.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 保证方法成功执行。如果在事务内的方法，会将操作记录入库，保证执行。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SecureInvoke {
    /**
     * 最大重试次数(包括第一次执行)
     *
     * @return
     */
    int maxRetryTimes() default 3;

    /**
     * 默认异步执行，先入库，后续异步执行，不影响主线程快速返回结果
     * 同步执行适合mq消费场景等对耗时不关心，但是希望链路追踪不被异步影响的场景。
     *
     * @return
     */
    boolean async() default true;
}
