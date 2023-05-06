package com.mall4j.cloud.common.idempotent.constant;

public class MsgStatusCst {

    /**
     * 本地消息表消息状态 0-未发送，1-已发送，2-处理中, 3-已处理
     */
    public static final byte NOT_SENDED = 0;
    public static final byte SENDED = 1;
    public static final byte IN_HANDLE = 2;
    public static final byte HANDLED = 3;

}
