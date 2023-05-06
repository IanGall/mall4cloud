package com.mall4j.cloud.common.idempotent.constant;

public class IdempotentCst {

    /**
     * 本地消息表消息状态 0-未处理，1-处理中, 2-已处理
     */
    public static final byte NOT_HANDLED = 0;
    public static final byte IN_HANDLED = 1;
    public static final byte HANDLED = 2;

}
