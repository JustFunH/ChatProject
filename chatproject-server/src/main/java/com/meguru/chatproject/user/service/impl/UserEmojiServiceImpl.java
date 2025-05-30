package com.meguru.chatproject.user.service.impl;

import com.meguru.chatproject.user.domain.entity.UserEmoji;
import com.meguru.chatproject.user.mapper.UserEmojiMapper;
import com.meguru.chatproject.user.service.IUserEmojiService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表情包 服务实现类
 * </p>
 *
 * @author Meguru
 * @since 2025-05-28
 */
@Service
public class UserEmojiServiceImpl extends ServiceImpl<UserEmojiMapper, UserEmoji> implements IUserEmojiService {

}
