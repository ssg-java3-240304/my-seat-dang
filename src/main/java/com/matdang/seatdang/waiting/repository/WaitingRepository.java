package com.matdang.seatdang.waiting.repository;

import com.matdang.seatdang.waiting.entity.Waiting;
import com.matdang.seatdang.waiting.entity.WaitingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface WaitingRepository extends JpaRepository<Waiting, Long> {

    List<Waiting> findAllByStoreId(Long storeId);

    @Modifying
    @Query("update Waiting w set w.waitingOrder = w.waitingOrder-1"
            + " where w.waitingStatus = com.matdang.seatdang.waiting.entity.WaitingStatus.WAITING"
            + " and w.storeId = :storeId")
    int updateAllWaitingNumberByVisit(@Param("storeId") Long storeId);

    @Modifying
    @Query("update Waiting w set w.waitingOrder = w.waitingOrder-1"
            + " where w.waitingStatus  = com.matdang.seatdang.waiting.entity.WaitingStatus.WAITING"
            + " and w.storeId = :storeId"
            + " and w.waitingOrder > :waitingNumber")
    int updateWaitingNumberByCancel(@Param("storeId") Long storeId, @Param("waitingNumber") Long waitingNumber);

    @Modifying
    @Query("update Waiting w set w.waitingStatus = :waitingStatus,"
            + " w.visitedTime = CASE WHEN :waitingStatus = com.matdang.seatdang.waiting.entity.WaitingStatus.VISITED"
            + " THEN CURRENT_TIMESTAMP ELSE w.visitedTime END"
            + " where w.id = :id")
    int updateStatus(@Param("waitingStatus") WaitingStatus waitingStatus, @Param("id") Long id);

    @Modifying
    @Query("update Waiting w"
            + " set w.waitingStatus = com.matdang.seatdang.waiting.entity.WaitingStatus.SHOP_CANCELED"
            + " where w.waitingStatus = com.matdang.seatdang.waiting.entity.WaitingStatus.WAITING"
            + " and w.storeId = :storeId")
    int cancelAllWaiting(@Param("storeId") Long storeId);


    @Modifying
    @Query("delete from Waiting w"
            + " where w.storeId = :storeId")
    int deleteAllByStoreId(@Param("storeId") Long storeId);

    // COALESCE == IFNULL, ifnull도 가능
    @Query("select COALESCE(max(w.waitingNumber), 0)"
            + " from Waiting w"
            + " where w.storeId = :storeId")
    Long findMaxWaitingNumberByStoreId(@Param("storeId") Long storeId);

    @Query("select COALESCE(max(w.waitingOrder), 0)"
            + " from Waiting w"
            + " where w.storeId = :storeId"
            + " and w.waitingStatus = com.matdang.seatdang.waiting.entity.WaitingStatus.WAITING")
    Long findMaxWaitingOrderByStoreId(@Param("storeId") Long storeId);

    @Query("select w"
            + " from Waiting w"
            + " where w.customerInfo.customerId = :customerId")
    List<Waiting> findAllByCustomerId(@Param("customerId") Long customerId);

    @Query("select COUNT(w)"
            + " from Waiting w"
            + " where w.storeId= :storeId"
            + " and w.waitingStatus = com.matdang.seatdang.waiting.entity.WaitingStatus.WAITING")
    Integer countWaitingByStoreIdAndWaitingStatus(@Param("storeId") Long storeId);

    @Modifying
    @Transactional
    @Query("update Waiting w"
            + " set w.waitingStatus = com.matdang.seatdang.waiting.entity.WaitingStatus.CUSTOMER_CANCELED"
            + " where w.id = :id")
    int cancelWaitingByCustomer(@Param("id") Long id);
}
