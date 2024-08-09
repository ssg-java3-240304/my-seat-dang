package com.matdang.seatdang.search.service;

import com.matdang.seatdang.store.dto.StoreListResponseDto;
import com.matdang.seatdang.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchStoreService {
    private final StoreRepository storeRepository;
    /**
     * 상호로 검색
     */
    public Page<StoreListResponseDto> searchStoreBystoreName(String storeName, Pageable pageable) {

        return storeRepository.findByStoreNameContaining(storeName, pageable)
                .map((store -> StoreListResponseDto.fromStore(store)));
    }
}
