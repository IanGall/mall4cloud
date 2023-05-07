package com.mall4j.cloud.common.idempotent.message;

import lombok.Data;

import java.util.List;

/**
 * 订单支付成功通知
 * @author FrozenWatermelon
 * @date 2020/12/8
 */
@Data
public class PayNotifyBO {

    private Long idempotentId;
    private List<Long> orderIds;
    //
    // public PayNotifyBO(){
    //
    // }
    //
    // public PayNotifyBO(List<Long> orderIds) {
    //     this.orderIds = orderIds;
    // }
    //
    // public List<Long> getOrderIds() {
    //     return orderIds;
    // }
    //
    // public void setOrderIds(List<Long> orderIds) {
    //     this.orderIds = orderIds;
    // }
    //
    // @Override
    // public String toString() {
    //     return "PayNotifyBO{" +
    //             "orderIds=" + orderIds +
    //             '}';
    // }
}
