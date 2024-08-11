package com.matdang.seatdang.search.service;

import com.matdang.seatdang.search.dto.SearchStoreResponseDto;
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
     * @param storeName
     * @param pageable
     * @return
     */
    public Page<SearchStoreResponseDto> searchStoreByStoreName(String storeName, Pageable pageable) {
        log.debug("searchStoreByStoreName: {}", storeName);
        return storeRepository.findByStoreNameContainingOrderByStoreAddressDesc(storeName, pageable)
                .map((store -> SearchStoreResponseDto.fromStore(store)));
    }

    /**
     * 주소로 검색
     * @param storeAddress
     * @param pageable
     * @return
     */
    public Page<SearchStoreResponseDto> searchStoreByAddress(String storeAddress, Pageable pageable) {
        log.debug("searchStoreByAddress: {}", storeAddress);
        return storeRepository.findByStoreAddressContainingOrderByStoreAddressDesc(storeAddress, pageable)
                .map((store -> SearchStoreResponseDto.fromStore(store)));
    }

    public Page<SearchStoreResponseDto> searchStoreByNameAndAddress(String storeName, String storeAddress, Pageable pageable) {
        log.debug("searchStoreByNameAndAddress Service: storeName={}, storeAddress={}", storeName, storeAddress);
        return storeRepository.findByStoreNameContainingAndStoreAddressContainingOrderByStoreAddressDesc(storeName, storeAddress, pageable)
                .map((store -> SearchStoreResponseDto.fromStore(store)));
    }


}
