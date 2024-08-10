package com.matdang.seatdang.search.service;

import com.matdang.seatdang.store.dto.StoreListResponseDto;
import com.matdang.seatdang.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchStoreQueryService {
    private final StoreRepository storeRepository;
    /**
     * 상호로 검색
     */
    public Page<StoreListResponseDto> searchStoreByStoreName(String storeName, Pageable pageable) {
        log.debug("searchStoreByStoreName: {}", storeName);
        return storeRepository.findByStoreNameContaining(storeName, pageable)
                .map((store -> StoreListResponseDto.fromStore(store)));
    }

    public Page<StoreListResponseDto> searchStoreByAddress(String storeAddress, Pageable pageable) {
        log.debug("searchStoreByAddress: {}", storeAddress);
        return storeRepository.findByStoreAddressContaining(storeAddress, pageable)
                .map((store -> StoreListResponseDto.fromStore(store)));
    }


}
