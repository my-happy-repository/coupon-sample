package com.study.couponconsumer.consumer;

import com.study.couponconsumer.domain.Coupon;
import com.study.couponconsumer.domain.FailedEvent;
import com.study.couponconsumer.repository.CouponRepository;
import com.study.couponconsumer.repository.FailedEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CouponCreatedConsumer {

    private final CouponRepository couponRepository;

    private final FailedEventRepository failedEventRepository;

    private final Logger logger = LoggerFactory.getLogger(CouponCreatedConsumer.class);

    public CouponCreatedConsumer(CouponRepository couponRepository, FailedEventRepository failedEventRepository) {
        this.couponRepository = couponRepository;
        this.failedEventRepository = failedEventRepository;
    }

    // Consumer 에서 만약 에러가 발생하였을 가능성을 대비해서 처리를 해주어야 함 !
    // 만약 Consumer 에서 에러가 발생을 하면 결과적으로 Count 만 올라가므로 발행하려는 쿠폰보다 적은 쿠폰을 발급하게 됨 !
    @KafkaListener(topics = "coupon_create", groupId = "group_1")
    public void listener(Long userId) {
        try {
            System.out.println("[THIS IS KAFKA LISTENER USER ID ] : " + userId);
            couponRepository.save(new Coupon(userId));
        } catch (Exception e) {
            logger.error("Kafka Consumer Error Message, UserID : {} , {}", e.getMessage(), userId);
            // Consumer 에서 쿠폰 발급에 실패한 UserId 는 DB 에 따로 데이터를 넣어줌
            failedEventRepository.save(new FailedEvent(userId));
        }
    }
}
