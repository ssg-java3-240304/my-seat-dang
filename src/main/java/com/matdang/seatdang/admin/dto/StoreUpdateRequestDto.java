package com.matdang.seatdang.admin.dto;

import com.matdang.seatdang.common.storeEnum.StoreType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreUpdateRequestDto {
    private  Long storeId;
    private String storeName;
    @Enumerated(EnumType.STRING)
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
}