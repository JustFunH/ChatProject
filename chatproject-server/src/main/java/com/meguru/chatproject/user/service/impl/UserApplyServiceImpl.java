package com.meguru.chatproject.user.service.impl;

import com.meguru.chatproject.chat.domain.enums.MessageStatusEnum;
import com.meguru.chatproject.common.domain.vo.request.CursorPageBaseReq;
import com.meguru.chatproject.user.domain.entity.UserApply;
import com.meguru.chatproject.user.mapper.UserApplyMapper;
import com.meguru.chatproject.user.service.IUserApplyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * <p>
 * 用户申请表 服务实现类
 * </p>
 *
 * @author Meguru
 * @since 2025-05-28
 */
@Service
public class UserApplyServiceImpl extends ServiceImpl<UserApplyMapper, UserApply> implements IUserApplyService {


}
