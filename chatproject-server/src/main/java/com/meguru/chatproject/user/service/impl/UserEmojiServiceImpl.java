package com.meguru.chatproject.user.service.impl;

import com.meguru.chatproject.common.annotation.RedissonLock;
import com.meguru.chatproject.common.domain.vo.response.ApiResult;
import com.meguru.chatproject.common.domain.vo.response.IdRespVO;
import com.meguru.chatproject.common.exception.BusinessErrorEnum;
import com.meguru.chatproject.common.utils.AssertUtil;
import com.meguru.chatproject.user.dao.UserEmojiDao;
import com.meguru.chatproject.user.domain.entity.UserEmoji;
import com.meguru.chatproject.user.domain.vo.request.user.UserEmojiReq;
import com.meguru.chatproject.user.domain.vo.response.user.UserEmojiResp;
import com.meguru.chatproject.user.service.IUserEmojiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表情包 服务实现类
 * </p>
 *
 * @author Meguru
 * @since 2025-05-28
 */
@Service
@Slf4j
public class UserEmojiServiceImpl implements IUserEmojiService {

    @Autowired
    private UserEmojiDao userEmojiDao;

    @Override
    public List<UserEmojiResp> list(Long uid) {
        return userEmojiDao.listByUid(uid).
                stream()
                .map(a -> UserEmojiResp.builder()
                        .id(a.getId())
                        .expressionUrl(a.getExpressionUrl())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * 新增表情包
     *
     * @param uid 用户ID
     * @return 表情包
     * @author Meguru
     * @since 2025-05-28
     **/
    @Override
    @RedissonLock(key = "#uid")
    public ApiResult<IdRespVO> insert(UserEmojiReq req, Long uid) {
        //校验表情数量是否超过30
        int count = (int) userEmojiDao.countByUid(uid);
        AssertUtil.isFalse(count > 30, BusinessErrorEnum.EMOJI_OUT_OF_RANGE);
        //校验表情是否存在
        Long existsCount = userEmojiDao.lambdaQuery()
                .eq(UserEmoji::getExpressionUrl, req.getExpressionUrl())
                .eq(UserEmoji::getUid, uid)
                .count();
        AssertUtil.isFalse(existsCount > 0, BusinessErrorEnum.EMOJI_HAS_EXIST);
        UserEmoji insert = UserEmoji.builder().uid(uid).expressionUrl(req.getExpressionUrl()).build();
        userEmojiDao.save(insert);
        return ApiResult.success(IdRespVO.id(insert.getId()));
    }

    @Override
    public void remove(Long id, Long uid) {
        UserEmoji userEmoji = userEmojiDao.getById(id);
        AssertUtil.isNotEmpty(userEmoji, BusinessErrorEnum.EMOJI_NOT_EXIST);
        AssertUtil.equal(userEmoji.getUid(), uid, BusinessErrorEnum.USER_POWER_ERROR.getErrorMsg());
        userEmojiDao.removeById(id);
    }
}
