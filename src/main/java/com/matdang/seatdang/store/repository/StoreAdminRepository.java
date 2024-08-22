package com.matdang.seatdang.store.repository;

import com.matdang.seatdang.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StoreAdminRepository extends JpaRepository<Store, Long> {
    Store findByStoreName(@Param("storeName") String storeName);


    Store findByStoreId(Long storeId);
}