package com.mall4j.cloud.common.idempotent.model;

import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 本地消息表
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MsgInfo extends BaseModel {
    public MsgInfo(String templateName, String messageTopic, String messageBody) {
        this.templateName = templateName;
        this.messageTopic = messageTopic;
        this.messageBody = messageBody;
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
     * 消息超时时间
     */
    private Integer timeout;

    /**
     * 消息延时等级
     */
    private Byte delayLevel;

    /**
     * 消息状态，0-未发送，1-已发送
     */
    private Byte status;

    /**
     * 失败原因
     */
    private String errorMsg;

}
