package com.mall4j.cloud.order.config;

import com.mall4j.cloud.common.idempotent.config.RocketMqAdapter;
import com.mall4j.cloud.common.idempotent.constant.MqConstant;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import static com.mall4j.cloud.common.idempotent.constant.MqConstant.MQTemplateName.*;

/**
 * @author FrozenWatermelon
 * @date 2021/3/30
 */
@RefreshScope
@Configuration
public class RocketMqConfig {

    @Autowired
    private RocketMqAdapter rocketMqAdapter;

    @Lazy
    @Bean(destroyMethod = "destroy", name = STOCK_MQ_TEMPLATE)
    public RocketMQTemplate stockMqTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(MqConstant.STOCK_UNLOCK_TOPIC);
    }


    @Lazy
    @Bean(destroyMethod = "destroy", name = ORDER_CANCEL_TEMPLATE)
    public RocketMQTemplate orderCancelTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(MqConstant.ORDER_CANCEL_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy", name = ORDER_NOTIFY_STOCK_TEMPLATE)
    public RocketMQTemplate orderNotifyStockTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(MqConstant.ORDER_NOTIFY_STOCK_TOPIC);
    }
}
