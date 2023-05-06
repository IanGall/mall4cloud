package com.mall4j.cloud.common.idempotent.service;


import com.mall4j.cloud.common.idempotent.model.MsgInfo;

import java.util.Collection;
import java.util.List;

public interface MsgInfoService{


    int deleteByPrimaryKey(Long msgId);

    int insert(MsgInfo record);

    int insertSelective(MsgInfo record);

    MsgInfo selectByPrimaryKey(Long msgId);

    int updateByPrimaryKeySelective(MsgInfo record);

    int updateByPrimaryKey(MsgInfo record);

    List<MsgInfo> selectByMsgIdIn(Collection<Long> msgIdCollection);


    void batchAsyncSend(List<MsgInfo> msgInfos);

    List<MsgInfo> selectByMsgIdInAndStatus(Collection<Long> msgIdCollection, Byte status);

    List<MsgInfo> selectByStatus(Byte status);

    int updateStatusByMsgIdAndStatus(Byte updatedStatus, Long msgId, Byte status);
}
