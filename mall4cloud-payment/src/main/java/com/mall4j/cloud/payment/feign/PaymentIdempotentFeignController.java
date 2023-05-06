package com.mall4j.cloud.payment.feign;

import com.mall4j.cloud.api.leaf.feign.PaymentIdempotentFeignClient;
import com.mall4j.cloud.common.idempotent.controller.AbstractIdempotentFeignController;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentIdempotentFeignController extends AbstractIdempotentFeignController implements PaymentIdempotentFeignClient {

    @Override
    public ServerResponseEntity<Boolean> idempotentCAS(Byte updatedStatus, Long id, Byte status) {
        return doIdempotentUpdate(updatedStatus, id,status );
    }
}
