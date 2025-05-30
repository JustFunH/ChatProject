package com.meguru.chatproject.user.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meguru.chatproject.user.domain.entity.UserApply;
import com.meguru.chatproject.user.domain.enums.ApplyReadStatusEnum;
import com.meguru.chatproject.user.domain.enums.ApplyStatusEnum;
import com.meguru.chatproject.user.domain.enums.ApplyTypeEnum;
import com.meguru.chatproject.user.mapper.UserApplyMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户申请表 服务实现类
 * </p>
 *
 * @author Meguru
 * @since 2025-05-29
 */
@Service
public class UserApplyDao extends ServiceImpl<UserApplyMapper, UserApply> {
    public UserApply getFriendApproving(Long uid, Long targetUid) {
        return lambdaQuery()
                .eq(UserApply::getUid, uid)
                .eq(UserApply::getTargetId, targetUid)
                .eq(UserApply::getStatus, ApplyStatusEnum.WAIT_APPROVAL)
                .eq(UserApply::getType, ApplyTypeEnum.ADD_FRIEND.getCode())
                .one();
    }

    public Integer getUnReadCount(Long targetId) {
        return lambdaQuery()
                .eq(UserApply::getTargetId, targetId)
                .eq(UserApply::getStatus, ApplyReadStatusEnum.UNREAD.getCode())
                .count();
    }

    public IPage<UserApply> friendApplyPage(Long uid, Page page) {
        return lambdaQuery()
                .eq(UserApply::getTargetId, uid)
                .eq(UserApply::getType, ApplyTypeEnum.ADD_FRIEND.getCode())
                .orderByDesc(UserApply::getCreateTime)
                .page(page);
    }

    public void readApplies(Long uid, List<Long> applyIds) {
        lambdaUpdate()
                .set(UserApply::getReadStatus, ApplyReadStatusEnum.READ.getCode())
                .eq(UserApply::getReadStatus, ApplyReadStatusEnum.UNREAD.getCode())
                .in(UserApply::getId, applyIds)
                .eq(UserApply::getTargetId, uid)
                .update();
    }

    public void agree(Long applyId) {
        lambdaUpdate()
                .set(UserApply::getStatus, ApplyStatusEnum.AGREE.getCode())
                .eq(UserApply::getId, applyId)
                .update();
    }

}
