package com.meguru.chatproject.common.factory;

import com.meguru.chatproject.common.handler.GlobalUncaughtException;
import lombok.AllArgsConstructor;

import java.util.concurrent.ThreadFactory;

@AllArgsConstructor
public class MyThreadFactory implements ThreadFactory {
    private final ThreadFactory factory;

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = factory.newThread(r);
        thread.setUncaughtExceptionHandler(GlobalUncaughtException.getInstance());
        return thread;
    }
}
