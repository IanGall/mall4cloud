package com.mall4j.cloud.common.idempotent.mapper;
import org.apache.ibatis.annotations.Param;

import com.mall4j.cloud.common.idempotent.model.IdempotentInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IdempotentInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(IdempotentInfo record);

    int insertSelective(IdempotentInfo record);

    IdempotentInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(IdempotentInfo record);

    int updateByPrimaryKey(IdempotentInfo record);

    int updateStatusByIdAndStatus(@Param("updatedStatus")Byte updatedStatus,@Param("id")Long id,@Param("status")Byte status);


}
