package com.matdang.seatdang.waiting.repository;

import com.matdang.seatdang.waiting.entity.Waiting;
import com.matdang.seatdang.waiting.entity.WaitingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WaitingRepository extends JpaRepository<Waiting, Long> {

    List<Waiting> findAllByStoreId(Long storeId);

    List<Waiting> findAllByStoreIdAndWaitingStatus(Long storeId, WaitingStatus waitingStatus);
}
