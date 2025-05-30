package com.meguru.chatproject.user.service.impl;

import cn.hutool.extra.mail.Mail;
import cn.hutool.extra.mail.MailAccount;
import com.meguru.chatproject.common.Properties.EmailProperties;
import com.meguru.chatproject.common.constant.RedisKey;
import com.meguru.chatproject.common.exception.BusinessErrorEnum;
import com.meguru.chatproject.common.exception.BusinessException;
import com.meguru.chatproject.common.utils.VerifyCodeUtil;
import com.meguru.chatproject.user.service.ICaptchaService;
import com.meguru.chatproject.utils.RedisUtils;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CaptchaServiceImpl implements ICaptchaService {

    private final EmailProperties emailProperties;
    private final TemplateEngine templateEngine;

    /**
     * 发送邮件验证码
     *
     * @param email 邮箱
     */
    @Override
    public void sendEmailCaptcha(String email) {
        // 验证邮件配置是否完整
        validateEmailProperties();

        // 验证邮箱格式
        if (!VerifyCodeUtil.checkEmail(email)) {
            throw new BusinessException(BusinessErrorEnum.EMAIL_CONFIGURATION_EXCEPTION);
        }

        // 生成或获取验证码
        String captcha = getCaptcha(email);

        // 生成邮件内容
        String content = generateEmailContent(captcha);

        // 发送邮件
        List<String> list = Collections.singletonList(email);
        sendEmail(list, content);
    }

    /**
     * 判断邮件配置是否完整
     */
    private void validateEmailProperties() {
        if (emailProperties.getUser() == null || emailProperties.getPassword() == null || emailProperties.getFrom() == null || emailProperties.getHost() == null || emailProperties.getPort() == null) {
            throw new BusinessException(BusinessErrorEnum.EMAIL_CONFIGURATION_EXCEPTION);
        }
    }

    /**
     * 获取验证码
     *
     * @param email 邮箱地址，用于生成和存储验证码。
     * @return {@link String} 返回生成的验证码。
     */
    private String getCaptcha(String email) {
        // 根据邮箱生成Redis键名
        String redisKey = RedisKey.getKey(RedisKey.CAPTCHA_CODE, email);
        // 尝试从Redis获取现有的验证码
        Object oldCode = RedisUtils.get(redisKey);
        if (oldCode == null) {
            // 如果验证码不存在，生成新的验证码
            String captcha = VerifyCodeUtil.generateVerifyCode();
            // 将新生成的验证码存储到Redis，并设置过期时间
            boolean saveResult = RedisUtils.set(redisKey, captcha, emailProperties.getExpireTime());
            if (!saveResult) {
                // 如果存储失败，抛出异常
                throw new BusinessException(BusinessErrorEnum.EMAIL_VERIFICATION_CODE_FAILED);
            }
            return captcha;
        } else {
            // 如果验证码存在，重置其在Redis中的过期时间
            boolean expireResult = RedisUtils.expire(redisKey, emailProperties.getExpireTime());
            if (!expireResult) {
                throw new BusinessException(BusinessErrorEnum.EMAIL_VERIFICATION_CODE_FAILED);
            }
            return String.valueOf(oldCode);
        }
    }


    /**
     * 生成邮件内容
     *
     * @param captcha 验证码
     * @return {@link String } 邮件内容
     */
    private String generateEmailContent(String captcha) {
        Context context = new Context();
        context.setVariable("verifyCode", Arrays.asList(captcha.split("")));
        return templateEngine.process("EmailVerificationCode.html", context);
    }

    /**
     * 发送邮件
     *
     * @param list
     * @param content 邮件内容
     */
    private void sendEmail(List<String> list, String content) {
        MailAccount account = createMailAccount();
        try {
            Mail.create(account)
                    .setTos(list.toArray(new String[0]))
                    .setTitle("ChatProject注册验证码")
                    .setContent(content)
                    .setHtml(true)
                    .setUseGlobalSession(false)
                    .send();
        } catch (Exception e) { // 捕获更广泛的异常
            //throw new BusinessException(BusinessErrorEnum.EMAIL_SENDING_EXCEPTION);
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * 创建邮件账户
     *
     * @return {@link MailAccount } 邮件账户
     */
    private MailAccount createMailAccount() {
        MailAccount account = new MailAccount();
        account.setAuth(true);
        account.setHost(emailProperties.getHost());
        account.setPort(emailProperties.getPort());
        account.setFrom(emailProperties.getFrom());
        account.setUser(emailProperties.getUser());
        account.setPass(emailProperties.getPassword());
        account.setSslEnable(true);
        account.setStarttlsEnable(true);
        return account;
    }
}


