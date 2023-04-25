package com.mall4j.cloud.product.feign;

import com.mall4j.cloud.api.product.dto.SkuStockLockDTO;
import com.mall4j.cloud.api.product.feign.SkuStockLockFeignClient;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.product.service.*;
import com.mall4j.cloud.product.service.SkuStockLockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author FrozenWatermelon
 * @date 2020/12/8
 */
@RestController
@Slf4j
public class SkuStockLockFeignController implements SkuStockLockFeignClient {


    @Autowired
    private SkuStockLockService skuStockLockService;

    @Override
    public ServerResponseEntity<Void> lock(List<SkuStockLockDTO> skuStockLocksParam) {
        // Long userId = AuthUserContext.get().getUserId();
        // log.debug("线程id:{},用户id：{}",Thread.currentThread().getName(), userId);
        return skuStockLockService.lock(skuStockLocksParam);
    }
}
