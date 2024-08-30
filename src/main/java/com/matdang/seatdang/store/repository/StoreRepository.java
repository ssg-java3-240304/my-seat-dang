package com.matdang.seatdang.store.repository;

import com.matdang.seatdang.common.storeEnum.StoreType;
import com.matdang.seatdang.store.entity.Store;
import com.matdang.seatdang.store.vo.Status;
import com.matdang.seatdang.store.vo.WaitingStatus;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import java.time.LocalTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.annotation.Transactional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    Page<Store> findByStoreNameContainingAndStoreAddressContainingOrderByStoreAddressDesc(String storeName,
                                                                                          String storeAddress,
                                                                                          Pageable pageable);

    Page<Store> findByStoreNameContainingOrderByStoreAddressDesc(String storeAddress, Pageable pageable);

    Page<Store> findByStoreAddressContainingOrderByStoreAddressDesc(String storeAddress, Pageable pageable);

    Page<Store> findByStoreNameContainingOrderByStoreAddressAsc(String storeAddress, Pageable pageable);

    Page<Store> findByStoreAddressContainingOrderByStoreAddressAsc(String storeAddress, Pageable pageable);

    Page<Store> findByStoreAddressContaining(String storeAddress, Pageable pageable);

    Page<Store> findByStoreNameContaining(String storeName, Pageable pageable);

    Page<Store> findAllByOrderByStarRatingDesc(Pageable pageable);

    Page<Store> findByStoreTypeContaining(StoreType storeType, Pageable pageable);

    Store findByStoreId(Long storeId);

    Page<Store> findByStoreTypeOrderByStoreAddressDesc(StoreType storeType, Pageable pageable);

    @Modifying
    @Transactional
    @Query("update Store s"
            + " set s.storeSetting.waitingTime.waitingOpenTime = :waitingOpenTime,"
            + " s.storeSetting.waitingTime.waitingCloseTime = :waitingCloseTime"
            + " where s.storeId = :storeId")
    int updateWaitingAvailableTime(@Param("waitingOpenTime") LocalTime waitingOpenTime,
                                   @Param("waitingCloseTime") LocalTime waitingCloseTime,
                                   @Param("storeId") Long storeId);

    @Modifying
    @Transactional
    @Query("update Store s"
            + " set s.storeSetting.waitingTime.estimatedWaitingTime = :estimatedWaitingTime"
            + " where s.storeId = :storeId")
    int updateEstimatedWaitingTime(@Param("estimatedWaitingTime") LocalTime estimatedWaitingTime,
                                   @Param("storeId") Long storeId);

    @Modifying
    @Transactional
    @Query("update Store s"
            + " set s.storeSetting.waitingStatus = :waitingStatus"
            + " where s.storeId = :storeId")
    int updateWaitingStatus(@Param("waitingStatus") WaitingStatus waitingStatus, @Param("storeId") Long storeId);

    @Modifying
    @Transactional
    @Query("update Store s"
            + " set s.storeSetting.waitingPeopleCount = :waitingPeopleCount"
            + " where s.storeId = :storeId")
    int updateWaitingPeopleCount(@Param("waitingPeopleCount") Integer waitingPeopleCount,
                                 @Param("storeId") Long storeId);
}
