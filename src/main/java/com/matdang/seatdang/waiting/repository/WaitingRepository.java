package com.matdang.seatdang.waiting.repository;

import com.matdang.seatdang.waiting.entity.Waiting;
import com.matdang.seatdang.waiting.entity.WaitingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import org.springframework.data.repository.query.Param;

public interface WaitingRepository extends JpaRepository<Waiting, Long> {

    List<Waiting> findAllByStoreId(Long storeId);


    List<Waiting> findAllByStoreIdAndWaitingStatus(Long storeId, WaitingStatus waitingStatus);

    @Query("select w from Waiting w where w.storeId = :storeId and w.waitingStatus in ("
            + "com.matdang.seatdang.waiting.entity.WaitingStatus.SHOP_CANCELED, "
            + "com.matdang.seatdang.waiting.entity.WaitingStatus.NO_SHOW, "
            + "com.matdang.seatdang.waiting.entity.WaitingStatus.CUSTOMER_CANCELED)")
    List<Waiting> findAllByCancelStatus(@Param("storeId") Long storeId);


    @Modifying
    @Query("update Waiting w set w.waitingNumber = w.waitingNumber-1"
            + " where w.waitingStatus = com.matdang.seatdang.waiting.entity.WaitingStatus.WAITING"
            + " and w.storeId = :storeId")
    int updateAllWaitingNumberByVisit(@Param("storeId") Long storeId);

    @Modifying
    @Query("update Waiting w set w.waitingNumber = w.waitingNumber-1"
            + " where w.waitingStatus  = com.matdang.seatdang.waiting.entity.WaitingStatus.WAITING"
            + " and w.storeId = :storeId"
            + " and w.waitingNumber > :waitingNumber")
    int updateWaitingNumberByCancel(@Param("storeId") Long storeId, @Param("waitingNumber") Long waitingNumber);

    @Modifying
    @Query("update Waiting w set w.waitingStatus = :waitingStatus,"
            + " w.visitedTime = CASE WHEN :waitingStatus = com.matdang.seatdang.waiting.entity.WaitingStatus.VISITED"
            + " THEN CURRENT_TIMESTAMP ELSE w.visitedTime END"
            + " where w.id = :id")
    int updateStatus(@Param("waitingStatus") WaitingStatus waitingStatus, @Param("id") Long id);
}
