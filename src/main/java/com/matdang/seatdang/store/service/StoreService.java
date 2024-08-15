package com.matdang.seatdang.store.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.matdang.seatdang.common.storeEnum.StoreType;
import com.matdang.seatdang.object_storage.model.dto.FileDto;
import com.matdang.seatdang.object_storage.service.FileService;
import com.matdang.seatdang.store.dto.StoreListResponseDto;
import com.matdang.seatdang.store.entity.Store;
import com.matdang.seatdang.store.repository.StoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public Page<StoreListResponseDto> findByStoreTypeContaining(StoreType storeType, StoreType storeType1, Pageable pageable) {
        Page<Store> storePage = storeRepository.findByStoreTypeContaining(GENERAL_RESERVATION, CUSTOM, pageable);
        return storePage.map(StoreListResponseDto::fromStore);
    }

    public Store findByStoreId(Long storeId) {
        return storeRepository.findByStoreId(storeId);
    }
}
