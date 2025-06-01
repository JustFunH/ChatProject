package com.meguru.chatproject.user.service;

public interface IIpService {
    /**
     * 异步更新用户ip详情
     *
     * @param uid
     */
    void refreshIpDetailAsync(Long uid);
}
