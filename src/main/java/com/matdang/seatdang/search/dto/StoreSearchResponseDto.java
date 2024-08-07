package com.matdang.seatdang.search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreSearchResponseDto {
    private long storeId;
    private String storeName;
    private String storeAddress;
}
