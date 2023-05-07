package com.mall4j.cloud.product.config;

import com.mall4j.cloud.common.idempotent.config.RocketMqAdapter;
import com.mall4j.cloud.common.idempotent.constant.MqConstant;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

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
    @Bean(destroyMethod = "destroy")
    public RocketMQTemplate stockMqTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(MqConstant.STOCK_UNLOCK_TOPIC);
    }

}
