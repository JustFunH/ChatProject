package com.meguru.chatproject.common.intecepter;

import com.meguru.chatproject.common.domain.dto.RequestInfo;
import com.meguru.chatproject.common.exception.HttpErrorEnum;
import com.meguru.chatproject.common.utils.RequestHolder;
import com.meguru.chatproject.user.domain.enums.BlackTypeEnum;
import com.meguru.chatproject.user.service.cache.UserCache;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.collection.CollUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 黑名单拦截
 */
@Order(2)
@Slf4j
@Component
public class BlackInterceptor implements HandlerInterceptor {

    @Autowired
    private UserCache userCache;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<Integer, Set<String>> blackMap = userCache.getBlackMap();
        RequestInfo requestInfo = RequestHolder.get();
        if (inBlackList(requestInfo.getUid(), blackMap.get(BlackTypeEnum.UID.getType()))) {
            HttpErrorEnum.ACCESS_DENIED.sendHttpError(response);
            return false;
        }
        if (inBlackList(requestInfo.getIp(), blackMap.get(BlackTypeEnum.IP.getType()))) {
            HttpErrorEnum.ACCESS_DENIED.sendHttpError(response);
            return false;
        }
        return true;
    }

    private boolean inBlackList(Object target, Set<String> blackSet) {
        if (Objects.isNull(target) || CollUtil.isEmpty(blackSet)) {
            return false;
        }
        return blackSet.contains(target.toString());
    }

}