package com.matdang.seatdang.reservation.dto;

import com.matdang.seatdang.reservation.entity.Reservation;
import com.matdang.seatdang.reservation.vo.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationSaveRequestDto {
    private StoreOwnerInfo storeOwner;
    private CustomerInfo customer;
    private StoreInfo store;
    private LocalDateTime reservedAt;
    private List<OrderedMenu> orderedMenuList;
    private ReservationStatus reservationStatus;

    public Reservation toEntity() {
        //validate 코드 필요
        return Reservation.builder()
                .storeOwner(this.storeOwner)
                .customer(this.customer)
                .store(this.store)
                .reservedAt(this.reservedAt)
                .orderedMenuList(this.orderedMenuList)
                .reservationStatus(this.reservationStatus)
                .build();
    }
}
