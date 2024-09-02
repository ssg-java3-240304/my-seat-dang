//package com.matdang.seatdang.waiting.repository;
//
//import com.matdang.seatdang.dashboard.dto.WaitingDashboardResponseDto;
//import com.matdang.seatdang.waiting.entity.Waiting;
//import com.matdang.seatdang.waiting.entity.WaitingStatus;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import org.springframework.data.repository.query.Param;
//import java.util.List;
//
//public interface WaitingRepository extends JpaRepository<Waiting, Long> {
//
//    List<Waiting> findAllByStoreId(Long storeId);
//
//    @Query("SELECT p FROM Waiting p WHERE p.storeId = :storeId and p.waitingStatus = :status AND p.visitedTime BETWEEN :start AND :end")
//    List<Waiting> findVisitedWithinCertainPeriod(@Param("storeId") Long storeId,
//                                                 @Param("start") LocalDateTime start,
//                                                 @Param("end") LocalDateTime end,
//                                                 @Param("status") WaitingStatus status);
//
////    @Query("""
////            SELECT NEW com.matdang.seatdang.dashboard.dto.WaitingDashboardResponseDto(COUNT(w), FUNCTION('DATE_FORMAT', w.visitedTime, '%Y-%m-%d'))
////            FROM Waiting w
////            WHERE w.storeId = :storeId
////            AND w.visitedTime BETWEEN :start AND :end
////            AND w.waitingStatus = :status
////            GROUP BY DATE(w.visitedTime)
////            ORDER BY DATE(w.visitedTime)
////            """)
////    List<WaitingDashboardResponseDto> findVisitedWithinCertainPeriod(@Param("storeId") Long storeId,
////                                                                     @Param("start") LocalDateTime start,
////                                                                     @Param("end") LocalDateTime end,
////                                                                     @Param("status") WaitingStatus status);
//
//    @Modifying
//    @Query("update Waiting w set w.waitingOrder = w.waitingOrder-1"
//            + " where w.waitingStatus = com.matdang.seatdang.waiting.entity.WaitingStatus.WAITING"
//            + " and w.storeId = :storeId")
//    int updateAllWaitingOrderByVisit(@Param("storeId") Long storeId);
//
//    @Modifying
//    @Query("update Waiting w set w.waitingOrder = w.waitingOrder-1"
//            + " where w.waitingStatus  = com.matdang.seatdang.waiting.entity.WaitingStatus.WAITING"
//            + " and w.storeId = :storeId"
//            + " and w.waitingOrder > :waitingOrder")
//    int updateWaitingOrderByCancel(@Param("storeId") Long storeId, @Param("waitingOrder") Long waitingOrder);
//
//    @Modifying
//    @Query("update Waiting w set w.waitingStatus =  com.matdang.seatdang.waiting.entity.WaitingStatus.VISITED,"
//            + " w.visitedTime = CURRENT_TIMESTAMP"
//            + " where w.id = :id")
//    int updateStatusByVisit(@Param("id") Long id);
//
//    @Modifying
//    @Query("update Waiting w set w.waitingStatus =  com.matdang.seatdang.waiting.entity.WaitingStatus.SHOP_CANCELED,"
//            + " w.canceledTime = CURRENT_TIMESTAMP"
//            + " where w.id = :id")
//    int updateStatusByShopCancel(@Param("id") Long id);
//
//    @Modifying
//    @Query("update Waiting w"
//            + " set w.waitingStatus = com.matdang.seatdang.waiting.entity.WaitingStatus.SHOP_CANCELED,"
//            + " w.canceledTime = CURRENT_TIMESTAMP"
//            + " where w.waitingStatus = com.matdang.seatdang.waiting.entity.WaitingStatus.WAITING"
//            + " and w.storeId = :storeId")
//    int cancelAllWaiting(@Param("storeId") Long storeId);
//
//
//    @Modifying
//    @Query("delete from Waiting w"
//            + " where w.storeId = :storeId")
//    int deleteAllByStoreId(@Param("storeId") Long storeId);
//
//    // COALESCE == IFNULL, ifnull도 가능
//    @Query("select COALESCE(max(w.waitingNumber), 0)"
//            + " from Waiting w"
//            + " where w.storeId = :storeId")
//    Long findMaxWaitingNumberByStoreId(@Param("storeId") Long storeId);
//
//    @Query("select COALESCE(max(w.waitingOrder), 0)"
//            + " from Waiting w"
//            + " where w.storeId = :storeId"
//            + " and w.waitingStatus = com.matdang.seatdang.waiting.entity.WaitingStatus.WAITING")
//    Long findMaxWaitingOrderByStoreId(@Param("storeId") Long storeId);
//
//    @Query("select w"
//            + " from Waiting w"
//            + " where w.customerInfo.customerId = :customerId")
//    List<Waiting> findAllByCustomerId(@Param("customerId") Long customerId);
//
//    @Query("select COUNT(w)"
//            + " from Waiting w"
//            + " where w.storeId= :storeId"
//            + " and w.waitingStatus = com.matdang.seatdang.waiting.entity.WaitingStatus.WAITING")
//    Integer countWaitingByStoreIdAndWaitingStatus(@Param("storeId") Long storeId);
//
//    @Modifying(clearAutomatically = true)
//    @Query("update Waiting w"
//            + " set w.waitingStatus = com.matdang.seatdang.waiting.entity.WaitingStatus.CUSTOMER_CANCELED,"
//            + " w.canceledTime = CURRENT_TIMESTAMP"
//            + " where w.id = :id")
//    int cancelWaitingByCustomer(@Param("id") Long id);
//
//    @Query("SELECT CASE WHEN COUNT(w) > 0 THEN true ELSE false END"
//            + " FROM Waiting w"
//            + " WHERE w.storeId = :storeId"
//            + " AND w.customerInfo.customerId = :customerId"
//            + " AND w.waitingStatus = com.matdang.seatdang.waiting.entity.WaitingStatus.WAITING")
//    boolean isRegisteredWaiting(@Param("storeId") Long storeId,
//                                @Param("customerId") Long customerId);
//}
