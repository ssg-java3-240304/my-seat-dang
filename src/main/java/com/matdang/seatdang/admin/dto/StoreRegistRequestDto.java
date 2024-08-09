package com.matdang.seatdang.admin.dto;

import com.matdang.seatdang.common.storeEnum.StoreType;
import com.matdang.seatdang.store.entity.Store;
import jakarta.persistence.*;
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
public class StoreRegistRequestDto {
    private String storeName;
    private StoreType storeType;
    private String description;
    private String notice;
    private String phone;

    private String thumbnail;
    private List<String> images;

    private String storeAddress;

    private String openTime;
    private String closeTime;
    private String startBreakTime;
    private String endBreakTime;
    private String lastOrder;
    private String regularDayOff;

    public Store toStore(){
        return Store.builder()
                .storeName(this.storeName)
                .storeType(this.storeType)
                .description(this.description)
                .notice(this.notice)
                .phone(this.phone)
                .storeAddress(this.storeAddress)
                .thumbnail(this.thumbnail)
                .images(this.images)
                .openTime(this.openTime)
                .closeTime(this.closeTime)
                .startBreakTime(this.startBreakTime)
                .endBreakTime(this.endBreakTime)
                .lastOrder(this.lastOrder)
                .regularDayOff(this.regularDayOff)
                .build();
    }
}