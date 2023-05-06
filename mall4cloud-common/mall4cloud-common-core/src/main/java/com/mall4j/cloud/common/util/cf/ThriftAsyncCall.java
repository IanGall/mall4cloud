package com.mall4j.cloud.common.util.cf;

import com.mall4j.cloud.common.exception.Mall4cloudException;

@FunctionalInterface
public interface ThriftAsyncCall {
    void invoke() throws Mall4cloudException;
}
