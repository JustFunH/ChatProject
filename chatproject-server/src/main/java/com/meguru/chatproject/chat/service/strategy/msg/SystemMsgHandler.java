package com.meguru.chatproject.chat.service.strategy.msg;

import com.meguru.chatproject.chat.dao.MessageDao;
import com.meguru.chatproject.chat.domain.entity.Message;
import com.meguru.chatproject.chat.domain.enums.MessageTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description:系统消息
 *
 * @author Meguru
 * @since 2025-05-31
 */
@Component
public class SystemMsgHandler extends AbstractMsgHandler<String> {

    @Autowired
    private MessageDao messageDao;

    @Override
    MessageTypeEnum getMsgTypeEnum() {
        return MessageTypeEnum.SYSTEM;
    }

    @Override
    public void saveMsg(Message msg, String body) {
        Message update = new Message();
        update.setId(msg.getId());
        update.setContent(body);
        messageDao.updateById(update);
    }

    @Override
    public Object showMsg(Message msg) {
        return msg.getContent();
    }

    @Override
    public Object showReplyMsg(Message msg) {
        return msg.getContent();
    }

    @Override
    public String showContactMsg(Message msg) {
        return msg.getContent();
    }
}
