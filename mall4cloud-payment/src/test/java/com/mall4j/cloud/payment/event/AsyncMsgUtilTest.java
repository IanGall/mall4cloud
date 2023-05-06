package com.mall4j.cloud.payment.event;

import cn.hutool.json.JSONUtil;
import com.mall4j.cloud.common.cloud.test.BaseSpringBootTest;
import com.mall4j.cloud.common.idempotent.event.AsyncMsgUtil;
import com.mall4j.cloud.common.idempotent.event.MsgEvent;
import com.mall4j.cloud.common.order.bo.PayNotifyBO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Arrays;

@Slf4j
public class AsyncMsgUtilTest extends BaseSpringBootTest {

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
