package com.matdang.seatdang.payment.repository;

import com.matdang.seatdang.payment.entity.PayReady;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface KakaoPayRepository extends JpaRepository<PayReady, String> {

}
