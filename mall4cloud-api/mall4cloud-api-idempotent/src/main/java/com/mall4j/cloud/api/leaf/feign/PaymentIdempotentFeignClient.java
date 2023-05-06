package com.mall4j.cloud.api.leaf.feign;

import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author FrozenWatermelon
 * @date 2020/09/08
 */
@FeignClient(value = "mall4cloud-payment",contextId ="paymentIdempotent")
public interface PaymentIdempotentFeignClient {

	/**
	 * 修改幂等状态
	 *
	 * @param updatedStatus
	 * @param id
	 * @return
	 */
	@PutMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/idempotent")
	ServerResponseEntity<Boolean> idempotentCAS(@RequestParam("updatedStatus") Byte updatedStatus, @RequestParam("id") Long id, @RequestParam("status") Byte status);


}
