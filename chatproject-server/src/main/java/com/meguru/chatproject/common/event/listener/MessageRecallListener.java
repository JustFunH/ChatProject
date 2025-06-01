package com.meguru.chatproject.common.event.listener;

import com.meguru.chatproject.chat.domain.dto.ChatMsgRecallDTO;
import com.meguru.chatproject.chat.service.cache.MsgCache;
import com.meguru.chatproject.common.event.MessageRecallEvent;
import com.meguru.chatproject.user.service.adapter.WSAdapter;
import com.meguru.chatproject.user.service.impl.PushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 消息撤回监听器
 *
 * @author Meguru
 * @since 2025-05-28
 */
@Slf4j
@Component
public class MessageRecallListener {

    @Autowired
    private MsgCache msgCache;
    @Autowired
    private PushService pushService;

    @Async
    @TransactionalEventListener(classes = MessageRecallEvent.class, fallbackExecution = true)
    public void evictMsg(MessageRecallEvent event) {
        ChatMsgRecallDTO recallDTO = event.getRecallDTO();
        msgCache.evictMsg(recallDTO.getMsgId());
    }

    @Async
    @TransactionalEventListener(classes = MessageRecallEvent.class, fallbackExecution = true)
    public void sendToAll(MessageRecallEvent event) {
        pushService.sendPushMsg(WSAdapter.buildMsgRecall(event.getRecallDTO()));
    }

}
