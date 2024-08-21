package com.matdang.seatdang.member.service;

import com.matdang.seatdang.member.entity.Customer;
import com.matdang.seatdang.member.entity.StoreOwner;
import com.matdang.seatdang.member.repository.MemberRepository;
import com.matdang.seatdang.member.repository.StoreOwnerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreOwnerMemberService {
    private final StoreOwnerRepository storeOwnerRepository;

    public StoreOwner findByStoreId(Long storeId) {
        // DB에서 최신 Customer 정보를 조회
        return (StoreOwner) storeOwnerRepository.findByStore_storeId(storeId).orElseThrow(() -> new RuntimeException("StoreOwner not found"));
    }
}
