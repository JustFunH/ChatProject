package com.meguru.chatproject.user.service.impl;

import com.meguru.chatproject.common.exception.BusinessErrorEnum;
import com.meguru.chatproject.common.utils.AssertUtil;
import com.meguru.chatproject.sensitive.algorithm.SensitiveWordBs;
import com.meguru.chatproject.user.dao.BlackDao;
import com.meguru.chatproject.user.dao.UserDao;
import com.meguru.chatproject.user.domain.dto.SummaryInfoDTO;
import com.meguru.chatproject.user.domain.entity.Black;
import com.meguru.chatproject.user.domain.entity.User;
import com.meguru.chatproject.user.domain.enums.BlackTypeEnum;
import com.meguru.chatproject.user.domain.vo.request.user.BlackReq;
import com.meguru.chatproject.user.domain.vo.request.user.ModifyNameReq;
import com.meguru.chatproject.user.domain.vo.request.user.SummaryInfoReq;
import com.meguru.chatproject.user.domain.vo.response.user.UserInfoResp;
import com.meguru.chatproject.user.service.IUserService;
import com.meguru.chatproject.user.service.adapter.UserAdapter;
import com.meguru.chatproject.user.service.cache.UserCache;
import com.meguru.chatproject.user.service.cache.UserSummaryCache;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.text.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author Meguru
 * @since 2025-05-28
 */
@Service
@Slf4j
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserCache userCache;
    @Autowired
    private UserSummaryCache userSummaryCache;
    @Autowired
    private SensitiveWordBs sensitiveWordBs;
    @Autowired
    private UserDao userDao;
    @Autowired
    private BlackDao blackDao;

    @Override
    public UserInfoResp getUserInfo(Long uid) {
        User user = userCache.getUserInfo(uid);
        return UserAdapter.buildUserInfoResp(user);
    }

    @Override
    @Transactional
    public void modifyName(Long uid, ModifyNameReq req) {
        String newName = req.getName();
        AssertUtil.isFalse(sensitiveWordBs.hasSensitiveWord(newName), BusinessErrorEnum.USERNAME_HAS_SENSITIVE_WORD);
        User oldUser = userDao.getByName(newName);
        AssertUtil.isEmpty(oldUser, BusinessErrorEnum.USERNAME_HAS_EXIST);
        userDao.modifyName(uid, newName);
        userCache.userInfoChange(uid);
    }

    @Override
    public List<SummaryInfoDTO> getSummeryUserInfo(SummaryInfoReq req) {
        List<Long> uidList = getNeedSyncUidList(req.getReqList());
        Map<Long, SummaryInfoDTO> batch = userSummaryCache.getBatch(uidList);
        return req.getReqList()
                .stream()
                .map(a -> batch.containsKey(a.getUid()) ? batch.get(a.getUid()) : SummaryInfoDTO.skip(a.getUid()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private List<Long> getNeedSyncUidList(List<SummaryInfoReq.infoReq> reqList) {
        List<Long> needSyncUidList = new ArrayList<>();
        List<Long> userModifyTime = userCache.getUserModifyTime(reqList.stream().map(SummaryInfoReq.infoReq::getUid).collect(Collectors.toList()));
        for (int i = 0; i < reqList.size(); i++) {
            SummaryInfoReq.infoReq infoReq = reqList.get(i);
            Long modifyTime = userModifyTime.get(i);
            if (Objects.isNull(infoReq.getLastModifyTime()) || (Objects.nonNull(modifyTime) && modifyTime > infoReq.getLastModifyTime())) {
                needSyncUidList.add(infoReq.getUid());
            }
        }
        return needSyncUidList;
    }

    @Override
    public void black(BlackReq req) {
        Long uid = req.getUid();
        Black user = new Black();
        user.setTarget(uid.toString());
        user.setType(BlackTypeEnum.UID.getType());
        blackDao.save(user);
        User byId = userDao.getById(uid);
        blackIp(byId.getIpInfo().getCreateIp());
        blackIp(byId.getIpInfo().getUpdateIp());
    }

    @Override
    public void blackIp(String ip) {
        if (StrUtil.isBlank(ip)) {
            return;
        }
        try {
            Black user = new Black();
            user.setTarget(ip);
            user.setType(BlackTypeEnum.IP.getType());
            blackDao.save(user);
        } catch (Exception e) {
            log.error("duplicate black ip:{}", ip);
        }
    }

}
