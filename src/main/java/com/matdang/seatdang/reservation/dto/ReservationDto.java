package com.matdang.seatdang.reservation.dto;

import com.matdang.seatdang.reservation.vo.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDto {
    private StoreOwnerInfo storeOwner;
    private CustomerInfo customer;
    private StoreInfo store;
    private LocalDateTime createdAt;
    private LocalDateTime reservedAt;
    private Set<OrderedMenu> orderedMenuSet;
    private ReservationStatus reservationStatus;
    private ChatRoom chatRoom;
}
