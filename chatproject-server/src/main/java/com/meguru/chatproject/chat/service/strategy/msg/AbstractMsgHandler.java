package com.meguru.chatproject.chat.service.strategy.msg;

import cn.hutool.core.bean.BeanUtil;
import com.meguru.chatproject.chat.dao.MessageDao;
import com.meguru.chatproject.chat.domain.entity.Message;
import com.meguru.chatproject.chat.domain.enums.MessageTypeEnum;
import com.meguru.chatproject.chat.domain.vo.request.ChatMessageReq;
import com.meguru.chatproject.chat.service.adapter.MessageAdapter;
import com.meguru.chatproject.common.utils.AssertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.lang.reflect.ParameterizedType;

/**
 * Description: 消息处理器抽象类
 *
 * @author Meguru
 * @since 2025-05-31
 */
public abstract class AbstractMsgHandler<Req> {
    @Autowired
    private MessageDao messageDao;
    private Class<Req> bodyClass;

    @PostConstruct
    private void init() {
        ParameterizedType genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.bodyClass = (Class<Req>) genericSuperclass.getActualTypeArguments()[0];
        MsgHandlerFactory.register(getMsgTypeEnum().getType(), this);
    }

    /**
     * 消息类型
     */
    abstract MessageTypeEnum getMsgTypeEnum();

    protected void checkMsg(Req body, Long roomId, Long uid) {

    }

    @Transactional
    public Long checkAndSaveMsg(ChatMessageReq request, Long uid) {
        Req body = this.toBean(request.getBody());
        //统一校验
        AssertUtil.allCheckValidateThrow(body);
        //子类扩展校验
        checkMsg(body, request.getRoomId(), uid);
        Message insert = MessageAdapter.buildMsgSave(request, uid);
        //统一保存
        messageDao.save(insert);
        //子类扩展保存
        saveMsg(insert, body);
        return insert.getId();
    }

    private Req toBean(Object body) {
        if (bodyClass.isAssignableFrom(body.getClass())) {
            return (Req) body;
        }
        return BeanUtil.toBean(body, bodyClass);
    }

    protected abstract void saveMsg(Message message, Req body);

    /**
     * 展示消息
     */
    public abstract Object showMsg(Message msg);

    /**
     * 被回复时——展示的消息
     */
    public abstract Object showReplyMsg(Message msg);

    /**
     * 会话列表——展示的消息
     */
    public abstract String showContactMsg(Message msg);

}
