package com.mall4j.cloud.payment.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.mall4j.cloud.api.leaf.feign.SegmentFeignClient;
import com.mall4j.cloud.api.order.feign.OrderFeignClient;
import com.mall4j.cloud.api.order.vo.OrderAmountVO;
import com.mall4j.cloud.common.exception.Mall4cloudException;
import com.mall4j.cloud.common.idempotent.mapper.IdempotentInfoMapper;
import com.mall4j.cloud.common.idempotent.model.IdempotentInfo;
import com.mall4j.cloud.common.idempotent.message.PayNotifyBO;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.idempotent.mapper.MsgInfoMapper;
import com.mall4j.cloud.common.idempotent.model.MsgInfo;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.payment.bo.PayInfoBO;
import com.mall4j.cloud.payment.bo.PayInfoResultBO;
import com.mall4j.cloud.payment.constant.PayStatus;
import com.mall4j.cloud.payment.dto.PayInfoDTO;
import com.mall4j.cloud.common.idempotent.event.AsyncMsgUtil;
import com.mall4j.cloud.common.idempotent.event.MsgEvent;
import com.mall4j.cloud.payment.mapper.PayInfoMapper;
import com.mall4j.cloud.payment.model.PayInfo;
import com.mall4j.cloud.payment.service.PayInfoService;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.mall4j.cloud.common.idempotent.constant.MqConstant.MQTemplateName.ORDER_NOTIFY_TEMPLATE;
import static com.mall4j.cloud.common.idempotent.constant.MqConstant.ORDER_NOTIFY_TOPIC;

/**
 * 订单支付记录
 *
 * @author FrozenWatermelon
 * @date 2020-12-25 09:50:59
 */
@Service
public class PayInfoServiceImpl implements PayInfoService {

    @Autowired
    private MsgInfoMapper msgInfoMapper;

    @Autowired
    private IdempotentInfoMapper idempotentInfoMapper;

    @Autowired
    private PayInfoMapper payInfoMapper;

    @Autowired
    private SegmentFeignClient segmentFeignClient;

    @Autowired
    private OrderFeignClient orderFeignClient;

    @Autowired
    private RocketMQTemplate orderNotifyTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PayInfoBO pay(Long userId, PayInfoDTO payParam) {
        // 支付单号
        ServerResponseEntity<Long> segmentIdResponse = segmentFeignClient.getSegmentId(PayInfo.DISTRIBUTED_ID_KEY);
        if (!segmentIdResponse.isSuccess()) {
            throw new Mall4cloudException(ResponseEnum.EXCEPTION);
        }
        Long payId = segmentIdResponse.getData();
        List<Long> orderIds = payParam.getOrderIds();

        ServerResponseEntity<OrderAmountVO> ordersAmountAndIfNoCancelResponse = orderFeignClient.getOrdersAmountAndIfNoCancel(orderIds);
        // 如果订单已经关闭了，此时不能够支付了
        if (!ordersAmountAndIfNoCancelResponse.isSuccess()) {
            throw new Mall4cloudException(ordersAmountAndIfNoCancelResponse.getMsg());
        }
        OrderAmountVO orderAmount = ordersAmountAndIfNoCancelResponse.getData();
        PayInfo payInfo = new PayInfo();
        payInfo.setPayId(payId);
        payInfo.setUserId(userId);
        payInfo.setPayAmount(orderAmount.getPayAmount());
        payInfo.setPayStatus(PayStatus.UNPAY.value());
        payInfo.setSysType(AuthUserContext.get().getSysType());
        payInfo.setVersion(0);
        // 保存多个支付订单号
        payInfo.setOrderIds(StrUtil.join(StrUtil.COMMA, orderIds));
        // 保存预支付信息
        payInfoMapper.save(payInfo);
        PayInfoBO payInfoDto = new PayInfoBO();
        payInfoDto.setBody("商城订单");
        payInfoDto.setPayAmount(orderAmount.getPayAmount());
        payInfoDto.setPayId(payId);
        return payInfoDto;
    }

    @Override
    public PayInfo getByPayId(Long payId) {
        return payInfoMapper.getByPayId(payId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void paySuccess(PayInfoResultBO payInfoResult, List<Long> orderIds) {
        // 标记为支付成功状态
        PayInfo payInfo = new PayInfo();
        payInfo.setPayId(payInfoResult.getPayId());
        payInfo.setBizPayNo(payInfoResult.getBizPayNo());
        payInfo.setCallbackContent(payInfoResult.getCallbackContent());
        payInfo.setCallbackTime(new Date());
        payInfo.setPayStatus(PayStatus.PAYED.value());
        payInfoMapper.update(payInfo);
        // // 发送消息，订单支付成功
        // SendStatus sendStatus = orderNotifyTemplate.syncSend(ORDER_NOTIFY_TOPIC, new GenericMessage<>(new PayNotifyBO(orderIds))).getSendStatus();
        // if (!Objects.equals(sendStatus, SendStatus.SEND_OK)) {
        //     // 消息发不出去就抛异常，因为订单回调会有多次，几乎不可能每次都无法发送出去，发的出去无所谓因为接口是幂等的
        //     throw new Mall4cloudException(ResponseEnum.EXCEPTION);
        // }

        //幂等记录
        ServerResponseEntity<Long> response = segmentFeignClient.getSegmentId("mall4cloud-idempotent");

        IdempotentInfo idempotentInfo = new IdempotentInfo();
        Long id = response.getData();
        idempotentInfo.setId(id);
        idempotentInfo.setStatus((byte) 0);
        idempotentInfoMapper.insertSelective(idempotentInfo);

        // 本地消息表记录
        PayNotifyBO notifyBO = new PayNotifyBO();
        notifyBO.setIdempotentId(id);
        notifyBO.setOrderIds(orderIds);
        MsgInfo msgInfo = new MsgInfo(ORDER_NOTIFY_TEMPLATE, ORDER_NOTIFY_TOPIC, JSONUtil.toJsonStr(notifyBO),notifyBO.getClass().getName());
        // msgInfo.setTemplateName();
        msgInfoMapper.insertSelective(msgInfo);
        AsyncMsgUtil.pubMsgEvent(new MsgEvent(Arrays.asList(msgInfo.getMsgId())));
    }

    @Override
    public Integer getPayStatusByOrderIds(String orderIds) {
        return payInfoMapper.getPayStatusByOrderIds(orderIds);
    }

    @Override
    public Integer isPay(String orderIds, Long userId) {
        return payInfoMapper.isPay(orderIds, userId);
    }
}
