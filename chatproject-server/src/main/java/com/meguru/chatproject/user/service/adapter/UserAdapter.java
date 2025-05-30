package com.meguru.chatproject.user.service.adapter;

import cn.hutool.core.bean.BeanUtil;
import com.meguru.chatproject.user.domain.entity.User;
import com.meguru.chatproject.user.domain.vo.response.user.UserInfoResp;
import lombok.extern.slf4j.Slf4j;

/**
 * Description: 用户适配器
 *
 * @author Meguru
 * @since 2025-05-28
 */
@Slf4j
public class UserAdapter {

    public static UserInfoResp buildUserInfoResp(User user) {
        UserInfoResp userInfoResp = new UserInfoResp();
        BeanUtil.copyProperties(user, userInfoResp);
        return userInfoResp;
    }
}
