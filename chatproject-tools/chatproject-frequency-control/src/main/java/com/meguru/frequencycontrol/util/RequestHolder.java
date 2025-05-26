package com.meguru.frequencycontrol.util;

import com.meguru.frequencycontrol.domain.RequestInfo;

/**
 * 请求上下文
 */
public class RequestHolder {
    private static ThreadLocal<RequestInfo> threadLocal = new ThreadLocal<>();

    public static void set(RequestInfo requestInfo) {
        threadLocal.set(requestInfo);
    }

    public static RequestInfo get() {
        return threadLocal.get();
    }

    public static void remove() {
        threadLocal.remove();
    }
}
