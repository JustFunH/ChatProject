package com.meguru.chatproject.common.event.listener;

import com.meguru.chatproject.chat.dao.ContactDao;
import com.meguru.chatproject.chat.dao.MessageDao;
import com.meguru.chatproject.chat.dao.RoomDao;
import com.meguru.chatproject.chat.dao.RoomFriendDao;
import com.meguru.chatproject.chat.domain.entity.Message;
import com.meguru.chatproject.chat.domain.entity.Room;
import com.meguru.chatproject.chat.domain.enums.HotFlagEnum;
import com.meguru.chatproject.chat.service.ChatService;
import com.meguru.chatproject.chat.service.cache.GroupMemberCache;
import com.meguru.chatproject.chat.service.cache.HotRoomCache;
import com.meguru.chatproject.chat.service.cache.RoomCache;
import com.meguru.chatproject.common.constant.MQConstant;
import com.meguru.chatproject.common.domain.dto.MsgSendMessageDTO;
import com.meguru.chatproject.common.event.MessageSendEvent;
import com.meguru.chatproject.service.MQProducer;
import com.meguru.chatproject.user.service.cache.UserCache;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Objects;

/**
 * 消息发送监听器
 *
 * @author zhongzb create on 2022/08/26
 */
@Slf4j
@Component
public class MessageSendListener {

    @Autowired
    private ChatService chatService;
    @Autowired
    private MessageDao messageDao;

    @Autowired
    private RoomCache roomCache;
    @Autowired
    private RoomDao roomDao;
    @Autowired
    private GroupMemberCache groupMemberCache;
    @Autowired
    private UserCache userCache;
    @Autowired
    private RoomFriendDao roomFriendDao;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private ContactDao contactDao;
    @Autowired
    private HotRoomCache hotRoomCache;
    @Autowired
    private MQProducer mqProducer;


    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT, classes = MessageSendEvent.class, fallbackExecution = true)
    public void messageRoute(MessageSendEvent event) {
        Long msgId = event.getMsgId();
        mqProducer.sendSecureMsg(MQConstant.SEND_MSG_TOPIC, new MsgSendMessageDTO(msgId), msgId);
    }

    @TransactionalEventListener(classes = MessageSendEvent.class, fallbackExecution = true)
    public void handlerMsg(@NotNull MessageSendEvent event) {
        Message message = messageDao.getById(event.getMsgId());
        Room room = roomCache.get(message.getRoomId());
    }

    public boolean isHotRoom(Room room) {
        return Objects.equals(HotFlagEnum.YES.getType(), room.getHotFlag());
    }

    /**
     * 给用户微信推送艾特好友的消息通知
     * （这个没开启，微信不让推）
     */
    @TransactionalEventListener(classes = MessageSendEvent.class, fallbackExecution = true)
    public void publishChatToWechat(@NotNull MessageSendEvent event) {
        Message message = messageDao.getById(event.getMsgId());
        if (Objects.nonNull(message.getExtra().getAtUidList())) {
//            weChatMsgOperationService.publishChatMsgToWeChatUser(message.getFromUid(), message.getExtra().getAtUidList(),
//                    message.getContent());
        }
    }
}
