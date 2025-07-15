package com.meguru.chatproject.common.event.listener;

import com.meguru.chatproject.common.event.UserApplyEvent;
import com.meguru.chatproject.user.dao.UserApplyDao;
import com.meguru.chatproject.user.domain.entity.UserApply;
import com.meguru.chatproject.user.domain.vo.response.ws.WSFriendApply;
import com.meguru.chatproject.user.service.adapter.WSAdapter;
import com.meguru.chatproject.user.service.impl.PushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 好友申请监听器
 *
 * @author Meguru
 * @since 2025-05-28
 */
@Slf4j
@Component
public class UserApplyListener {
    @Autowired
    private UserApplyDao userApplyDao;

    @Autowired
    private PushService pushService;

    @Async
    @TransactionalEventListener(classes = UserApplyEvent.class, fallbackExecution = true)
    public void notifyFriend(UserApplyEvent event) {
        UserApply userApply = event.getUserApply();
        Integer unReadCount = userApplyDao.getUnReadCount(userApply.getTargetId()).intValue();
        pushService.sendPushMsg(WSAdapter.buildApplySend(new WSFriendApply(userApply.getUid(), unReadCount)), userApply.getTargetId());
    }

}
