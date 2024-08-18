// CustomerRepository.java
package com.matdang.seatdang.member.repository;

import com.matdang.seatdang.member.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByCustomerNickName(String customerNickName);
}
