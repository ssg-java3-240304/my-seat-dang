package com.matdang.seatdang.store.service;

import com.matdang.seatdang.common.storeEnum.StoreType;
import com.matdang.seatdang.store.dto.StoreListResponseDto;
import com.matdang.seatdang.store.entity.Store;
import com.matdang.seatdang.store.repository.StoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.matdang.seatdang.common.storeEnum.StoreType.CUSTOM;
import static com.matdang.seatdang.common.storeEnum.StoreType.GENERAL_RESERVATION;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    public Page<StoreListResponseDto> findAll(Pageable pageable) {
        Page<Store> storePage = storeRepository.findAll(pageable);
        return storePage.map(StoreListResponseDto::fromStore);
    }

    public Page<StoreListResponseDto> findByStoreTypeContaining(StoreType storeType, StoreType storeType1, Pageable pageable) {
        Page<Store> storePage = storeRepository.findByStoreTypeContaining(GENERAL_RESERVATION, CUSTOM, pageable);
        return storePage.map(StoreListResponseDto::fromStore);
    }
}
