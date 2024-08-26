package com.matdang.seatdang.waiting.repository.query;

import com.matdang.seatdang.waiting.entity.WaitingStatus;
import com.matdang.seatdang.waiting.entity.WaitingStorage;
import com.matdang.seatdang.waiting.repository.query.dto.WaitingInfoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WaitingStorageQueryRepository extends JpaRepository<WaitingStorage, Long> {
//    @Query("select new com.matdang.seatdang.waiting.repository.query.dto.WaitingInfoDto("
//            + " w.id, s.storeName, w.waitingNumber, w.customerInfo.peopleCount, w.waitingStatus)"
//            + " from WaitingStorage w"
//            + " join Store s on w.storeId = s.storeId"
//            + " where w.customerInfo.customerId = :customerId"
//            + " and w.waitingStatus = :waitingStatus")
//    Page<WaitingInfoDto> findAllByCustomerIdAndWaitingStatus(@Param("customerId") Long customerId,
//                                                             @Param("waitingStatus") WaitingStatus waitingStatus,
//                                                             Pageable pageable);

//    @Query("select new com.matdang.seatdang.waiting.repository.query.dto.WaitingInfoDto("
//            + " w.id, s.storeName, w.waitingNumber, w.customerInfo.peopleCount, w.waitingStatus)"
//            + " from WaitingStorage w"
//            + " join Store s on w.storeId = s.storeId"
//            + " where w.customerInfo.customerId = :customerId"
//            + " and w.waitingStatus in ("
//            + " com.matdang.seatdang.waiting.entity.WaitingStatus.SHOP_CANCELED,"
//            + " com.matdang.seatdang.waiting.entity.WaitingStatus.NO_SHOW,"
//            + " com.matdang.seatdang.waiting.entity.WaitingStatus.CUSTOMER_CANCELED)")
//    Page<WaitingInfoDto> findAllByCustomerIdAndCancelStatus(@Param("customerId") Long customerId,
//                                                            Pageable pageable);

    @Query("select count(w)"
            + " from WaitingStorage w"
            + " where w.customerInfo.customerId = :customerId"
            + " and w.waitingStatus = :waitingStatus")
    int countWaitingStorageByCustomerIdAndWaitingStatus(@Param("customerId") Long customerId,
                                                        @Param("waitingStatus") WaitingStatus waitingStatus);

    @Query("select count(w)"
            + " from WaitingStorage w"
            + " where w.customerInfo.customerId = :customerId"
            + " and w.waitingStatus in ("
            + " com.matdang.seatdang.waiting.entity.WaitingStatus.SHOP_CANCELED,"
            + " com.matdang.seatdang.waiting.entity.WaitingStatus.NO_SHOW,"
            + " com.matdang.seatdang.waiting.entity.WaitingStatus.CUSTOMER_CANCELED)")
    int countWaitingStorageByCustomerIdAndCancelStatus(@Param("customerId") Long customerId);
}
