package com.matdang.seatdang.ai.repository;

import com.matdang.seatdang.ai.entity.GeneratedImageUrl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GeneratedImageUrlRepository extends JpaRepository<GeneratedImageUrl, Long> {

    // 기존 메서드 (예시)
    List<GeneratedImageUrl> findAllByCustomerId(Long customerId);
    Page<GeneratedImageUrl> findPageByCustomerId(Long customerId, Pageable pageable);
}
