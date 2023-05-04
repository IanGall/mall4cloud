package com.mall4j.cloud.common.rocketmq.service.impl;

import com.mall4j.cloud.common.rocketmq.constant.MsgStatusCst;
import com.mall4j.cloud.common.rocketmq.mapper.MsgInfoMapper;
import com.mall4j.cloud.common.rocketmq.model.MsgInfo;
import com.mall4j.cloud.common.rocketmq.service.MsgInfoService;
import com.mall4j.cloud.common.util.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class MsgInfoServiceImpl implements MsgInfoService {

    @Resource
    private MsgInfoMapper msgInfoMapper;

    @Override
    public int deleteByPrimaryKey(Long msgId) {
        return msgInfoMapper.deleteByPrimaryKey(msgId);
    }

    @Override
    public int insert(MsgInfo record) {
        return msgInfoMapper.insert(record);
    }

    @Override
    public int insertSelective(MsgInfo record) {
        return msgInfoMapper.insertSelective(record);
    }

    @Override
    public MsgInfo selectByPrimaryKey(Long msgId) {
        return msgInfoMapper.selectByPrimaryKey(msgId);
    }

    @Override
    public int updateByPrimaryKeySelective(MsgInfo record) {
        return msgInfoMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(MsgInfo record) {
        return msgInfoMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<MsgInfo> selectByMsgIdIn(Collection<Long> msgIdCollection) {
        return msgInfoMapper.selectByMsgIdIn(msgIdCollection);
    }

    @Override
    public void batchAsyncSend(List<MsgInfo> msgInfos) {
        for (MsgInfo msgInfo : msgInfos) {
            Long msgId = msgInfo.getMsgId();
            RocketMQTemplate mqTemplate = SpringContextUtils.getBean(msgInfo.getTemplateName(), RocketMQTemplate.class);
            SendStatus sendStatus = mqTemplate.syncSend(msgInfo.getMessageTopic(), new GenericMessage<>(msgInfo.getMessageBody())
                    , msgInfo.getTimeout(), msgInfo.getDelayLevel()).getSendStatus();
            log.info("msgId: {}, sendStatus: {}", msgId,sendStatus);
            if (SendStatus.SEND_OK.equals(sendStatus)) {
                // 成功修改状态
                msgInfoMapper.updateStatusByMsgIdAndStatus(MsgStatusCst.SENDED, msgId,MsgStatusCst.NOT_SENDED);
            }
        }
    }

    @Override
    public List<MsgInfo> selectByMsgIdInAndStatus(Collection<Long> msgIdCollection, Byte status) {
        return msgInfoMapper.selectByMsgIdInAndStatus(msgIdCollection, status);
    }

    @Override
    public List<MsgInfo> selectByStatus(Byte status) {
        return msgInfoMapper.selectByStatus(status);
    }

    @Override
    public int updateStatusByMsgIdAndStatus(Byte updatedStatus, Long msgId, Byte status) {
        return msgInfoMapper.updateStatusByMsgIdAndStatus(updatedStatus, msgId, status);
    }
}
