package com.mall4j.cloud.common.idempotent.event;

import org.springframework.context.ApplicationEvent;

import java.util.List;

public class MsgEvent extends ApplicationEvent {
    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public MsgEvent(List<Long> source) {
        super(source);
    }

    @Override
    public List<Long> getSource() {
        return (List<Long>) super.getSource();
    }
}
