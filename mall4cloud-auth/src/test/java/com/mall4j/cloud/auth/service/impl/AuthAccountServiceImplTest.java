package com.mall4j.cloud.auth.service.impl;


import com.mall4j.cloud.common.cache.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
// @RequiredArgsConstructor
@Slf4j
public class AuthAccountServiceImplTest{

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired // <1>
    private RedissonClient redissonClient;

    @Test
    public void test() {
        Boolean res = redisTemplate.opsForValue().setIfAbsent("1", "test");
        log.info("res: {}",res);
    }

    @Test
    public void test2() {
        RLock lock = redissonClient.getLock("lockTest");
        try {
            lock.lock();
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
        // log.info("res: {}",res);
    }

    @Test
    public void test3() {
        RLock lock = redissonClient.getLock("lockTest");
        try {
            lock.lock();
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
        // log.info("res: {}",res);
    }
}
