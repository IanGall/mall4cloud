package com.mall4j.cloud.common.idempotent.service;

import com.mall4j.cloud.common.idempotent.model.IdempotentInfo;

public interface IdempotentInfoService{


    int deleteByPrimaryKey(Long id);

    int insert(IdempotentInfo record);

    int insertSelective(IdempotentInfo record);

    IdempotentInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(IdempotentInfo record);

    int updateByPrimaryKey(IdempotentInfo record);

    int updateStatusByIdAndStatus(Byte updatedStatus, Long id, Byte status);

    Boolean idempotentCAS(Byte updatedStatus, Long id, Byte status);
}
