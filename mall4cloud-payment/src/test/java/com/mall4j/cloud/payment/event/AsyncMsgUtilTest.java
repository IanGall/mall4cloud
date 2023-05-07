package com.mall4j.cloud.payment.event;

import cn.hutool.json.JSONUtil;
import com.mall4j.cloud.common.cloud.test.BaseSpringBootTest;
import com.mall4j.cloud.common.idempotent.event.AsyncMsgUtil;
import com.mall4j.cloud.common.idempotent.event.MsgEvent;
import com.mall4j.cloud.common.idempotent.message.PayNotifyBO;
import com.mall4j.cloud.payment.producer.Demo01Producer;
import org.junit.Test;
import org.springframework.kafka.support.SendResult;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

public class AsyncMsgUtilTest extends BaseSpringBootTest {
    @Resource
    private Demo01Producer producer;

    @Test
    public void testSyncSend() throws ExecutionException, InterruptedException {
        int id = (int) (System.currentTimeMillis() / 1000);
        SendResult result = producer.syncSend(id);
        log.info("[testSyncSend][发送编号：[{}] 发送结果：[{}]]", id, result);

        // 阻塞等待，保证消费
        new CountDownLatch(1).await();
    }
    @Test
    @Transactional
    public void test() throws InterruptedException {
        // Thread.sleep(1000L);
        AsyncMsgUtil.pubMsgEvent(new MsgEvent(Arrays.asList(1L, 2L)));
        // RedisUtil.del("1");
        // eventPublisher.publishEvent(new MsgEvent(Arrays.asList(1L, 2L)));
    }

    @Test
    public void test2() {
        PayNotifyBO bean = JSONUtil.toBean("{\"idempotentId\":1,\"orderIds\":[1568693984]}", PayNotifyBO.class);

        log.info(String.valueOf(bean.getOrderIds().get(0)));
    }
}
