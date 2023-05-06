package com.mall4j.cloud.common.idempotent.model;

import com.mall4j.cloud.common.database.annotations.DistributedId;
import com.mall4j.cloud.common.model.BaseModel;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 幂等表
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class IdempotentInfo extends BaseModel {
    /**
     * 幂等id
     */
    private Long id;

    /**
     * 状态，0-未开始，1-处理中，2-已处理
     */
    private Byte status;

    /**
     * 失败原因
     */
    private String errorMsg;
}
