package com.mall4j.cloud.common.idempotent.service.impl;

import com.mall4j.cloud.common.idempotent.constant.MsgStatusCst;
import com.mall4j.cloud.common.idempotent.mapper.MsgInfoMapper;
import com.mall4j.cloud.common.idempotent.model.MsgInfo;
import com.mall4j.cloud.common.idempotent.service.MsgInfoService;
import com.mall4j.cloud.common.util.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class MsgInfoServiceImpl implements MsgInfoService {

    @Resource
    private MsgInfoMapper msgInfoMapper;

    private final ExecutorService asyncSendService = new ThreadPoolExecutor(5, 10, 60L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(200), new MqAsyncSendThreadFactory());

    public static class MqAsyncSendThreadFactory implements ThreadFactory {

        private static final AtomicInteger threadInitNumber = new AtomicInteger(0);

        private static int nextThreadNum() {
            return threadInitNumber.incrementAndGet();
        }

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "Thread-Mq-Async-Send-" + nextThreadNum());
        }

    }

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
            // SendStatus sendStatus = mqTemplate.syncSend(msgInfo.getMessageTopic(), new GenericMessage<>(msgInfo.getMessageBody())
            //         , msgInfo.getTimeout(), msgInfo.getDelayLevel()).getSendStatus();
            CompletableFuture.supplyAsync(() -> mqTemplate.syncSend(msgInfo.getMessageTopic(), new GenericMessage<>(msgInfo.getMessageBody())
                            , msgInfo.getTimeout(), msgInfo.getDelayLevel()).getSendStatus(), asyncSendService)
                    .thenAccept(sendStatus -> {
                        log.info("msgId: {}, sendStatus: {}", msgId, sendStatus);
                        if (SendStatus.SEND_OK.equals(sendStatus)) {
                            // 成功修改状态
                            msgInfoMapper.updateStatusByMsgIdAndStatus(MsgStatusCst.SENDED, msgId, MsgStatusCst.NOT_SENDED);
                        }
                    });

            // log.info("msgId: {}, sendStatus: {}", msgId,sendStatus);
            // if (SendStatus.SEND_OK.equals(sendStatus)) {
            //     // 成功修改状态
            //     msgInfoMapper.updateStatusByMsgIdAndStatus(MsgStatusCst.SENDED, msgId,MsgStatusCst.NOT_SENDED);
            // }
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
