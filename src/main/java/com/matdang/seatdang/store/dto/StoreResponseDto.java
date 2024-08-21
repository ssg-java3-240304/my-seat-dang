package com.matdang.seatdang.store.dto;

import com.matdang.seatdang.common.storeEnum.StoreType;
import com.matdang.seatdang.store.entity.Store;
import com.matdang.seatdang.store.vo.StoreSetting;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreResponseDto {
    // 매장 상세 페이지
    private Long storeId;
    private String storeName;
    private String storeAddress;
    private StoreType storeType;
    private LocalTime openTime;
    private LocalTime startBreakTime;
    private LocalTime endBreakTime;
    private LocalTime lastOrder;
    private LocalTime closeTime;
    private String regularDayOff;
    private String thumbnail;
    private List<String> images;
    private String description;
    private String notice;
    private String phone;
    private double starRating;
    private StoreSetting storeSetting;

    // Store 엔티티를 StoreDto로 변환하는 메소드
    public static StoreResponseDto fromEntity(Store store) {
        return StoreResponseDto.builder()
                .storeId(store.getStoreId())
                .storeName(store.getStoreName())
                .storeAddress(store.getStoreAddress())
                .storeType(store.getStoreType())
                .openTime(store.getOpenTime())
                .startBreakTime(store.getStartBreakTime())
                .endBreakTime(store.getEndBreakTime())
                .lastOrder(store.getLastOrder())
                .closeTime(store.getCloseTime())
                .regularDayOff(store.getRegularDayOff())
                .thumbnail(store.getThumbnail())
                .images(store.getImages())
                .description(store.getDescription())
                .notice(store.getNotice())
                .phone(store.getPhone())
                .starRating(store.getStarRating())
                .storeSetting(store.getStoreSetting())
                .build();
    }
}
