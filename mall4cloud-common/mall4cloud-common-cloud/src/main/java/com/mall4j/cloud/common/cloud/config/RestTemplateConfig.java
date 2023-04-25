package com.mall4j.cloud.common.cloud.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author FrozenWatermelon
 * @date 2020/7/11
 */
@Configuration
@LoadBalancerClient(value = "mall4cloud-order", configuration = CustomLoadBalancerConfiguration.class)
@Slf4j
public class RestTemplateConfig {
	// @Value("${spring.application.name}")
	// private String value;
	@Bean
	@ConditionalOnMissingBean
	@LoadBalanced
	public RestTemplate restTemplate() {
		// log.debug("spring.application.name: {}",value);
		return new RestTemplate();
	}

}
