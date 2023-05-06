package com.mall4j.cloud.common.idempotent.event;

import com.mall4j.cloud.common.util.SpringContextUtils;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;

public class AsyncMsgUtil {

    private static final ApplicationEventPublisher eventPublisher = SpringContextUtils.getApplicationContext();

    public static void pubMsgEvent(MsgEvent event) {
        eventPublisher.publishEvent(event);
    }

    public static void pubEvent(ApplicationEvent event) {
        eventPublisher.publishEvent(event);
    }

}
