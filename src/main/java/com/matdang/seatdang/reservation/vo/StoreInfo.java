package com.matdang.seatdang.reservation.vo;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class StoreInfo {
    private Long storeId;
    private String storeName;
    private String storePhone;
    private String thumbnail;
}
