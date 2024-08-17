package com.matdang.seatdang.store.repository.query.dto;

import com.matdang.seatdang.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StoreQueryRepository extends JpaRepository<Store, Long> {

    @Query("select new com.matdang.seatdang.store.repository.query.dto.AvailableWaitingTime("
            + "s.storeSetting.waitingTime.waitingOpenTime, s.storeSetting.waitingTime.waitingCloseTime)"
            + " from Store s where s.storeId = :storeId")
    AvailableWaitingTime findAvailableWaitingTime(@Param("storeId") Long storeId);
}
