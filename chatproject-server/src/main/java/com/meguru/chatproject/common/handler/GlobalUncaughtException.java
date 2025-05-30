package com.meguru.chatproject.common.handler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GlobalUncaughtException implements Thread.UncaughtExceptionHandler {
    private static final GlobalUncaughtException instance = new GlobalUncaughtException();

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.error("Exception in thread {} ", t.getName(), e);
    }

    public static GlobalUncaughtException getInstance() {
        return instance;
    }

    private GlobalUncaughtException() {
    }
}
