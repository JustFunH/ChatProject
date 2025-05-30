package com.meguru.chatproject.user.service;

import com.meguru.chatproject.common.domain.vo.request.CursorPageBaseReq;
import com.meguru.chatproject.common.domain.vo.request.PageBaseReq;
import com.meguru.chatproject.common.domain.vo.response.CursorPageBaseResp;
import com.meguru.chatproject.common.domain.vo.response.PageBaseResp;
import com.meguru.chatproject.user.domain.vo.request.friend.FriendApplyReq;
import com.meguru.chatproject.user.domain.vo.request.friend.FriendApproveReq;
import com.meguru.chatproject.user.domain.vo.request.friend.FriendCheckReq;
import com.meguru.chatproject.user.domain.vo.response.friend.FriendApplyResp;
import com.meguru.chatproject.user.domain.vo.response.friend.FriendCheckResp;
import com.meguru.chatproject.user.domain.vo.response.friend.FriendResp;
import com.meguru.chatproject.user.domain.vo.response.friend.FriendUnreadResp;

/**
 * <p>
 * 用户联系人表 服务类
 * </p>
 *
 * @author Meguru
 * @since 2025-05-28
 */
public interface IUserFriendService {
    /**
     * 检查
     * 检查是否是自己好友
     *
     * @param request 请求
     * @param uid     uid
     * @return {@link FriendCheckResp}
     */
    FriendCheckResp check(Long uid, FriendCheckReq request);

    /**
     * 应用
     * 申请好友
     *
     * @param request 请求
     * @param uid     uid
     */
    void apply(Long uid, FriendApplyReq request);

    /**
     * 分页查询好友申请
     *
     * @param request 请求
     * @return {@link PageBaseResp}<{@link FriendApplyResp}>
     */
    PageBaseResp<FriendApplyResp> pageApplyFriend(Long uid, PageBaseReq request);

    /**
     * 申请未读数
     *
     * @return {@link FriendUnreadResp}
     */
    FriendUnreadResp unread(Long uid);

    /**
     * 同意好友申请
     *
     * @param uid     uid
     * @param request 请求
     */
    void applyApprove(Long uid, FriendApproveReq request);

    /**
     * 删除好友
     *
     * @param uid       uid
     * @param friendUid 朋友uid
     */
    void deleteFriend(Long uid, Long friendUid);

    CursorPageBaseResp<FriendResp> friendList(Long uid, CursorPageBaseReq request);

}
