package com.mall4j.cloud.payment.config;

import com.mall4j.cloud.common.idempotent.config.RocketMqAdapter;
import com.mall4j.cloud.common.idempotent.constant.MqConstant;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import static com.mall4j.cloud.common.idempotent.constant.MqConstant.MQTemplateName.ORDER_NOTIFY_TEMPLATE;

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
    @Bean(destroyMethod = "destroy",value = ORDER_NOTIFY_TEMPLATE)
    public RocketMQTemplate orderNotifyTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(MqConstant.ORDER_NOTIFY_TOPIC);
    }


}
