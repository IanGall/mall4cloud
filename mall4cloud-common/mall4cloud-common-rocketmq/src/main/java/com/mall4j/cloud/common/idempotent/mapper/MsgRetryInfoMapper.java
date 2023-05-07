package com.mall4j.cloud.common.idempotent.mapper;
import java.util.List;

import com.mall4j.cloud.common.idempotent.model.MsgRetryInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MsgRetryInfoMapper {
    int deleteByPrimaryKey(Long msgId);

    int insert(MsgRetryInfo record);

    int insertSelective(MsgRetryInfo record);

    MsgRetryInfo selectByPrimaryKey(Long msgId);

    int updateByPrimaryKeySelective(MsgRetryInfo record);

    int updateByPrimaryKey(MsgRetryInfo record);

    int updateStatusByMsgIdAndStatus(@Param("updatedStatus") Byte updatedStatus, @Param("msgId") Long msgId, @Param("status") Byte status);

    int updateCurrTimeByMsgId(@Param("updatedCurrTime")Byte updatedCurrTime,@Param("msgId")Long msgId);

    List<MsgRetryInfo> selectByStatus(@Param("status")Byte status);

}
