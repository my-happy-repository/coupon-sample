package com.study.couponconsumer.repository;

import com.study.couponconsumer.domain.FailedEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FailedEventRepository extends JpaRepository <FailedEvent, Long> {

}
