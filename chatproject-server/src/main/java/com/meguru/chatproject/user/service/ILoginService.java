package com.meguru.chatproject.user.service;

import com.meguru.chatproject.user.domain.vo.request.user.LoginReq;
import com.meguru.chatproject.user.domain.vo.request.user.RegisterReq;
import com.meguru.chatproject.user.domain.vo.response.user.LoginSuccess;

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
     * @param
     * @return 返回token
     */
    LoginSuccess login(LoginReq req);

    /**
     * 如果token有效，返回uid
     *
     * @param token
     * @return
     */
    Long getValidUid(String token);

    /**
     * 注册用户
     *
     * @param
     * @return
     */
    LoginSuccess register(RegisterReq req);
}
