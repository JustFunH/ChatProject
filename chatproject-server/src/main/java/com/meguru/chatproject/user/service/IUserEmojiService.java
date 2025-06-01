package com.meguru.chatproject.user.service;

import com.meguru.chatproject.common.annotation.RedissonLock;
import com.meguru.chatproject.common.domain.vo.response.ApiResult;
import com.meguru.chatproject.common.domain.vo.response.IdRespVO;
import com.meguru.chatproject.user.domain.vo.request.user.UserEmojiReq;
import com.meguru.chatproject.user.domain.vo.response.user.UserEmojiResp;

import java.util.List;

/**
 * <p>
 * 用户表情包 服务类
 * </p>
 *
 * @author Meguru
 * @since 2025-05-28
 */
public interface IUserEmojiService {

    List<UserEmojiResp> list(Long uid);

    @RedissonLock(key = "#uid")
    ApiResult<IdRespVO> insert(UserEmojiReq req, Long uid);

    void remove(Long id, Long uid);
}
