package com.matdang.seatdang.payment.repository;

import com.matdang.seatdang.payment.entity.PayApprove;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PayApproveRepository extends JpaRepository<PayApprove, String> {

}
