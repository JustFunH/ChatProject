package com.meguru.chatproject.user.service.impl;

import cn.hutool.core.util.StrUtil;
import com.meguru.chatproject.common.constant.RedisKey;
import com.meguru.chatproject.common.exception.BusinessErrorEnum;
import com.meguru.chatproject.common.utils.AssertUtil;
import com.meguru.chatproject.common.utils.JwtUtils;
import com.meguru.chatproject.common.utils.PasswordEncoderUtil;
import com.meguru.chatproject.user.dao.UserDao;
import com.meguru.chatproject.user.domain.entity.User;
import com.meguru.chatproject.user.domain.vo.request.user.LoginReq;
import com.meguru.chatproject.user.domain.vo.request.user.RegisterReq;
import com.meguru.chatproject.user.domain.vo.response.user.LoginSuccess;
import com.meguru.chatproject.user.service.ILoginService;
import com.meguru.chatproject.user.service.adapter.UserAdapter;
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
    @Autowired
    private UserDao userDao;


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
    public LoginSuccess login(LoginReq req) {
        User user = userDao.getByUsername(req.getUsername());
        AssertUtil.isNotEmpty(user, BusinessErrorEnum.LOGIN_ERROR);
        AssertUtil.isTrue(
                PasswordEncoderUtil.matches(req.getPassword(), user.getPassword()),
                BusinessErrorEnum.LOGIN_ERROR
        );
        String key = RedisKey.getKey(RedisKey.USER_TOKEN_STRING, user.getId());
        String token = RedisUtils.getStr(key);

        if (StrUtil.isBlank(token)) {
            token = jwtUtils.createToken(user.getId());
            RedisUtils.set(key, token, TOKEN_EXPIRE_TIME, TimeUnit.DAYS);
        }

        return UserAdapter.buildLoginSuccessResp(user, token);
    }


    @Override
    public Long getValidUid(String token) {
        return verify(token) ? jwtUtils.getUidOrNull(token) : null;
    }

    @Override
    public LoginSuccess register(RegisterReq req) {
        boolean isNull = StrUtil.isBlank(req.getUsername()) || StrUtil.isBlank(req.getPassword());
        AssertUtil.isTrue(!isNull, BusinessErrorEnum.USERNAME_IS_NULL);
        boolean equal = req.getPassword().equals(req.getConfirmPassword());
        AssertUtil.isTrue(equal, BusinessErrorEnum.PASSWORD_NOT_EQUAL);

        AssertUtil.isEmpty(userDao.getByUsername(req.getUsername()), BusinessErrorEnum.USERNAME_HAS_EXIST);
        String codeKey = RedisKey.getKey(RedisKey.CAPTCHA_CODE, req.getEmail());
        AssertUtil.isTrue(RedisUtils.getStr(codeKey).equals(req.getVerificationCode()), BusinessErrorEnum.VERIFICATION_CODE_ERROR);

        String encryptedPwd = PasswordEncoderUtil.encode(req.getPassword());

        User user = User.builder()
                .username(req.getUsername())
                .password(encryptedPwd)
                .email(req.getEmail())
                .build();

        userDao.save(user);

        String token = jwtUtils.createToken(user.getId());
        String redisKey = RedisKey.getKey(RedisKey.USER_TOKEN_STRING, user.getId());
        RedisUtils.set(redisKey, token, TOKEN_EXPIRE_TIME, TimeUnit.DAYS);

        return LoginSuccess.builder()
                .token(token)
                .id(user.getId())
                .name(user.getName())
                .build();
    }

}
