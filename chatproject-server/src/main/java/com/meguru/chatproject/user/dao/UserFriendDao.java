package com.meguru.chatproject.user.dao;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meguru.chatproject.common.domain.vo.request.CursorPageBaseReq;
import com.meguru.chatproject.common.domain.vo.response.CursorPageBaseResp;
import com.meguru.chatproject.common.utils.CursorUtils;
import com.meguru.chatproject.user.domain.entity.UserFriend;
import com.meguru.chatproject.user.mapper.UserFriendMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户联系人表 服务实现类
 * </p>
 *
 * @author Meguru
 * @since 2025-05-29
 */
@Service
public class UserFriendDao extends ServiceImpl<UserFriendMapper, UserFriend> {
    public List<UserFriend> getByFriends(Long uid, List<Long> uidList) {
        return lambdaQuery()
                .eq(UserFriend::getUid, uid)
                .in(UserFriend::getFriendUid, uidList)
                .list();
    }

    public UserFriend getByFriend(Long uid, Long targetUid) {
        return lambdaQuery()
                .eq(UserFriend::getUid, uid)
                .eq(UserFriend::getFriendUid, targetUid)
                .one();
    }


    public CursorPageBaseResp<UserFriend> getFriendPage(Long uid, CursorPageBaseReq cursorPageBaseReq) {
        return CursorUtils.getCursorPageByMysql(this, cursorPageBaseReq,
                wrapper -> wrapper.eq(UserFriend::getUid, uid),
                UserFriend::getId);
    }

    public List<UserFriend> getUserFriend(Long uid, Long friendUid) {
        return lambdaQuery()
                .eq(UserFriend::getUid, uid)
                .eq(UserFriend::getFriendUid, friendUid)
                .or()
                .eq(UserFriend::getFriendUid, uid)
                .eq(UserFriend::getUid, friendUid)
                .select(UserFriend::getId)
                .list();
    }
}
