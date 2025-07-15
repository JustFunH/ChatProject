package com.meguru.chatproject.user.service.adapter;

import com.meguru.chatproject.user.domain.entity.User;
import com.meguru.chatproject.user.domain.vo.response.user.LoginSuccess;
import com.meguru.chatproject.user.domain.vo.response.user.UserInfoResp;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.bean.BeanUtil;

/**
 * Description: 用户适配器
 * @author Meguru
 * @since 2025-05-30
 */
@Slf4j
public class UserAdapter {

    public static UserInfoResp buildUserInfoResp(User userInfo) {
        UserInfoResp userInfoResp = new UserInfoResp();
        BeanUtil.copyProperties(userInfo, userInfoResp);
        return userInfoResp;
    }

    public static LoginSuccess buildLoginSuccessResp(User userInfo, String token) {
        LoginSuccess userInfoResp = new LoginSuccess();
        BeanUtil.copyProperties(userInfo, userInfoResp);
        userInfoResp.setToken(token);
        return userInfoResp;
    }

}
