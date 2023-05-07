package com.mall4j.cloud.common.idempotent.handler;

public interface MsgBizHandler<T> {
    /**
     * 业务处理
     * @param o
     * @return
     */
    boolean doHandle(T o);
}
