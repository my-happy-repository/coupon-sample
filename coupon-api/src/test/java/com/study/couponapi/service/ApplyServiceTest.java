package com.study.couponapi.service;

import com.study.couponapi.repository.CouponRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
public class ApplyServiceTest {

    @Autowired
    private ApplyService applyService;

    @Autowired
    private CouponRepository couponRepository;

    @Test
    @Transactional
    public void 한번만응모 () {
        long beforeCount = couponRepository.count();
        applyService.apply(1l);

        long count = couponRepository.count();
        Assertions.assertEquals(beforeCount + 1, count);
    }

    @Test
    public void 여러명응모() throws InterruptedException {
        long originalCount = couponRepository.count();

        int threadCount = 1;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        // CountDownLatch 는 다른 스레드에서 수행하고 있는 작업을 기다려 주는 클래스
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            long userId = i;
            executorService.submit(() -> {
                try {
                    applyService.apply(userId);
                } finally {
                     latch.countDown();
                }
            });
        }

        latch.await();

        long count = couponRepository.count();
        // 테스트 케이스가 실패 함, 실패한 이유는 레이스 컨디션이 실패함
        // 여러개 스레드가 공유 되는 자원에 접근하려고 할 때 레이스 컨디션이 발생함

        // Consumer 에서 다 저장하기 떄 까지 기다리기 위하여 Thread Sleep 을 걸어 줌
        Thread.sleep(10000);

        // Redis 로 변경을 하여 테스트에 성공함
        Assertions.assertEquals(originalCount + 100, count);
    }
}
