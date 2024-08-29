package com.matdang.seatdang.store.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.matdang.seatdang.common.storeEnum.StoreType;
import com.matdang.seatdang.store.dto.StoreListResponseDto;
import com.matdang.seatdang.store.dto.StoreResponseDto;
import com.matdang.seatdang.store.entity.Store;
import com.matdang.seatdang.store.repository.StoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.matdang.seatdang.common.storeEnum.StoreType.CUSTOM;
import static com.matdang.seatdang.common.storeEnum.StoreType.GENERAL_RESERVATION;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;

    @Value("${spring.s3.bucket}")
    private String bucketName;

    private final AmazonS3Client amazonS3Client;

    public Page<StoreListResponseDto> findAll(Pageable pageable) {
        Page<Store> storePage = storeRepository.findAll(pageable);
        return storePage.map(StoreListResponseDto::fromStore);
    }

    public StoreResponseDto findByStoreId(Long storeId) {
        return StoreResponseDto.fromEntity(storeRepository.findByStoreId(storeId));
    }

    public int getMaxReservationInTime(Long storeId){
        Optional<Store> optStore = storeRepository.findById(storeId);
        Store store = optStore.orElse(null);
        if(store != null){
            return store.getStoreSetting().getMaxReservationInTime();
        }else {
            return 0;
        }
    }

    @Cacheable(value = "storeNames", key = "#storeId")
    public String findStoreNameByStoreId(Long storeId) {
        Store store = storeRepository.findByStoreId(storeId);
        return store.getStoreName();
    }

}
