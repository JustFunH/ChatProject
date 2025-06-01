package com.meguru.chatproject.user.service;

import com.meguru.chatproject.user.domain.dto.SummaryInfoDTO;
import com.meguru.chatproject.user.domain.vo.request.user.BlackReq;
import com.meguru.chatproject.user.domain.vo.request.user.ModifyNameReq;
import com.meguru.chatproject.user.domain.vo.request.user.SummaryInfoReq;
import com.meguru.chatproject.user.domain.vo.response.user.UserInfoResp;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author Meguru
 * @since 2025-05-28
 */
public interface IUserService {

    /**
     * 获取前端展示信息
     *
     * @param uid
     * @return
     */
    UserInfoResp getUserInfo(Long uid);

    /**
     * 修改用户名
     *
     * @param uid
     * @param req
     */
    void modifyName(Long uid, ModifyNameReq req);

    /**
     * 获取用户聚合信息
     *
     * @param req
     * @return
     */
    List<SummaryInfoDTO> getSummeryUserInfo(SummaryInfoReq req);

    /**
     * 拉黑用户
     *
     * @param req
     */
    void black(BlackReq req);

    /**
     * 拉黑 IP
     *
     * @param ip
     */
    void blackIp(String ip);
}
