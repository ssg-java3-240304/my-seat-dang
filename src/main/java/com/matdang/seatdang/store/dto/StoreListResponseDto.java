package com.matdang.seatdang.store.dto;

import com.matdang.seatdang.store.entity.Store;
import com.matdang.seatdang.common.storeEnum.StoreType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreListResponseDto {
    private Long storeId;
    private String storeName;
    private String storeAddress;
    private String thumbnail;
    private double starRating;
    private StoreType storeType;

    public static StoreListResponseDto fromStore(Store store){
        return new StoreListResponseDto(
                store.getStoreId(),
                store.getStoreName(),
                store.getStoreAddress(),
                store.getThumbnail(),
                store.getStarRating(),
                store.getStoreType()
        );
    }
}