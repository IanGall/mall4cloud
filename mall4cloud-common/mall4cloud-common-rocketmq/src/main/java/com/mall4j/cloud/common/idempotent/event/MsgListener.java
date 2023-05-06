package com.mall4j.cloud.common.idempotent.event;

import com.mall4j.cloud.common.idempotent.constant.MsgStatusCst;
import com.mall4j.cloud.common.idempotent.model.MsgInfo;
import com.mall4j.cloud.common.idempotent.service.MsgInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class MsgListener {

    private final MsgInfoService msgInfoService;
    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT,
            classes = MsgEvent.class)
    public void onEvent(MsgEvent demoEvent) {
        List<Long> source = demoEvent.getSource();
        log.info("收到事件，事件源是：{}", source);
        // todo 事务提交后的业务处理
        List<MsgInfo> msgInfos = msgInfoService.selectByMsgIdInAndStatus(source, MsgStatusCst.NOT_SENDED);
        msgInfoService.batchAsyncSend(msgInfos);
    }
}
