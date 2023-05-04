package com.mall4j.cloud.order.config;

import com.mall4j.cloud.common.rocketmq.config.RocketMqAdapter;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import static com.mall4j.cloud.common.rocketmq.config.RocketMqConstant.MQTemplateName.*;

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
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.STOCK_UNLOCK_TOPIC);
    }


    @Lazy
    @Bean(destroyMethod = "destroy", name = ORDER_CANCEL_TEMPLATE)
    public RocketMQTemplate orderCancelTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.ORDER_CANCEL_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy", name = ORDER_NOTIFY_STOCK_TEMPLATE)
    public RocketMQTemplate orderNotifyStockTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.ORDER_NOTIFY_STOCK_TOPIC);
    }
}
