package com.matdang.seatdang.waiting.repository.query;

import com.matdang.seatdang.waiting.entity.Waiting;
import com.matdang.seatdang.waiting.entity.WaitingStatus;
import com.matdang.seatdang.waiting.entity.WaitingStorage;
import com.matdang.seatdang.waiting.repository.query.dto.WaitingDto;
import com.matdang.seatdang.waiting.repository.query.dto.WaitingInfoDto;
import com.matdang.seatdang.waiting.repository.query.dto.WaitingInfoProjection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WaitingQueryRepository extends JpaRepository<Waiting, Long> {
    @Query("select new com.matdang.seatdang.waiting.repository.query.dto.WaitingDto("
            + "w.id, w.waitingNumber,w.waitingOrder, w.customerInfo.customerPhone, w.customerInfo.peopleCount,"
            + " w.waitingStatus, w.createdDate, w.visitedTime, w.canceledTime)"
            + " from Waiting w"
            + " where w.storeId = :storeId and w.waitingStatus = :waitingStatus")
    Page<WaitingDto> findAllByStoreIdAndWaitingStatus(@Param("storeId") Long storeId,
                                                      @Param("waitingStatus") WaitingStatus waitingStatus,
                                                      Pageable pageable);

    @Query("select new com.matdang.seatdang.waiting.repository.query.dto.WaitingDto("
            + "w.id, w.waitingNumber,w.waitingOrder, w.customerInfo.customerPhone, w.customerInfo.peopleCount,"
            + " w.waitingStatus, w.createdDate, w.visitedTime,  w.canceledTime)"
            + " from Waiting w"
            + " where w.storeId = :storeId and w.waitingStatus = com.matdang.seatdang.waiting.entity.WaitingStatus.WAITING"
            + " order by w.waitingOrder")
    Page<WaitingDto> findAllByWaitingStatusOrderByWaitingOrder(@Param("storeId") Long storeId, Pageable pageable);

    @Query("select new com.matdang.seatdang.waiting.repository.query.dto.WaitingDto("
            + "w.id, w.waitingNumber,w.waitingOrder, w.customerInfo.customerPhone, w.customerInfo.peopleCount,"
            + " w.waitingStatus, w.createdDate, w.visitedTime, w.canceledTime)"
            + " from Waiting w where w.storeId = :storeId and w.waitingStatus in ("
            + "com.matdang.seatdang.waiting.entity.WaitingStatus.SHOP_CANCELED, "
            + "com.matdang.seatdang.waiting.entity.WaitingStatus.NO_SHOW, "
            + "com.matdang.seatdang.waiting.entity.WaitingStatus.CUSTOMER_CANCELED)")
    Page<WaitingDto> findAllByCancelStatus(@Param("storeId") Long storeId, Pageable pageable);

    @Query("select new com.matdang.seatdang.waiting.entity.WaitingStorage(w.waitingNumber, w.waitingOrder,w.storeId,"
            + " w.customerInfo.customerId, w.customerInfo.customerPhone, w.customerInfo.peopleCount,"
            + " w.createdDate, w.waitingStatus, w.visitedTime, w.canceledTime) from Waiting w "
            + " where w.storeId = :storeId")
    List<WaitingStorage> findAllByStoreId(@Param("storeId") Long storeId);

    @Query("select new com.matdang.seatdang.waiting.repository.query.dto.WaitingInfoDto("
            + " w.id, s.storeName, w.waitingNumber, w.customerInfo.peopleCount, w.waitingStatus)"
            + " from Waiting w"
            + " join Store s on w.storeId = s.storeId"
            + " where w.customerInfo.customerId = :customerId"
            + " and w.waitingStatus = :waitingStatus")
    Page<WaitingInfoDto> findAllByCustomerIdAndWaitingStatus(@Param("customerId") Long customerId,
                                                             @Param("waitingStatus") WaitingStatus waitingStatus,
                                                             Pageable pageable);

    @Query("select new com.matdang.seatdang.waiting.repository.query.dto.WaitingInfoDto("
            + " w.id, s.storeName, w.waitingNumber, w.customerInfo.peopleCount, w.waitingStatus)"
            + " from Waiting w"
            + " join Store s on w.storeId = s.storeId"
            + " where w.customerInfo.customerId = :customerId"
            + " and w.waitingStatus in ("
            + " com.matdang.seatdang.waiting.entity.WaitingStatus.SHOP_CANCELED,"
            + " com.matdang.seatdang.waiting.entity.WaitingStatus.NO_SHOW,"
            + " com.matdang.seatdang.waiting.entity.WaitingStatus.CUSTOMER_CANCELED)")
    Page<WaitingInfoDto> findAllByCustomerIdAndCancelStatus(@Param("customerId") Long customerId,
                                                            Pageable pageable);

    @Query(value = "SELECT id, store_name AS storeName, waiting_number AS waitingNumber, "
            + "people_count AS peopleCount, waiting_status AS waitingStatus "
            + "FROM ("
            + "    SELECT w.id, s.store_name, w.waiting_number, w.people_count, w.waiting_status, w.created_date "
            + "    FROM waiting w "
            + "    JOIN store s ON w.store_id = s.store_id "
            + "    WHERE w.customer_id = :customerId "
            + "      AND w.waiting_status = :#{#waitingStatus.name()} "
            + "    UNION ALL "
            + "    SELECT ws.id, s.store_name, ws.waiting_number, ws.people_count, ws.waiting_status, ws.created_date "
            + "    FROM waiting_storage ws "
            + "    JOIN store s ON ws.store_id = s.store_id "
            + "    WHERE ws.customer_id = :customerId "
            + "      AND ws.waiting_status = :#{#waitingStatus.name()} "
            + ") AS combined "
            + "ORDER BY created_date DESC", // 'created_date'로 최신순 정렬
            countQuery = "SELECT COUNT(*) FROM ("
                    + "    SELECT 1 "
                    + "    FROM waiting w "
                    + "    WHERE w.customer_id = :customerId "
                    + "      AND w.waiting_status = :#{#waitingStatus.name()} "
                    + "    UNION ALL "
                    + "    SELECT 1 "
                    + "    FROM waiting_storage ws "
                    + "    WHERE ws.customer_id = :customerId "
                    + "      AND ws.waiting_status = :#{#waitingStatus.name()} "
                    + ") AS combined",
            nativeQuery = true)
    Page<WaitingInfoProjection> findUnionAllByCustomerIdAndWaitingStatus(@Param("customerId") Long customerId,
                                                                         @Param("waitingStatus") WaitingStatus waitingStatus,
                                                                         Pageable pageable);

    @Query(value = "SELECT id, store_name AS storeName, waiting_number AS waitingNumber, "
            + "people_count AS peopleCount, waiting_status AS waitingStatus "
            + "FROM ("
            + "    SELECT w.id, s.store_name, w.waiting_number, w.people_count, w.waiting_status, w.created_date "
            + "    FROM waiting w "
            + "    JOIN store s ON w.store_id = s.store_id "
            + "    WHERE w.customer_id = :customerId "
            + "      AND w.waiting_status IN ("
            + "          'SHOP_CANCELED',"
            + "          'NO_SHOW',"
            + "          'CUSTOMER_CANCELED'"
            + "      ) "
            + "    UNION ALL "
            + "    SELECT ws.id, s.store_name, ws.waiting_number, ws.people_count, ws.waiting_status, ws.created_date "
            + "    FROM waiting_storage ws "
            + "    JOIN store s ON ws.store_id = s.store_id "
            + "    WHERE ws.customer_id = :customerId "
            + "      AND ws.waiting_status IN ("
            + "          'SHOP_CANCELED',"
            + "          'NO_SHOW',"
            + "          'CUSTOMER_CANCELED'"
            + "      ) "
            + ") AS combined "
            + "ORDER BY created_date DESC",
            countQuery = "SELECT COUNT(*) FROM ("
                    + "    SELECT 1 "
                    + "    FROM waiting w "
                    + "    WHERE w.customer_id = :customerId "
                    + "      AND w.waiting_status IN ("
                    + "          'SHOP_CANCELED',"
                    + "          'NO_SHOW',"
                    + "          'CUSTOMER_CANCELED') "
                    + "    UNION ALL "
                    + "    SELECT 1 "
                    + "    FROM waiting_storage ws "
                    + "    WHERE ws.customer_id = :customerId "
                    + "      AND ws.waiting_status IN ("
                    + "          'SHOP_CANCELED',"
                    + "          'NO_SHOW',"
                    + "          'CUSTOMER_CANCELED') "
                    + ") AS combined",
            nativeQuery = true)
    Page<WaitingInfoProjection> findUnionAllByCustomerIdAndCancelStatus(@Param("customerId") Long customerId,
                                                                        Pageable pageable);
}
