package com.study.couponapi.service;

import com.study.couponapi.domain.Coupon;
import com.study.couponapi.producer.CouponCreateProducer;
import com.study.couponapi.repository.CouponCountRepository;
import com.study.couponapi.repository.CouponRepository;
import org.springframework.stereotype.Service;

@Service
public class ApplyService {

    private final CouponRepository couponRepository;

    private final CouponCountRepository couponCountRepository;

    private final CouponCreateProducer couponCreateProducer;

    public ApplyService(CouponRepository couponRepository, CouponCountRepository couponCountRepository, CouponCreateProducer couponCreateProducer) {
        this.couponRepository = couponRepository;
        this.couponCountRepository = couponCountRepository;
        this.couponCreateProducer = couponCreateProducer;
    }

    public void apply(Long userId) {
        // TODO
        // Kafka / Redis 연결 확인 필요 !

        // 레이스 컨디션이 발생함
        // 레이스 컨디션 - 두 개의 스레드에서 공유되는 자원에 접근 시 발생
        // synchronized 를 사용 시 해결 되지만 서버가 여러개 되면 레이스 컨디션이 다시 발생
        // 핵심은 Count 에 대한 정합성임
        // long count = couponRepository.count();

//        couponCreateProducer.create(userId);

        // Redis 를 이용하면 Single Thread 임

        // DB 를 사용 시 여러 Thread 가 동시 접근 시 값이 맞지 않음 !!
//        long count = couponRepository.count();
        long count = couponCountRepository.increment();

//        if (count > 100) {
//            return;
//        }

        // TODO - Redis 연결 제대로 하기 !
        // 만약 쿠폰 발급 갯수가 늘어나면 RDB 에 부하가 걸림
        // NGrinder 로 실습 시 DB 에 부하가 많이 걸려 에러가 발생하게 됨 !
//        couponRepository.save(
//            new Coupon(userId)
//        );

        // 직접 생성을 하는 것이아닌 Kafka 를 통하여 전달
 //       couponCreateProducer.create(userId);

    }
}

