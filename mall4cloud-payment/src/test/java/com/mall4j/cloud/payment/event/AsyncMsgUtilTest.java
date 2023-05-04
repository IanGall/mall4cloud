package com.mall4j.cloud.payment.event;

import com.mall4j.cloud.common.cloud.test.BaseSpringBootTest;
import com.mall4j.cloud.common.rocketmq.event.AsyncMsgUtil;
import com.mall4j.cloud.common.rocketmq.event.MsgEvent;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

public class AsyncMsgUtilTest extends BaseSpringBootTest {

    @Test
    @Transactional
    public void test() throws InterruptedException {
        // Thread.sleep(1000L);
        AsyncMsgUtil.pubMsgEvent(new MsgEvent(Arrays.asList(1L, 2L)));
        // RedisUtil.del("1");
        // eventPublisher.publishEvent(new MsgEvent(Arrays.asList(1L, 2L)));
    }
}
