package com.matdang.seatdang.waiting.repository;

import com.matdang.seatdang.waiting.entity.WaitingStorage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WaitingStorageRepository extends JpaRepository<WaitingStorage, Long> {

    @Query("select w"
            + " from WaitingStorage w"
            + " where w.storeId = :storeId")
    List<WaitingStorage> findAllByStoreId(@Param("storeId") Long storeId);
}
