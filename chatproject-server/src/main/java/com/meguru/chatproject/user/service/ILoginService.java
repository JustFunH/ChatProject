package com.meguru.chatproject.user.service;

/**
 * Description: 登录相关处理类
 *
 * @author Meguru
 * @since 2025-05-29
 */
public interface ILoginService {

    /**
     * 校验token是否有效
     *
     * @param token
     * @return
     */
    boolean verify(String token);

    /**
     * 刷新token有效期
     *
     * @param token
     */
    void renewalTokenIfNecessary(String token);

    /**
     * 登录成功，获取token
     *
     * @param uid
     * @return 返回token
     */
    String login(Long uid);

    /**
     * 如果token有效，返回uid
     *
     * @param token
     * @return
     */
    Long getValidUid(String token);

}
