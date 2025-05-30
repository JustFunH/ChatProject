package com.meguru.chatproject.user.service.impl;

import cn.hutool.core.util.StrUtil;
import com.meguru.chatproject.common.constant.RedisKey;
import com.meguru.chatproject.common.utils.JwtUtils;
import com.meguru.chatproject.user.service.ILoginService;
import com.meguru.chatproject.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Description: 登录相关处理类
 *
 * @author Meguru
 * @since 2025-05-29
 */
@Service
@Slf4j
public class LoginServiceImpl implements ILoginService {
    @Autowired
    private JwtUtils jwtUtils;
    // token 过期时间
    private static final Integer TOKEN_EXPIRE_TIME = 5;
    // token 续期时间
    private static final Integer TOKEN_RENEWAL_DAYS = 2;

    /**
     * 检验 token 是否有效
     *
     * @param token
     * @return
     */
    @Override
    public boolean verify(String token) {
        Long uid = jwtUtils.getUidOrNull(token);
        if (Objects.isNull(uid)) {
            return false;
        }
        String key = RedisKey.getKey(RedisKey.USER_TOKEN_STRING, uid);
        String realToken = RedisUtils.getStr(key);
        return Objects.equals(token, realToken);
    }

    /**
     * 刷新token有效期
     *
     * @param token
     */
    @Async
    @Override
    public void renewalTokenIfNecessary(String token) {
        Long uid = jwtUtils.getUidOrNull(token);
        if (Objects.isNull(uid)) {
            return;
        }
        String key = RedisKey.getKey(RedisKey.USER_TOKEN_STRING, uid);
        String realToken = RedisUtils.getStr(key);
        if (Objects.equals(token, realToken)) {
            RedisUtils.expire(key, TOKEN_RENEWAL_DAYS);
        }
    }

    @Override
    public String login(Long uid) {
        String key = RedisKey.getKey(RedisKey.USER_TOKEN_STRING, uid);
        String token = RedisUtils.getStr(key);
        if (StrUtil.isNotBlank(token)) {
            return token;
        }
        token = jwtUtils.createToken(uid);
        RedisUtils.set(key, token, TOKEN_EXPIRE_TIME, TimeUnit.DAYS);
        return token;
    }

    @Override
    public Long getValidUid(String token) {
        return verify(token) ? jwtUtils.getUidOrNull(token) : null;
    }

}
