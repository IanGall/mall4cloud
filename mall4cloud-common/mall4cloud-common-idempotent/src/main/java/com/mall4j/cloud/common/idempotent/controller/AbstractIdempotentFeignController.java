package com.mall4j.cloud.common.idempotent.controller;

import com.mall4j.cloud.common.idempotent.service.IdempotentInfoService;
import com.mall4j.cloud.common.response.ServerResponseEntity;

import javax.annotation.Resource;


/**
 * @author FrozenWatermelon
 * @date 2020/7/15
 */
public abstract class AbstractIdempotentFeignController {

    @Resource
    private IdempotentInfoService idempotentInfoService;

    protected ServerResponseEntity<Boolean> doIdempotentUpdate(Byte updatedStatus, Long id, Byte status) {
        Boolean successFlag = idempotentInfoService.idempotentCAS(updatedStatus, id, status);
        return ServerResponseEntity.success(successFlag);
    }


}
