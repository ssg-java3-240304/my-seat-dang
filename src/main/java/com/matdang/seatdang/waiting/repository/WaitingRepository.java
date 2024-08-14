package com.matdang.seatdang.waiting.repository;

import com.matdang.seatdang.waiting.entity.Waiting;
import com.matdang.seatdang.waiting.entity.WaitingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WaitingRepository extends JpaRepository<Waiting, Long> {

    List<Waiting> findAllByStoreId(Long storeId);

    List<Waiting> findAllByStoreIdAndWaitingStatus(Long storeId, WaitingStatus waitingStatus);

    @Query("select w from Waiting w where w.storeId = :storeId and w.waitingStatus in (" +
            "com.matdang.seatdang.waiting.entity.WaitingStatus.SHOP_CANCELED, " +
            "com.matdang.seatdang.waiting.entity.WaitingStatus.NO_SHOW, " +
            "com.matdang.seatdang.waiting.entity.WaitingStatus.CUSTOMER_CANCELED)")
    List<Waiting> findAllByCancelStatus(Long storeId);
}
