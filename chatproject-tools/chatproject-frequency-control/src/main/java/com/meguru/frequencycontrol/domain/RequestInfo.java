package com.meguru.frequencycontrol.domain;

import lombok.Data;

/**
 * web请求信息收集类
 */
@Data
public class RequestInfo {
    private Long uid;
    private String ip;
}
