package com.mall4j.cloud.payment.comsumer;

import com.mall4j.cloud.common.idempotent.handler.MsgBizHandler;
import com.mall4j.cloud.payment.message.Demo01Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class Demo01Consumer implements MsgBizHandler<Demo01Message> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @KafkaListener(topics = Demo01Message.TOPIC,
            groupId = "demo01-consumer-group-" + Demo01Message.TOPIC)
    public void onMessage(Demo01Message message) {
        logger.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
    }

    @Override
    public boolean doHandle(Demo01Message o) {
        return false;
    }
}
