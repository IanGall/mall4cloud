package com.mall4j.cloud.common.idempotent.schedule;

import com.mall4j.cloud.common.idempotent.constant.MsgStatusCst;
import com.mall4j.cloud.common.idempotent.model.MsgInfo;
import com.mall4j.cloud.common.idempotent.service.MsgInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * 定时任务补偿，频次不要太高，主要是为了补偿发 MQ 失败的
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class MsgSchedule {

    private final MsgInfoService msgInfoService;

    @Scheduled(cron = "0/10 * * * * ?")
    public void scheduleScanLocalMessage(){
        log.info("扫描消息表开始...."+System.currentTimeMillis());

        // 查询所有等待发送的消息
        List<MsgInfo> msgInfos = msgInfoService.selectByStatus(MsgStatusCst.NOT_SENDED);

        msgInfoService.batchAsyncSend(msgInfos);
    }
}
