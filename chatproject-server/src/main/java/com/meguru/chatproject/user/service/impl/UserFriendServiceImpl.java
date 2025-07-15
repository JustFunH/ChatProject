package com.meguru.chatproject.user.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import com.meguru.chatproject.chat.domain.entity.RoomFriend;
import com.meguru.chatproject.chat.service.ChatService;
import com.meguru.chatproject.chat.service.RoomService;
import com.meguru.chatproject.chat.service.adapter.MessageAdapter;
import com.meguru.chatproject.common.annotation.RedissonLock;
import com.meguru.chatproject.common.domain.vo.request.CursorPageBaseReq;
import com.meguru.chatproject.common.domain.vo.request.PageBaseReq;
import com.meguru.chatproject.common.domain.vo.response.CursorPageBaseResp;
import com.meguru.chatproject.common.domain.vo.response.PageBaseResp;
import com.meguru.chatproject.common.event.UserApplyEvent;
import com.meguru.chatproject.common.exception.BusinessErrorEnum;
import com.meguru.chatproject.common.utils.AssertUtil;
import com.meguru.chatproject.user.dao.UserApplyDao;
import com.meguru.chatproject.user.dao.UserDao;
import com.meguru.chatproject.user.dao.UserFriendDao;
import com.meguru.chatproject.user.domain.entity.User;
import com.meguru.chatproject.user.domain.entity.UserApply;
import com.meguru.chatproject.user.domain.entity.UserFriend;
import com.meguru.chatproject.user.domain.enums.ApplyStatusEnum;
import com.meguru.chatproject.user.domain.vo.request.friend.FriendApplyReq;
import com.meguru.chatproject.user.domain.vo.request.friend.FriendApproveReq;
import com.meguru.chatproject.user.domain.vo.request.friend.FriendCheckReq;
import com.meguru.chatproject.user.domain.vo.response.friend.FriendApplyResp;
import com.meguru.chatproject.user.domain.vo.response.friend.FriendCheckResp;
import com.meguru.chatproject.user.domain.vo.response.friend.FriendResp;
import com.meguru.chatproject.user.domain.vo.response.friend.FriendUnreadResp;
import com.meguru.chatproject.user.service.IUserFriendService;
import com.meguru.chatproject.user.service.adapter.FriendAdapter;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.collection.CollUtil;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
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
    @Autowired
    private RoomService roomService;
    @Autowired
    private ChatService chatService;
    @Autowired
    private UserDao userDao;

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
        AssertUtil.isEmpty(friend, BusinessErrorEnum.FRIEND_HAS_EXIST);
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

    /**
     * 分页查询好友申请
     *
     * @param request 请求
     * @return {@link PageBaseResp}<{@link FriendApplyResp}>
     */
    @Override
    public PageBaseResp<FriendApplyResp> pageApplyFriend(Long uid, PageBaseReq request) {
        IPage<UserApply> userApplyIPage = userApplyDao.friendApplyPage(uid, request.plusPage());
        if (CollUtil.isEmpty(userApplyIPage.getRecords())) {
            return PageBaseResp.empty();
        }
        //将这些申请列表设为已读
        readApples(uid, userApplyIPage);
        //返回消息
        return PageBaseResp.init(userApplyIPage, FriendAdapter.buildFriendApplyList(userApplyIPage.getRecords()));
    }

    private void readApples(Long uid, IPage<UserApply> userApplyIPage) {
        List<Long> applyIds = userApplyIPage.getRecords()
                .stream().map(UserApply::getId)
                .collect(Collectors.toList());
        userApplyDao.readApples(uid, applyIds);
    }

    /**
     * 申请未读数
     *
     * @return {@link FriendUnreadResp}
     */
    @Override
    public FriendUnreadResp unread(Long uid) {
        Integer unReadCount = userApplyDao.getUnReadCount(uid).intValue();
        return new FriendUnreadResp(unReadCount);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @RedissonLock(key = "#uid")
    public void applyApprove(Long uid, FriendApproveReq request) {
        UserApply userApply = userApplyDao.getById(request.getApplyId());
        AssertUtil.isNotEmpty(userApply, BusinessErrorEnum.FRIEND_RECORD_NOT_EXIST);
        AssertUtil.equal(userApply.getTargetId(), uid, BusinessErrorEnum.FRIEND_RECORD_NOT_EXIST.getErrorMsg());
        AssertUtil.equal(userApply.getStatus(), ApplyStatusEnum.WAIT_APPROVAL.getCode(), "已同意好友申请");
        //同意申请
        userApplyDao.agree(request.getApplyId());
        //创建双方好友关系
        createFriend(uid, userApply.getUid());
        //创建一个聊天房间
        RoomFriend roomFriend = roomService.createFriendRoom(Arrays.asList(uid, userApply.getUid()));
        chatService.sendMsg(MessageAdapter.buildAgreeMsg(roomFriend.getRoomId()), uid);
    }


    /**
     * 删除好友
     *
     * @param uid       uid
     * @param friendUid 朋友uid
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFriend(Long uid, Long friendUid) {
        List<UserFriend> userFriends = userFriendDao.getUserFriend(uid, friendUid);
        if (CollUtil.isEmpty(userFriends)) {
            log.info("没有好友关系：{},{}", uid, friendUid);
            return;
        }
        List<Long> friendRecordIds = userFriends.stream().map(UserFriend::getId).collect(Collectors.toList());
        userFriendDao.removeByIds(friendRecordIds);
        //禁用房间
        roomService.disableFriendRoom(Arrays.asList(uid, friendUid));
    }

    @Override
    public CursorPageBaseResp<FriendResp> friendList(Long uid, CursorPageBaseReq request) {
        CursorPageBaseResp<UserFriend> friendPage = userFriendDao.getFriendPage(uid, request);
        if (CollectionUtils.isEmpty(friendPage.getList())) {
            return CursorPageBaseResp.empty();
        }
        List<Long> friendUids = friendPage.getList()
                .stream().map(UserFriend::getFriendUid)
                .collect(Collectors.toList());
        List<User> userList = userDao.getFriendList(friendUids);
        return CursorPageBaseResp.init(friendPage, FriendAdapter.buildFriend(friendPage.getList(), userList));
    }

    private void createFriend(Long uid, Long targetUid) {
        UserFriend userFriend1 = new UserFriend();
        userFriend1.setUid(uid);
        userFriend1.setFriendUid(targetUid);
        UserFriend userFriend2 = new UserFriend();
        userFriend2.setUid(targetUid);
        userFriend2.setFriendUid(uid);
        userFriendDao.saveBatch(Lists.newArrayList(userFriend1, userFriend2));
    }
}
