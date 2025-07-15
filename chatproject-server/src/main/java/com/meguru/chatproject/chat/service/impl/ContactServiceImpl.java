package com.meguru.chatproject.chat.service.impl;

import com.meguru.chatproject.chat.dao.ContactDao;
import com.meguru.chatproject.chat.dao.MessageDao;
import com.meguru.chatproject.chat.domain.dto.MsgReadInfoDTO;
import com.meguru.chatproject.chat.domain.entity.Contact;
import com.meguru.chatproject.chat.domain.entity.Message;
import com.meguru.chatproject.chat.service.ContactService;
import com.meguru.chatproject.chat.service.adapter.ChatAdapter;
import com.meguru.chatproject.common.exception.BusinessErrorEnum;
import com.meguru.chatproject.common.utils.AssertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description: 会话列表
 *
 * @author Meguru
 * @since 2025-05-31
 */
@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactDao contactDao;
    @Autowired
    private MessageDao messageDao;

    @Override
    public Contact createContact(Long uid, Long roomId) {
        Contact contact = contactDao.get(uid, roomId);
        if (Objects.isNull(contact)) {
            contact = ChatAdapter.buildContact(uid, roomId);
            contactDao.save(contact);
        }
        return contact;
    }

    @Override
    public Integer getMsgReadCount(Message message) {
        return contactDao.getReadCount(message).intValue();
    }

    @Override
    public Integer getMsgUnReadCount(Message message) {
        return contactDao.getUnReadCount(message).intValue();
    }

    @Override
    public Map<Long, MsgReadInfoDTO> getMsgReadInfo(List<Message> messages) {
        Map<Long, List<Message>> roomGroup = messages.stream().collect(Collectors.groupingBy(Message::getRoomId));
        AssertUtil.equal(roomGroup.size(), 1, BusinessErrorEnum.GROUP_ID_ERROR.getErrorMsg());
        Long roomId = roomGroup.keySet().iterator().next();
        Integer totalCount = contactDao.getTotalCount(roomId).intValue();
        return messages.stream().map(message -> {
            MsgReadInfoDTO readInfoDTO = new MsgReadInfoDTO();
            readInfoDTO.setMsgId(message.getId());
            Integer readCount = contactDao.getReadCount(message).intValue();
            readInfoDTO.setReadCount(readCount);
            readInfoDTO.setUnReadCount(totalCount - readCount - 1);
            return readInfoDTO;
        }).collect(Collectors.toMap(MsgReadInfoDTO::getMsgId, Function.identity()));
    }
}
