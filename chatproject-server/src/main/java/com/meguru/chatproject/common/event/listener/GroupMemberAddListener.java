package com.meguru.chatproject.common.event.listener;

import com.meguru.chatproject.chat.domain.entity.GroupMember;
import com.meguru.chatproject.chat.domain.entity.RoomGroup;
import com.meguru.chatproject.chat.domain.vo.request.ChatMessageReq;
import com.meguru.chatproject.chat.service.ChatService;
import com.meguru.chatproject.chat.service.adapter.MemberAdapter;
import com.meguru.chatproject.chat.service.adapter.RoomAdapter;
import com.meguru.chatproject.chat.service.cache.GroupMemberCache;
import com.meguru.chatproject.common.event.GroupMemberAddEvent;
import com.meguru.chatproject.user.dao.UserDao;
import com.meguru.chatproject.user.domain.entity.User;
import com.meguru.chatproject.user.domain.enums.WSBaseResp;
import com.meguru.chatproject.user.domain.vo.response.ws.WSMemberChange;
import com.meguru.chatproject.user.service.cache.UserInfoCache;
import com.meguru.chatproject.user.service.impl.PushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 添加群成员监听器
 *
 * @author Meguru
 * @since 2025-05-28
 */
@Slf4j
@Component
public class GroupMemberAddListener {
    @Autowired
    private ChatService chatService;
    @Autowired
    private UserInfoCache userInfoCache;
    @Autowired
    private UserDao userDao;
    @Autowired
    private GroupMemberCache groupMemberCache;
    @Autowired
    private PushService pushService;


    @Async
    @TransactionalEventListener(classes = GroupMemberAddEvent.class, fallbackExecution = true)
    public void sendAddMsg(GroupMemberAddEvent event) {
        List<GroupMember> memberList = event.getMemberList();
        RoomGroup roomGroup = event.getRoomGroup();
        Long inviteUid = event.getInviteUid();
        User user = userInfoCache.get(inviteUid);
        List<Long> uidList = memberList.stream().map(GroupMember::getUid).collect(Collectors.toList());
        ChatMessageReq chatMessageReq = RoomAdapter.buildGroupAddMessage(roomGroup, user, userInfoCache.getBatch(uidList));
        chatService.sendMsg(chatMessageReq, User.UID_SYSTEM);
    }

    @Async
    @TransactionalEventListener(classes = GroupMemberAddEvent.class, fallbackExecution = true)
    public void sendChangePush(GroupMemberAddEvent event) {
        List<GroupMember> memberList = event.getMemberList();
        RoomGroup roomGroup = event.getRoomGroup();
        List<Long> memberUidList = groupMemberCache.getMemberUidList(roomGroup.getRoomId());
        List<Long> uidList = memberList.stream().map(GroupMember::getUid).collect(Collectors.toList());
        List<User> users = userDao.listByIds(uidList);
        users.forEach(user -> {
            WSBaseResp<WSMemberChange> ws = MemberAdapter.buildMemberAddWS(roomGroup.getRoomId(), user);
            pushService.sendPushMsg(ws, memberUidList);
        });
        //移除缓存
        groupMemberCache.evictMemberUidList(roomGroup.getRoomId());
    }

}
