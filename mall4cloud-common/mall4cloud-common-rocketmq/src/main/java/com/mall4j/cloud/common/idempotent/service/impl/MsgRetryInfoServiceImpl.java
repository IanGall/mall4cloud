package com.mall4j.cloud.common.idempotent.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.mall4j.cloud.common.idempotent.mapper.MsgRetryInfoMapper;
import com.mall4j.cloud.common.idempotent.model.MsgRetryInfo;
import com.mall4j.cloud.common.idempotent.service.MsgRetryInfoService;

import java.util.List;

@Service
public class MsgRetryInfoServiceImpl implements MsgRetryInfoService{

    @Resource
    private MsgRetryInfoMapper msgRetryInfoMapper;

    @Override
    public int deleteByPrimaryKey(Long msgId) {
        return msgRetryInfoMapper.deleteByPrimaryKey(msgId);
    }

    @Override
    public int insert(MsgRetryInfo record) {
        return msgRetryInfoMapper.insert(record);
    }

    @Override
    public int insertSelective(MsgRetryInfo record) {
        return msgRetryInfoMapper.insertSelective(record);
    }

    @Override
    public MsgRetryInfo selectByPrimaryKey(Long msgId) {
        return msgRetryInfoMapper.selectByPrimaryKey(msgId);
    }

    @Override
    public int updateByPrimaryKeySelective(MsgRetryInfo record) {
        return msgRetryInfoMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(MsgRetryInfo record) {
        return msgRetryInfoMapper.updateByPrimaryKey(record);
    }

    @Override
    public int updateStatusByMsgIdAndStatus(Byte updatedStatus, Long msgId, Byte status) {
        return msgRetryInfoMapper.updateStatusByMsgIdAndStatus(updatedStatus, msgId, status);
    }

    @Override
    public Boolean idempotentCAS(Byte updatedStatus, Long id, Byte status) {
        int affectedRows = updateStatusByMsgIdAndStatus(updatedStatus, id, status);
        return affectedRows == 1;
    }

    @Override
    public Boolean updateCurrTimeByMsgId(Byte updatedCurrTime, Long msgId) {
        int affectedRows = msgRetryInfoMapper.updateCurrTimeByMsgId(updatedCurrTime, msgId);
        return affectedRows == 1;
    }

    @Override
    public List<MsgRetryInfo> selectByStatus(Byte status) {
        return msgRetryInfoMapper.selectByStatus(status);
    }
}
