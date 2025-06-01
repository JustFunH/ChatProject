package com.meguru.chatproject.chat.service;

import com.meguru.chatproject.chat.domain.vo.request.ChatMessageMemberReq;
import com.meguru.chatproject.chat.domain.vo.request.GroupAddReq;
import com.meguru.chatproject.chat.domain.vo.request.member.MemberAddReq;
import com.meguru.chatproject.chat.domain.vo.request.member.MemberDelReq;
import com.meguru.chatproject.chat.domain.vo.request.member.MemberReq;
import com.meguru.chatproject.chat.domain.vo.response.ChatMemberListResp;
import com.meguru.chatproject.chat.domain.vo.response.ChatRoomResp;
import com.meguru.chatproject.chat.domain.vo.response.MemberResp;
import com.meguru.chatproject.common.domain.vo.request.CursorPageBaseReq;
import com.meguru.chatproject.common.domain.vo.response.CursorPageBaseResp;
import com.meguru.chatproject.user.domain.vo.response.ws.ChatMemberResp;

import java.util.List;

/**
 * Description:
 *
 * @author Meguru
 * @since 2025-05-31
 */
public interface RoomAppService {
    /**
     * 获取会话列表--支持未登录态
     */
    CursorPageBaseResp<ChatRoomResp> getContactPage(CursorPageBaseReq request, Long uid);

    /**
     * 获取群组信息
     */
    MemberResp getGroupDetail(Long uid, long roomId);

    CursorPageBaseResp<ChatMemberResp> getMemberPage(MemberReq request);

    List<ChatMemberListResp> getMemberList(ChatMessageMemberReq request);

    void delMember(Long uid, MemberDelReq request);

    void addMember(Long uid, MemberAddReq request);

    Long addGroup(Long uid, GroupAddReq request);

    ChatRoomResp getContactDetail(Long uid, Long roomId);

    ChatRoomResp getContactDetailByFriend(Long uid, Long friendUid);
}
