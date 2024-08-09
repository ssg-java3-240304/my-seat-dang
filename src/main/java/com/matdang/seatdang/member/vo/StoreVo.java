package com.matdang.seatdang.member.vo;


import common.storeEnum.StoreType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * 상점 정보를 가진 VO
 *
 */


@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreVo {
    private Long storeId;
    private String storeName;

    @Enumerated(EnumType.STRING)
    private StoreType storeType;

    private String storeAddress;



    // Getters and setters (optional)
}