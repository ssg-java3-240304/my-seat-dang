package com.matdang.seatdang.store.dto;

import com.matdang.seatdang.store.entity.Store;
import com.matdang.seatdang.common.storeEnum.StoreType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreListResponseDto {
    private Long storeId;
    private String storeName;
    private String storeAddress;
    private String thumbnail;
    private List<String> images;
    private double starRating;
    @Enumerated(EnumType.STRING)
    private StoreType storeType;

    public static StoreListResponseDto fromStore(Store store){
        return new StoreListResponseDto(
                store.getStoreId(),
                store.getStoreName(),
                store.getStoreAddress(),
                store.getThumbnail(),
                store.getImages(),
                store.getStarRating(),
                store.getStoreType()
        );
    }
}