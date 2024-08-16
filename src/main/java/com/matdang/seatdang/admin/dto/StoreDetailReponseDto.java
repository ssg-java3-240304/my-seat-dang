package com.matdang.seatdang.admin.dto;

import com.matdang.seatdang.common.storeEnum.StoreType;
import com.matdang.seatdang.store.entity.Store;
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
public class StoreDetailReponseDto {
    private String storeName;
    @Enumerated(EnumType.STRING)
    private StoreType storeType;
    private String description;
    private String notice;
    private String phone;
    private String thumbnail;
    private List<String> images;
    private String storeAddress;
    private LocalTime openTime;
    private LocalTime closeTime;
    private LocalTime startBreakTime;
    private LocalTime endBreakTime;
    private LocalTime lastOrder;
    private String regularDayOff;
//    private Long menuListId;

    public static StoreDetailReponseDto fromStore(Store store){
        return new StoreDetailReponseDto(
                store.getStoreName(),
                store.getStoreType(),
                store.getDescription(),
                store.getNotice(),
                store.getPhone(),
                store.getThumbnail(),
                store.getImages(),
                store.getStoreAddress(),
                store.getOpenTime(),
                store.getCloseTime(),
                store.getStartBreakTime(),
                store.getEndBreakTime(),
                store.getLastOrder(),
                store.getRegularDayOff()
//                store.getMenuListId()
        );
    }
}