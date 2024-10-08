package com.study.couponapi.service;

import com.study.couponapi.domain.Coupon;
import com.study.couponapi.producer.CouponCreateProducer;
import com.study.couponapi.repository.AppliedUserRepository;
import com.study.couponapi.repository.CouponCountRepository;
import com.study.couponapi.repository.CouponRepository;
import org.springframework.stereotype.Service;

@Service
public class ApplyService {

    private final CouponRepository couponRepository;

    private final CouponCountRepository couponCountRepository;

    private final CouponCreateProducer couponCreateProducer;

    private final AppliedUserRepository appliedUserRepository;

    public ApplyService(CouponRepository couponRepository, CouponCountRepository couponCountRepository, CouponCreateProducer couponCreateProducer, AppliedUserRepository appliedUserRepository) {
        this.couponRepository = couponRepository;
        this.couponCountRepository = couponCountRepository;
        this.couponCreateProducer = couponCreateProducer;
        this.appliedUserRepository = appliedUserRepository;
    }

    public void apply(Long userId) {
        // lock start
        // 쿠폰 발급 여부
        // if (발급 되었다면) 여부
        long apply = appliedUserRepository.add(userId);
        // 이미 값이 들어가 있으면 이전에 신청한 유저 인거임 !
        if (apply != 1) {
            return;
        }

        // 레이스 컨디션이 발생함
        // 레이스 컨디션 - 두 개의 스레드에서 공유되는 자원에 접근 시 발생
        // synchronized 를 사용 시 해결 되지만 서버가 여러개 되면 레이스 컨디션이 다시 발생
        // 핵심은 Count 에 대한 정합성임

        // DB 를 사용하여 Count 를 확인시에는 Count 수가 맞지 않음 !
        // Redis 를 사용하면 정합성이 딱 맞음 100 개로 !!
        // long count = couponRepository.count();
        long count = couponCountRepository.increment();

        if (count > 100) {
            return;
        }

        // TODO - Redis 연결 제대로 하기 !
        // 만약 쿠폰 발급 갯수가 늘어나면 RDB 에 부하가 걸림
        // NGrinder 로 실습 시 DB 에 부하가 많이 걸려 에러가 발생하게 됨 !
//        couponRepository.save(
//            new Coupon(userId)
//        );

//         직접 생성을 하는 것이아닌 Kafka 를 통하여 전달
        couponCreateProducer.create(userId);
    }
}

