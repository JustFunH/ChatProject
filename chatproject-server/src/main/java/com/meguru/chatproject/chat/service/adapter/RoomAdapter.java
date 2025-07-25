package com.meguru.chatproject.chat.service.adapter;

import com.meguru.chatproject.chat.domain.entity.Contact;
import com.meguru.chatproject.chat.domain.entity.GroupMember;
import com.meguru.chatproject.chat.domain.entity.Room;
import com.meguru.chatproject.chat.domain.entity.RoomGroup;
import com.meguru.chatproject.chat.domain.enums.GroupRoleEnum;
import com.meguru.chatproject.chat.domain.enums.MessageTypeEnum;
import com.meguru.chatproject.chat.domain.vo.request.ChatMessageReq;
import com.meguru.chatproject.chat.domain.vo.response.ChatMessageReadResp;
import com.meguru.chatproject.chat.domain.vo.response.ChatRoomResp;
import com.meguru.chatproject.user.domain.entity.User;
import org.dromara.hutool.core.bean.BeanUtil;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description: 消息适配器
 *
 * @author Meguru
 * @since 2025-05-31
 */
public class RoomAdapter {


    public static List<ChatRoomResp> buildResp(List<Room> list) {
        return list.stream()
                .map(a -> {
                    ChatRoomResp resp = new ChatRoomResp();
                    BeanUtil.copyProperties(a, resp);
                    resp.setActiveTime(a.getActiveTime());
                    return resp;
                }).collect(Collectors.toList());
    }

    public static List<ChatMessageReadResp> buildReadResp(List<Contact> list) {
        return list.stream().map(contact -> {
            ChatMessageReadResp resp = new ChatMessageReadResp();
            resp.setUid(contact.getUid());
            return resp;
        }).collect(Collectors.toList());
    }

    public static List<GroupMember> buildGroupMemberBatch(List<Long> uidList, Long groupId) {
        return uidList.stream()
                .distinct()
                .map(uid -> {
                    GroupMember member = new GroupMember();
                    member.setRole(GroupRoleEnum.MEMBER.getType());
                    member.setUid(uid);
                    member.setGroupId(groupId);
                    return member;
                }).collect(Collectors.toList());
    }

    public static ChatMessageReq buildGroupAddMessage(RoomGroup groupRoom, User inviter, Map<Long, User> member) {
        ChatMessageReq chatMessageReq = new ChatMessageReq();
        chatMessageReq.setRoomId(groupRoom.getRoomId());
        chatMessageReq.setMsgType(MessageTypeEnum.SYSTEM.getType());
        StringBuilder sb = new StringBuilder();
        sb.append("\"")
                .append(inviter.getName())
                .append("\"")
                .append("邀请")
                .append(member.values().stream().map(u -> "\"" + u.getName() + "\"").collect(Collectors.joining(",")))
                .append("加入群聊");
        chatMessageReq.setBody(sb.toString());
        return chatMessageReq;
    }
}
