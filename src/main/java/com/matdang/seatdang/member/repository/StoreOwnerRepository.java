// CustomerRepository.java
package com.matdang.seatdang.member.repository;

import com.matdang.seatdang.member.entity.Customer;
import com.matdang.seatdang.member.entity.StoreOwner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreOwnerRepository extends JpaRepository<StoreOwner, Long> {
    Optional<Object> findByStore_storeId(Long storeId);
}
