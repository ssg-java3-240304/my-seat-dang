package com.matdang.seatdang.admin.dto;

import com.matdang.seatdang.common.storeEnum.StoreType;
import com.matdang.seatdang.store.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalTime;
import java.util.Collections;
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
    private MultipartFile thumbnail;
    private List<MultipartFile> images;
    private String storeAddress;
    private LocalTime openTime;
    private LocalTime closeTime;
    private LocalTime startBreakTime;
    private LocalTime endBreakTime;
    private LocalTime lastOrder;
    private String regularDayOff;

    public Store toStore(){
        return Store.builder()
                .storeName(this.storeName)
                .storeType(this.storeType)
                .description(this.description)
                .notice(this.notice)
                .phone(this.phone)
                .storeAddress(this.storeAddress)
                .thumbnail(String.valueOf(this.thumbnail))
//                .images(this.images)
                .openTime(this.openTime)
                .closeTime(this.closeTime)
                .startBreakTime(this.startBreakTime)
                .endBreakTime(this.endBreakTime)
                .lastOrder(this.lastOrder)
                .regularDayOff(this.regularDayOff)
                .build();
    }
}