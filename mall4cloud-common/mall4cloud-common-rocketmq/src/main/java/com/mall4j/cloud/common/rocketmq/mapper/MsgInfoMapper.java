package com.mall4j.cloud.common.rocketmq.mapper;

import com.mall4j.cloud.common.rocketmq.model.MsgInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

@Mapper
public interface MsgInfoMapper {
    int deleteByPrimaryKey(Long msgId);

    int insert(MsgInfo record);

    int insertSelective(MsgInfo record);

    MsgInfo selectByPrimaryKey(Long msgId);

    int updateByPrimaryKeySelective(MsgInfo record);

    int updateByPrimaryKey(MsgInfo record);

    List<MsgInfo> selectByMsgIdIn(@Param("msgIdCollection") Collection<Long> msgIdCollection);

    int updateStatusByMsgId(@Param("updatedStatus")Byte updatedStatus,@Param("msgId")Long msgId);

    int updateStatusByMsgIdAndStatus(@Param("updatedStatus")Byte updatedStatus,@Param("msgId")Long msgId,@Param("status")Byte status);

    List<MsgInfo> selectByMsgIdInAndStatus(@Param("msgIdCollection")Collection<Long> msgIdCollection,@Param("status")Byte status);

    List<MsgInfo> selectByStatus(@Param("status")Byte status);


}
