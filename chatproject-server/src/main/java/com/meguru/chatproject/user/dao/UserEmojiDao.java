package com.meguru.chatproject.user.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meguru.chatproject.user.domain.entity.UserEmoji;
import com.meguru.chatproject.user.mapper.UserEmojiMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户表情包 服务实现类
 * </p>
 *
 * @author Meguru
 * @since 2025-05-29
 */
@Service
public class UserEmojiDao extends ServiceImpl<UserEmojiMapper, UserEmoji> {

    public List<UserEmoji> listByUid(Long uid) {
        return lambdaQuery()
                .eq(UserEmoji::getUid, uid)
                .list();
    }

    public int countByUid(Long uid) {
        return lambdaQuery()
                .eq(UserEmoji::getUid, uid)
                .count();
    }
}
