package com.meguru.chatproject.chat.service.strategy.msg;

import com.meguru.chatproject.chat.dao.MessageDao;
import com.meguru.chatproject.chat.domain.entity.Message;
import com.meguru.chatproject.chat.domain.entity.msg.FileMsgDTO;
import com.meguru.chatproject.chat.domain.entity.msg.MessageExtra;
import com.meguru.chatproject.chat.domain.enums.MessageTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Description:图片消息
 *
 * @author Meguru
 * @since 2025-05-31
 */
@Component
public class FileMsgHandler extends AbstractMsgHandler<FileMsgDTO> {
    @Autowired
    private MessageDao messageDao;

    @Override
    MessageTypeEnum getMsgTypeEnum() {
        return MessageTypeEnum.FILE;
    }

    @Override
    public void saveMsg(Message msg, FileMsgDTO body) {
        MessageExtra extra = Optional.ofNullable(msg.getExtra()).orElse(new MessageExtra());
        Message update = new Message();
        update.setId(msg.getId());
        update.setExtra(extra);
        extra.setFileMsg(body);
        messageDao.updateById(update);
    }

    @Override
    public Object showMsg(Message msg) {
        return msg.getExtra().getFileMsg();
    }

    @Override
    public Object showReplyMsg(Message msg) {
        return "文件:" + msg.getExtra().getFileMsg().getFileName();
    }

    @Override
    public String showContactMsg(Message msg) {
        return "[文件]" + msg.getExtra().getFileMsg().getFileName();
    }
}
