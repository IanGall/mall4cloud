package com.mall4j.cloud.common.idempotent.service.impl;

import com.mall4j.cloud.common.idempotent.mapper.IdempotentInfoMapper;
import com.mall4j.cloud.common.idempotent.model.IdempotentInfo;
import com.mall4j.cloud.common.idempotent.service.IdempotentInfoService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service
public class IdempotentInfoServiceImpl implements IdempotentInfoService {

    @Resource
    private IdempotentInfoMapper idempotentInfoMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return idempotentInfoMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(IdempotentInfo record) {
        return idempotentInfoMapper.insert(record);
    }

    @Override
    public int insertSelective(IdempotentInfo record) {
        return idempotentInfoMapper.insertSelective(record);
    }

    @Override
    public IdempotentInfo selectByPrimaryKey(Long id) {
        return idempotentInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(IdempotentInfo record) {
        return idempotentInfoMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(IdempotentInfo record) {
        return idempotentInfoMapper.updateByPrimaryKey(record);
    }

    @Override
    public int updateStatusByIdAndStatus(Byte updatedStatus, Long id, Byte status) {
        return idempotentInfoMapper.updateStatusByIdAndStatus(updatedStatus, id, status);
    }

    @Override
    public Boolean idempotentCAS(Byte updatedStatus, Long id, Byte status) {
        int affectedRows = updateStatusByIdAndStatus(updatedStatus, id, status);
        return affectedRows == 1;
    }
}
