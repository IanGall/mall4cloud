package com.mall4j.cloud.common.idempotent.model;

import com.mall4j.cloud.common.model.BaseModel;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 消息重试表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class MsgRetryInfo extends BaseModel {
    public MsgRetryInfo(String messageBody, String className, String bizHandlerName) {
        this.messageBody = messageBody;
        this.className = className;
        this.bizHandlerName = bizHandlerName;
    }

    public MsgRetryInfo(Long msgId, String messageBody, String className, String bizHandlerName) {
        this.msgId = msgId;
        this.messageBody = messageBody;
        this.className = className;
        this.bizHandlerName = bizHandlerName;
    }

    /**
     * 消息id
     */
    private Long msgId;

    /**
     * 生产者名称
     */
    private String templateName;

    /**
     * topic
     */
    private String messageTopic;

    /**
     * 消息体
     */
    private String messageBody;

    /**
     * 消息体类名
     */
    private String className;

    /**
     * 业务处理对象名
     */
    private String bizHandlerName;

    /**
     * 消费次数
     */
    private Byte currTime;

    /**
     * 最大消费次数
     */
    private Byte maxTime;

    /**
     * 消息超时时间
     */
    private Integer timeout;

    /**
     * 消息延时等级
     */
    private Byte delayLevel;

    /**
     * 消息状态，0-未处理，1-已处理
     */
    private Byte status;

    /**
     * 处理失败原因
     */
    private String errorMsg;
}
