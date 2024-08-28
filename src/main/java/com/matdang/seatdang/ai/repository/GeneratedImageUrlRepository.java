package com.matdang.seatdang.ai.repository;

import com.matdang.seatdang.ai.entity.GeneratedImageUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GeneratedImageUrlRepository extends JpaRepository<GeneratedImageUrl, Long> {
    List<GeneratedImageUrl> findAllByCustomerId(Long customerId);
}
