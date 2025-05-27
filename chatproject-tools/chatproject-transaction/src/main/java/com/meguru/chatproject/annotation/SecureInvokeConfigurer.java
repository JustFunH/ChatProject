package com.meguru.chatproject.annotation;

import io.micrometer.core.lang.Nullable;

import java.util.concurrent.Executor;

public interface SecureInvokeConfigurer {
    /**
     * 返回一个线程池
     *
     * @return
     */
    @Nullable
    default Executor getSecureInvokeExecutor() {
        return null;
    }
}
