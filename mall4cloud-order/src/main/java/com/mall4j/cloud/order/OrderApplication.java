package com.mall4j.cloud.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author FrozenWatermelon
 * @date 2020/11/19
 */
@SpringBootApplication(scanBasePackages = { "com.mall4j.cloud" })
@EnableFeignClients(basePackages = {"com.mall4j.cloud.api.**.feign"})
@EnableScheduling
public class OrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderApplication.class, args);
	}

}
