package com.meguru.chatproject.user.service.impl;

import com.meguru.chatproject.common.handler.GlobalUncaughtExceptionHandler;
import com.meguru.chatproject.user.dao.UserDao;
import com.meguru.chatproject.user.domain.dto.IpResult;
import com.meguru.chatproject.user.domain.entity.IpDetail;
import com.meguru.chatproject.user.domain.entity.IpInfo;
import com.meguru.chatproject.user.domain.entity.User;
import com.meguru.chatproject.user.service.IIpService;
import com.meguru.chatproject.user.service.cache.UserCache;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.reflect.TypeReference;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.thread.NamedThreadFactory;
import org.dromara.hutool.http.HttpUtil;
import org.dromara.hutool.json.JSONUtil;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Description: ip
 *
 * @author Meguru
 * @since 2025-05-30
 */
@Service
@Slf4j
public class IpServiceImpl implements IIpService, DisposableBean {
    private static final ExecutorService EXECUTOR = new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(500),
            new NamedThreadFactory("refresh-ipDetail", null, false,
                    GlobalUncaughtExceptionHandler.getInstance()));

    @Autowired
    private UserDao userDao;
    @Autowired
    private UserCache userCache;

    private static IpDetail TryGetIpDetailOrNullTreeTimes(String ip) {
        for (int i = 0; i < 3; i++) {
            IpDetail ipDetail = getIpDetailOrNull(ip);
            if (Objects.nonNull(ipDetail)) {
                return ipDetail;
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static IpDetail getIpDetailOrNull(String ip) {
        String body = HttpUtil.get("https://ip.taobao.com/outGetIpInfo?ip=" + ip + "&accessKey=alibaba-inc");
        try {
            IpResult<IpDetail> result = JSONUtil.parse(body).toBean(
                    new TypeReference<IpResult<IpDetail>>() {
                    });
            if (result.isSuccess()) {
                return result.getData();
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    @Override
    public void refreshIpDetailAsync(Long uid) {
        EXECUTOR.execute(() -> {
            User user = userDao.getById(uid);
            IpInfo ipInfo = user.getIpInfo();
            if (Objects.isNull(ipInfo)) {
                return;
            }
            String ip = ipInfo.needRefreshIp();
            if (StrUtil.isBlank(ip)) {
                return;
            }
            IpDetail ipDetail = TryGetIpDetailOrNullTreeTimes(ip);
            if (Objects.nonNull(ipDetail)) {
                ipInfo.refreshIpDetail(ipDetail);
                User update = new User();
                update.setId(uid);
                update.setIpInfo(ipInfo);
                userDao.updateById(update);
                userCache.userInfoChange(uid);
            } else {
                log.error("get ip detail fail ip:{},uid:{}", ip, uid);
            }
        });
    }

    @Override
    public void destroy() throws InterruptedException {
        EXECUTOR.shutdown();
        if (!EXECUTOR.awaitTermination(30, TimeUnit.SECONDS)) {
            if (log.isErrorEnabled()) {
                log.error("Timed out while waiting for executor [{}] to terminate", EXECUTOR);
            }
        }
    }

}
