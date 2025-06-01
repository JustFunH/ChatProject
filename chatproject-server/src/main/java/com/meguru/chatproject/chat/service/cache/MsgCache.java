package com.meguru.chatproject.chat.service.cache;

import com.meguru.chatproject.chat.dao.MessageDao;
import com.meguru.chatproject.chat.domain.entity.Message;
import com.meguru.chatproject.user.dao.BlackDao;
import com.meguru.chatproject.user.dao.RoleDao;
import com.meguru.chatproject.user.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * Description: 消息相关缓存
 *
 * @author Meguru
 * @since 2025-05-31
 */
@Component
public class MsgCache {

    @Autowired
    private UserDao userDao;
    @Autowired
    private BlackDao blackDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private MessageDao messageDao;

    @Cacheable(cacheNames = "msg", key = "'msg'+#msgId")
    public Message getMsg(Long msgId) {
        return messageDao.getById(msgId);
    }

    @CacheEvict(cacheNames = "msg", key = "'msg'+#msgId")
    public Message evictMsg(Long msgId) {
        return null;
    }
}
