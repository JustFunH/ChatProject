package com.meguru.chatproject.user.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meguru.chatproject.common.domain.enums.NormalOrNoEnum;
import com.meguru.chatproject.common.domain.vo.request.CursorPageBaseReq;
import com.meguru.chatproject.common.domain.vo.response.CursorPageBaseResp;
import com.meguru.chatproject.common.utils.CursorUtils;
import com.meguru.chatproject.user.domain.entity.User;
import com.meguru.chatproject.user.domain.enums.ChatActiveStatusEnum;
import com.meguru.chatproject.user.mapper.UserMapper;
import org.dromara.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author Meguru
 * @since 2025-05-29
 */
@Service
public class UserDao extends ServiceImpl<UserMapper, User> {

    public void modifyName(Long uid, String name) {
        User update = new User();
        update.setId(uid);
        update.setName(name);
        updateById(update);
    }

    public User getByUsername(String username) {
        return lambdaQuery().eq(User::getUsername, username).one();
    }

    public User getByName(String name) {
        return lambdaQuery().eq(User::getName, name).one();
    }

    public List<User> getMemberList() {
        return lambdaQuery()
                .eq(User::getStatus, NormalOrNoEnum.NORMAL.getStatus())
                .orderByDesc(User::getLastOptTime)
                .last("limit 1000")
                .select(User::getId, User::getName, User::getAvatar)
                .list();
    }

    public List<User> getFriendList(List<Long> uids) {
        return lambdaQuery()
                .in(User::getId, uids)
                .select(User::getId, User::getActiveStatus, User::getName, User::getAvatar)
                .list();
    }

    public Long getOnlineCount(List<Long> memberUidList) {
        return lambdaQuery()
                .eq(User::getActiveStatus, ChatActiveStatusEnum.ONLINE.getStatus())
                .in(CollUtil.isNotEmpty(memberUidList), User::getId, memberUidList)
                .count();
    }


    public CursorPageBaseResp<User> getCursorPage(List<Long> memberUidList, CursorPageBaseReq request, ChatActiveStatusEnum online) {
        return CursorUtils.getCursorPageByMysql(this, request, wrapper -> {
            wrapper.eq(User::getActiveStatus, online.getStatus());//筛选上线或者离线的
            wrapper.in(CollectionUtils.isNotEmpty(memberUidList), User::getId, memberUidList);//普通群对uid列表做限制
        }, User::getLastOptTime);
    }
}
