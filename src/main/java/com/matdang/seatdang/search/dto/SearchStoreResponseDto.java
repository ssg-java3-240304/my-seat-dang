package com.matdang.seatdang.search.dto;

import com.matdang.seatdang.common.storeEnum.StoreType;
import com.matdang.seatdang.store.dto.StoreListResponseDto;
import com.matdang.seatdang.store.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchStoreResponseDto {
    private Long storeId;
    private String storeName;
    private String storeAddress;
    private String thumbnail;
    private double starRating;
    private StoreType storeType;

    public static SearchStoreResponseDto fromStore(Store store){
        return new SearchStoreResponseDto(
                store.getStoreId(),
                store.getStoreName(),
                store.getStoreAddress(),
                store.getThumbnail(),
                store.getStarRating(),
                store.getStoreType()
        );
    }
}
