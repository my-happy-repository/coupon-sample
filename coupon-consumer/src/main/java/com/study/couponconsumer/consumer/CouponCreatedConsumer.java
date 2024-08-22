package com.study.couponconsumer.consumer;

import com.study.couponconsumer.domain.Coupon;
import com.study.couponconsumer.repository.CouponRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CouponCreatedConsumer {

    private final CouponRepository couponRepository;

    public CouponCreatedConsumer(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @KafkaListener(topics = "coupon_create", groupId = "group_1")
    public void listener(Long userId) {

        System.out.println("[THIS IS KAFKA LISTENER USER ID ] : " + userId);
        couponRepository.save(new Coupon(userId));
    }
}
