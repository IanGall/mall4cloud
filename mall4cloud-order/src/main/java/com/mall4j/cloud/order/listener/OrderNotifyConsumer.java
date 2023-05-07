package com.mall4j.cloud.order.listener;

import com.mall4j.cloud.api.leaf.feign.PaymentIdempotentFeignClient;
import com.mall4j.cloud.common.exception.Mall4cloudException;
import com.mall4j.cloud.common.idempotent.model.IdempotentInfo;
import com.mall4j.cloud.common.idempotent.service.IdempotentInfoService;
import com.mall4j.cloud.common.idempotent.message.PayNotifyBO;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.idempotent.constant.MqConstant;
import com.mall4j.cloud.common.util.Json;
import com.mall4j.cloud.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.mall4j.cloud.common.idempotent.constant.IdempotentCst.IN_HANDLED;
import static com.mall4j.cloud.common.idempotent.constant.IdempotentCst.NOT_HANDLED;


/**
 * @author FrozenWatermelon
 * @date 2021/1/7
 */
@Component
@RocketMQMessageListener(topic = MqConstant.ORDER_NOTIFY_TOPIC, consumerGroup = MqConstant.ORDER_NOTIFY_TOPIC)
@Slf4j
public class OrderNotifyConsumer implements RocketMQListener<PayNotifyBO> {

    @Autowired
    private OrderService orderService;
    @Autowired
    private RocketMQTemplate orderNotifyStockTemplate;
    @Autowired
    private PaymentIdempotentFeignClient paymentIdempotentFeignClient;
    @Autowired
    private IdempotentInfoService idempotentInfoService;

    @Override
    public void onMessage(PayNotifyBO message) {

        Long idempotentId = message.getIdempotentId();
        IdempotentInfo info = idempotentInfoService.selectByPrimaryKey(idempotentId);
        if (info == null) {
            // 保存
            IdempotentInfo idempotentInfo = new IdempotentInfo();
            idempotentInfo.setId(idempotentId);
            idempotentInfo.setStatus((byte) 0);
            try {
                idempotentInfoService.insertSelective(idempotentInfo);
            } catch (Exception e) {
                log.error("插入异常：{}", e.getMessage());
            }
        }

        if (!idempotentInfoService.idempotentCAS(IN_HANDLED, message.getIdempotentId(), NOT_HANDLED)) {
            log.info("幂等记录状态更新失败... message: {}", Json.toJsonString(message));
            return;
        }
        log.info("订单回调开始... message: " + Json.toJsonString(message));
        orderService.updateByToPaySuccess(message.getOrderIds());
        // 发送消息，订单支付成功 通知库存扣减
        SendStatus sendStockStatus = orderNotifyStockTemplate.syncSend(MqConstant.ORDER_NOTIFY_STOCK_TOPIC, new GenericMessage<>(message)).getSendStatus();
        if (!Objects.equals(sendStockStatus, SendStatus.SEND_OK)) {
            throw new Mall4cloudException(ResponseEnum.EXCEPTION);
        }
    }
}
