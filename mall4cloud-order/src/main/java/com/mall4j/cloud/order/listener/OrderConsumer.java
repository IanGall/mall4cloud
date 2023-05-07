package com.mall4j.cloud.order.listener;

import cn.hutool.json.JSONUtil;
import com.mall4j.cloud.api.leaf.feign.PaymentIdempotentFeignClient;
import com.mall4j.cloud.common.idempotent.constant.MqConstant;
import com.mall4j.cloud.common.idempotent.handler.MsgBizHandler;
import com.mall4j.cloud.common.idempotent.message.PayNotifyBO;
import com.mall4j.cloud.common.idempotent.model.IdempotentInfo;
import com.mall4j.cloud.common.idempotent.model.MsgRetryInfo;
import com.mall4j.cloud.common.idempotent.service.IdempotentInfoService;
import com.mall4j.cloud.common.idempotent.service.MsgRetryInfoService;
import com.mall4j.cloud.common.idempotent.service.impl.MqIdempotentManagerServiceImpl;
import com.mall4j.cloud.common.util.Json;
import com.mall4j.cloud.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.mall4j.cloud.common.idempotent.constant.IdempotentCst.*;
import static com.mall4j.cloud.common.idempotent.constant.MqConstant.MQConsumerName.ORDER_CONSUMER;
import static com.mall4j.cloud.common.idempotent.constant.MqConstant.ORDER_NOTIFY_TOPIC;

@Component(ORDER_CONSUMER)
@Slf4j
public class OrderConsumer implements MsgBizHandler<PayNotifyBO> {

    @Autowired
    private OrderService orderService;
    @Autowired
    private RocketMQTemplate orderNotifyStockTemplate;
    @Autowired
    private PaymentIdempotentFeignClient paymentIdempotentFeignClient;
    @Autowired
    private IdempotentInfoService idempotentInfoService;
    @Autowired
    private MsgRetryInfoService msgRetryInfoService;
    @Autowired
    private MqIdempotentManagerServiceImpl mqIdempotentManagerService;


    @KafkaListener(topics = ORDER_NOTIFY_TOPIC,
            groupId = "order-consumer-group-" + ORDER_NOTIFY_TOPIC)
    public void onMessage(PayNotifyBO message) {

        Long idempotentId = message.getIdempotentId();
        // 插入幂等记录
        IdempotentInfo idempotentInfo = new IdempotentInfo();
        idempotentInfo.setId(idempotentId);
        idempotentInfo.setStatus((byte) 1);
        MsgRetryInfo record =
                new MsgRetryInfo(idempotentId, JSONUtil.toJsonStr(message), message.getClass().getName(), ORDER_CONSUMER);
        try {
            mqIdempotentManagerService.mqIdempotentInsert(idempotentInfo, record);
            // message.setIdempotentId(record.getMsgId());
        } catch (Exception e) {
            log.error("插入异常：{}", e.getMessage());
            return;
        }

        // acknowledgment.acknowledge();
        doHandle(message);
    }

    @Override
    public boolean doHandle(PayNotifyBO o) {
        Long idempotentId = o.getIdempotentId();
        // 尝试获取锁
        if (!msgRetryInfoService.idempotentCAS(IN_HANDLED, idempotentId, NOT_HANDLED)) {
            log.info("幂等记录状态更新失败... message: {}", Json.toJsonString(o));
            return false;
        }


        log.info("订单回调开始... message: " + Json.toJsonString(o));

        orderService.updateByToPaySuccess(o.getOrderIds());
        // 发送消息，订单支付成功 通知库存扣减
        SendStatus sendStockStatus = orderNotifyStockTemplate.syncSend(MqConstant.ORDER_NOTIFY_STOCK_TOPIC, new GenericMessage<>(o)).getSendStatus();
        if (Objects.equals(sendStockStatus, SendStatus.SEND_OK)) {
            // 更新状态成功
            msgRetryInfoService.idempotentCAS(HANDLED, idempotentId, IN_HANDLED);
            return true;
        }
        log.error("发送-通知库存扣减-消息失败： {}", o);


        // after
        MsgRetryInfo retryInfo = msgRetryInfoService.selectByPrimaryKey(idempotentId);
        byte currTime = (byte) (retryInfo.getCurrTime() + 1);
        msgRetryInfoService.updateCurrTimeByMsgId(currTime, idempotentId);
        if (currTime >= retryInfo.getMaxTime()) {
            log.info("消息消费表消费次数达到上限... message: {}", Json.toJsonString(o));
            // 更新状态失败
            msgRetryInfoService.idempotentCAS(FAILED, idempotentId, IN_HANDLED);
        } else {
            // 恢复锁
            msgRetryInfoService.idempotentCAS(NOT_HANDLED, idempotentId, IN_HANDLED);
        }
        // if (!msgRetryInfoService.updateCurrTimeByMsgId((byte) (currTime + 1), idempotentId)) {
        //     log.info("更新消费次数失败... message: {}", Json.toJsonString(o));
        //     return false;
        // }


        return false;
    }
}
