package com.mall4j.cloud.common.idempotent.service;

import com.mall4j.cloud.common.idempotent.model.MsgRetryInfo;

import java.util.List;

public interface MsgRetryInfoService{


    int deleteByPrimaryKey(Long msgId);

    int insert(MsgRetryInfo record);

    int insertSelective(MsgRetryInfo record);

    MsgRetryInfo selectByPrimaryKey(Long msgId);

    int updateByPrimaryKeySelective(MsgRetryInfo record);

    int updateByPrimaryKey(MsgRetryInfo record);

    int updateStatusByMsgIdAndStatus(Byte updatedStatus, Long msgId, Byte status);

    Boolean idempotentCAS(Byte updatedStatus, Long id, Byte status);

    Boolean updateCurrTimeByMsgId(Byte updatedCurrTime, Long msgId);

    List<MsgRetryInfo> selectByStatus(Byte status);
}
