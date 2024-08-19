package com.matdang.seatdang.store.repository.query.dto;

import com.matdang.seatdang.store.entity.Store;
import com.matdang.seatdang.store.vo.WaitingStatus;
import java.time.LocalTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StoreQueryRepository extends JpaRepository<Store, Long> {

    @Query("select new com.matdang.seatdang.store.repository.query.dto.AvailableWaitingTime("
            + "s.storeSetting.waitingTime.waitingOpenTime, s.storeSetting.waitingTime.waitingCloseTime)"
            + " from Store s where s.storeId = :storeId")
    AvailableWaitingTime findAvailableWaitingTime(@Param("storeId") Long storeId);

    @Query("select s.storeSetting.waitingTime.estimatedWaitingTime"
            + " from Store s"
            + " where s.storeId = :storeId")
    LocalTime findEstimatedWaitingTime(@Param("storeId") Long storeId);

    @Query("select s.storeSetting.waitingStatus"
            + " from Store s"
            + " where s.storeId = :storeId")
    WaitingStatus findWaitingStatus(@Param("storeId") Long storeId);

    @Query("select s.storeSetting.waitingPeopleCount"
            + " from Store s"
            + " where s.storeId = :storeId")
    Integer findWaitingPeopleCount(@Param("storeId") Long storeId);

}
