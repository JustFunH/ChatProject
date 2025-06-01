package com.meguru.chatproject.common.event.listener;

import com.meguru.chatproject.common.event.UserOnlineEvent;
import com.meguru.chatproject.user.dao.UserDao;
import com.meguru.chatproject.user.domain.entity.User;
import com.meguru.chatproject.user.domain.enums.ChatActiveStatusEnum;
import com.meguru.chatproject.user.service.IIpService;
import com.meguru.chatproject.user.service.adapter.WSAdapter;
import com.meguru.chatproject.user.service.cache.UserCache;
import com.meguru.chatproject.user.service.impl.PushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 用户上线监听器
 *
 * @author Meguru
 * @since 2025-05-28
 */
@Slf4j
@Component
public class UserOnlineListener {
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserCache userCache;
    @Autowired
    private WSAdapter wsAdapter;
    @Autowired
    private IIpService ipService;
    @Autowired
    private PushService pushService;

    @Async
    @EventListener(classes = UserOnlineEvent.class)
    public void saveRedisAndPush(UserOnlineEvent event) {
        User user = event.getUser();
        userCache.online(user.getId(), user.getLastOptTime());
        //推送给所有在线用户，该用户登录成功
        pushService.sendPushMsg(wsAdapter.buildOnlineNotifyResp(event.getUser()));
    }

    @Async
    @EventListener(classes = UserOnlineEvent.class)
    public void saveDB(UserOnlineEvent event) {
        User user = event.getUser();
        User update = new User();
        update.setId(user.getId());
        update.setLastOptTime(user.getLastOptTime());
        update.setIpInfo(user.getIpInfo());
        update.setActiveStatus(ChatActiveStatusEnum.ONLINE.getStatus());
        userDao.updateById(update);
        //更新用户ip详情
        ipService.refreshIpDetailAsync(user.getId());
    }

}
