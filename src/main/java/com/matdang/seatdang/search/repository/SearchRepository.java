package com.matdang.seatdang.search.repository;

import com.matdang.seatdang.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface SearchRepository extends JpaRepository<Store, Long> {
}
