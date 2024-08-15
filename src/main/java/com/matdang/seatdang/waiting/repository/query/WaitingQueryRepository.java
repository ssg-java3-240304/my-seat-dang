package com.matdang.seatdang.waiting.repository.query;

import com.matdang.seatdang.waiting.entity.Waiting;
import com.matdang.seatdang.waiting.entity.WaitingStatus;
import com.matdang.seatdang.waiting.repository.query.dto.WaitingDto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WaitingQueryRepository extends JpaRepository<Waiting, Long> {
    @Query("select new com.matdang.seatdang.waiting.repository.query.dto.WaitingDto(w.id, w.waitingNumber,w.waitingOrder,"
            + " w.customerInfo.customerPhone, w.customerInfo.peopleCount, w.waitingStatus, w.createdAt, w.visitedTime)"
            + " from Waiting w"
            + " where w.storeId = :storeId and w.waitingStatus = :waitingStatus")
    List<WaitingDto> findAllByStoreIdAndWaitingStatus(@Param("storeId") Long storeId, @Param("waitingStatus") WaitingStatus waitingStatus);


    @Query("select new com.matdang.seatdang.waiting.repository.query.dto.WaitingDto(w.id, w.waitingNumber,w.waitingOrder,"
            + " w.customerInfo.customerPhone, w.customerInfo.peopleCount, w.waitingStatus, w.createdAt, w.visitedTime)"
            + " from Waiting w where w.storeId = :storeId and w.waitingStatus in ("
            + "com.matdang.seatdang.waiting.entity.WaitingStatus.SHOP_CANCELED, "
            + "com.matdang.seatdang.waiting.entity.WaitingStatus.NO_SHOW, "
            + "com.matdang.seatdang.waiting.entity.WaitingStatus.CUSTOMER_CANCELED)")
    List<WaitingDto> findAllByCancelStatus(@Param("storeId") Long storeId);
}
