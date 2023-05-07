package com.mall4j.cloud.common.idempotent.service.impl;

import com.mall4j.cloud.common.idempotent.model.IdempotentInfo;
import com.mall4j.cloud.common.idempotent.model.MsgRetryInfo;
import com.mall4j.cloud.common.idempotent.service.IdempotentInfoService;
import com.mall4j.cloud.common.idempotent.service.MqIdempotentManagerService;
import com.mall4j.cloud.common.idempotent.service.MsgRetryInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MqIdempotentManagerServiceImpl implements MqIdempotentManagerService {
    @Autowired
    private IdempotentInfoService idempotentInfoService;
    @Autowired
    private MsgRetryInfoService msgRetryInfoService;

    @Transactional
    public void mqIdempotentInsert(IdempotentInfo idempotentInfo, MsgRetryInfo record) {
        idempotentInfoService.insertSelective(idempotentInfo);
        msgRetryInfoService.insertSelective(record);
    }
}
