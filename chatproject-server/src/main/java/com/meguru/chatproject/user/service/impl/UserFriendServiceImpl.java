package com.meguru.chatproject.user.service.impl;

import com.meguru.chatproject.common.domain.vo.request.CursorPageBaseReq;
import com.meguru.chatproject.common.domain.vo.request.PageBaseReq;
import com.meguru.chatproject.common.domain.vo.response.CursorPageBaseResp;
import com.meguru.chatproject.common.domain.vo.response.PageBaseResp;
import com.meguru.chatproject.common.event.UserApplyEvent;
import com.meguru.chatproject.common.utils.AssertUtil;
import com.meguru.chatproject.user.dao.UserApplyDao;
import com.meguru.chatproject.user.dao.UserDao;
import com.meguru.chatproject.user.dao.UserFriendDao;
import com.meguru.chatproject.user.domain.entity.UserApply;
import com.meguru.chatproject.user.domain.entity.UserFriend;
import com.meguru.chatproject.user.domain.vo.request.friend.FriendApplyReq;
import com.meguru.chatproject.user.domain.vo.request.friend.FriendApproveReq;
import com.meguru.chatproject.user.domain.vo.request.friend.FriendCheckReq;
import com.meguru.chatproject.user.domain.vo.response.friend.FriendApplyResp;
import com.meguru.chatproject.user.domain.vo.response.friend.FriendCheckResp;
import com.meguru.chatproject.user.domain.vo.response.friend.FriendResp;
import com.meguru.chatproject.user.domain.vo.response.friend.FriendUnreadResp;
import com.meguru.chatproject.user.service.IUserFriendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户联系人表 服务实现类
 * </p>
 *
 * @author Meguru
 * @since 2025-05-28
 */
@Slf4j
@Service
public class UserFriendServiceImpl implements IUserFriendService {
    @Autowired
    private UserFriendDao userFriendDao;
    @Autowired
    private UserApplyDao userApplyDao;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;


    /**
     * 检查是否是自己好友
     *
     * @param uid     uid
     * @param request 请求
     * @return
     */
    @Override
    public FriendCheckResp check(Long uid, FriendCheckReq request) {
        List<UserFriend> friendList = userFriendDao.getByFriends(uid, request.getUidList());
        Set<Long> friendUidSet = friendList.stream().map(UserFriend::getFriendUid).collect(Collectors.toSet());
        List<FriendCheckResp.FriendCheck> friendCheckList = request.getUidList().stream().map(friendUid -> {
            FriendCheckResp.FriendCheck friendCheck = new FriendCheckResp.FriendCheck();
            friendCheck.setUid(friendUid);
            friendCheck.setIsFriend(friendUidSet.contains(friendUid));
            return friendCheck;
        }).toList();
        return new FriendCheckResp(friendCheckList);
    }

    /**
     * 申请好友
     *
     * @param request 请求
     */
    @Override
    public void apply(Long uid, FriendApplyReq request) {
        //是否有好友关系
        UserFriend friend = userFriendDao.getByFriend(uid, request.getTargetUid());
        AssertUtil.isEmpty(friend, "你们已经是好友了");
        //是否有待审批的申请记录(自己的)
        UserApply selfApproving = userApplyDao.getFriendApproving(uid, request.getTargetUid());
        if (Objects.nonNull(selfApproving)) {
            log.info("已有好友申请记录,uid:{}, targetId:{}", uid, request.getTargetUid());
            return;
        }
        //是否有待审批的申请记录(别人请求自己的)
        UserApply friendApproving = userApplyDao.getFriendApproving(request.getTargetUid(), uid);
        if (Objects.nonNull(friendApproving)) {
            ((IUserFriendService) AopContext.currentProxy()).applyApprove(uid, new FriendApproveReq(friendApproving.getId()));
            return;
        }
        //申请入库
        UserApply insert = FriendAdapter.buildFriendApply(uid, request);
        userApplyDao.save(insert);
        //申请事件
        applicationEventPublisher.publishEvent(new UserApplyEvent(this, insert));
    }

    @Override
    public PageBaseResp<FriendApplyResp> pageApplyFriend(Long uid, PageBaseReq request) {
        return null;
    }

    @Override
    public FriendUnreadResp unread(Long uid) {
        return null;
    }

    @Override
    public void applyApprove(Long uid, FriendApproveReq request) {

    }

    @Override
    public void deleteFriend(Long uid, Long friendUid) {

    }

    @Override
    public CursorPageBaseResp<FriendResp> friendList(Long uid, CursorPageBaseReq request) {
        return null;
    }


}
