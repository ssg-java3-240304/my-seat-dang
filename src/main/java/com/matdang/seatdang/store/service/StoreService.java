package com.matdang.seatdang.store.service;

import com.matdang.seatdang.store.dto.StoreListResponseDto;
import com.matdang.seatdang.store.entity.Store;
import com.matdang.seatdang.store.repository.StoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    public Page<StoreListResponseDto> findAll(String q, Pageable pageable) {
        Page<Store> storePage = q != null ?
                storeRepository.findByStoreNameContaining(q, pageable) :
                storeRepository.findAll(pageable);
        return storePage.map(StoreListResponseDto::fromStore);
    }
}
