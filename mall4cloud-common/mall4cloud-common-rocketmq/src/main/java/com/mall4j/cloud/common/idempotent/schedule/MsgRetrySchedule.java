package com.mall4j.cloud.common.idempotent.schedule;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.mall4j.cloud.common.idempotent.constant.MsgStatusCst;
import com.mall4j.cloud.common.idempotent.handler.MsgBizHandler;
import com.mall4j.cloud.common.idempotent.model.MsgInfo;
import com.mall4j.cloud.common.idempotent.model.MsgRetryInfo;
import com.mall4j.cloud.common.idempotent.service.MsgInfoService;
import com.mall4j.cloud.common.idempotent.service.MsgRetryInfoService;
import com.mall4j.cloud.common.util.SpringContextUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * 定时任务补偿，频次不要太高
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class MsgRetrySchedule {

    private final MsgRetryInfoService msgRetryInfoService;

    @Scheduled(cron = "0/10 * * * * ?")
    public void scheduleScanLocalMessage() throws ClassNotFoundException {
        log.info("扫描消息表开始...."+System.currentTimeMillis());

        // 查询所有等待发送的消息
        List<MsgRetryInfo> retries = msgRetryInfoService.selectByStatus(MsgStatusCst.NOT_SENDED);

        for (MsgRetryInfo retryInfo : retries) {
            MsgBizHandler handler = SpringContextUtils.getBean(retryInfo.getBizHandlerName(), MsgBizHandler.class);
            handler.doHandle(JSONUtil.toBean(retryInfo.getMessageBody(),Class.forName(retryInfo.getClassName())));
        }
    }
}
