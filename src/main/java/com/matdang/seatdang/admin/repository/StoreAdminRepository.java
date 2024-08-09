package com.matdang.seatdang.admin.repository;

import com.matdang.seatdang.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StoreAdminRepository extends JpaRepository<Store, Long> {
    Store findByStoreName(@Param("storeName") String storeName);
}