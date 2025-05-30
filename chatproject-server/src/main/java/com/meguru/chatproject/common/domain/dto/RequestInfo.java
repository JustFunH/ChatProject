package com.meguru.chatproject.common.domain.dto;

import lombok.Data;

/**
 * Description: web请求信息收集类
 *
 * @author Meguru
 * @since 2025-05-28
 */
@Data
public class RequestInfo {
    private Long uid;
    private String ip;
}
