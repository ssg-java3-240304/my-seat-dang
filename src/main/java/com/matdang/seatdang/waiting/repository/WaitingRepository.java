package com.matdang.seatdang.waiting.repository;

import com.matdang.seatdang.dashboard.dto.WaitingDashboardResponseDto;
import com.matdang.seatdang.waiting.entity.Waiting;
import com.matdang.seatdang.waiting.entity.WaitingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.repository.query.Param;

public interface WaitingRepository extends JpaRepository<Waiting, Long> {

    List<Waiting> findAllByStoreId(Long storeId);

    @Query("SELECT p FROM Waiting p WHERE p.storeId = :storeId and p.waitingStatus = :status AND p.visitedTime BETWEEN :start AND :end")
    List<Waiting> findVisitedWithinCertainPeriod(@Param("storeId") Long storeId,
                                                 @Param("start") LocalDateTime start,
                                                 @Param("end") LocalDateTime end,
                                                 @Param("status") WaitingStatus status);

//    @Query("""
//            SELECT NEW com.matdang.seatdang.dashboard.dto.WaitingDashboardResponseDto(COUNT(w), FUNCTION('DATE_FORMAT', w.visitedTime, '%Y-%m-%d'))
//            FROM Waiting w
//            WHERE w.storeId = :storeId
//            AND w.visitedTime BETWEEN :start AND :end
//            AND w.waitingStatus = :status
//            GROUP BY DATE(w.visitedTime)
//            ORDER BY DATE(w.visitedTime)
//            """)
//    List<WaitingDashboardResponseDto> findVisitedWithinCertainPeriod(@Param("storeId") Long storeId,
//                                                                     @Param("start") LocalDateTime start,
//                                                                     @Param("end") LocalDateTime end,
//                                                                     @Param("status") WaitingStatus status);

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
    @Query("delete from Waiting w where w.storeId = :storeId")
    int deleteAllByStoreId(@Param("storeId") Long storeId);

}
