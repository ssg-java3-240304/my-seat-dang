package com.matdang.seatdang.member.dto;

import com.matdang.seatdang.member.entity.StoreOwner;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreOwnerResponseDto {
    private Long storeOwnerId;
    private String storeOwnerName;

    public StoreOwnerResponseDto fromEntity(StoreOwner storeOwner) {
        return new StoreOwnerResponseDto(storeOwner.getMemberId(), storeOwner.getMemberName());
    }
}
