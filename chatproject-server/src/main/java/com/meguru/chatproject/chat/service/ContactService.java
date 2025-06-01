package com.meguru.chatproject.chat.service;

import com.meguru.chatproject.chat.domain.dto.MsgReadInfoDTO;
import com.meguru.chatproject.chat.domain.entity.Contact;
import com.meguru.chatproject.chat.domain.entity.Message;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 会话列表 服务类
 * </p>
 *
 * @author Meguru
 * @since 2025-05-31
 */
public interface ContactService {
    /**
     * 创建会话
     */
    Contact createContact(Long uid, Long roomId);

    Integer getMsgReadCount(Message message);

    Integer getMsgUnReadCount(Message message);

    Map<Long, MsgReadInfoDTO> getMsgReadInfo(List<Message> messages);
}
