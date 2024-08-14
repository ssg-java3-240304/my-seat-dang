package com.matdang.seatdang.payment.repository;

import com.matdang.seatdang.payment.entity.RefundResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundRepository extends JpaRepository<RefundResult, Long> {

}
