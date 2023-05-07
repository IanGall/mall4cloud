package com.mall4j.cloud.payment.message;

import lombok.Data;

@Data
public class Demo01Message {

    public static final String TOPIC = "DEMO_01";

    /**
     * 编号
     */
    private Integer id;

    // ... 省略 set/get/toString 方法

}
