package com.matdang.seatdang.payment.repository;

import com.matdang.seatdang.payment.entity.PayReady;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// TODO : Test 필요
public interface KakaoPayRepository extends JpaRepository<PayReady, String> {
    @Query("SELECT p.tid FROM PayReady p WHERE p.partnerOrderId = :partnerOrderId AND p.partnerUserId = :partnerUserId")
    String findTidByPartnerOrderIdAndPartnerUserId(@Param("partnerOrderId") String partnerOrderId,
                                                   @Param("partnerUserId") String partnerUserId);
}
