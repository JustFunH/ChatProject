package com.meguru.chatproject.service;

import com.meguru.chatproject.annotation.SecureInvoke;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

/**
 * 发送 mq 工具类
 */
public class MQProducer {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public void sendMsg(String topic, Object body) {
        Message<Object> build = MessageBuilder.withPayload(body).build();
        rocketMQTemplate.send(topic, build);
    }

    /**
     * 发送可靠消息
     *
     * @param topic
     * @param body
     * @param key
     */
    @SecureInvoke
    public void sendSecureMsg(String topic, Object body, Object key) {
        Message<Object> build = MessageBuilder.withPayload(body)
                .setHeader("KEYS", key).build();
        rocketMQTemplate.send(topic, build);
    }
}
