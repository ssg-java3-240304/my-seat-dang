package com.matdang.seatdang.reservation.dto;

import com.matdang.seatdang.reservation.entity.Reservation;
import com.matdang.seatdang.reservation.vo.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationSaveRequestDto {
    private StoreOwnerInfo storeOwner;
    private CustomerInfo customer;
    private StoreInfo store;
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate date;
    private LocalTime time;
    private List<ReservationRequestMenu> menuList;
    private ReservationStatus reservationStatus;

    public Reservation toEntity() {
        //validate 코드 필요
        return Reservation.builder()
                .storeOwner(this.storeOwner)
                .customer(this.customer)
                .store(this.store)
                .reservedAt(LocalDateTime.of(this.date, this.time))
                .orderedMenuList(convertToOrderedMenuList())
                .reservationStatus(this.reservationStatus)
                .build();
    }

    private List<OrderedMenu> convertToOrderedMenuList() {
        return this.menuList.stream()
                .map(menu -> new OrderedMenu(
                        menu.getMenuName(),
                        menu.getMenuPrice(),
                        menu.getImageUrl(),
                        menu.getMenuType(),
                        menu.getCustomMenuOpt(),
                        menu.getQuantity()
                ))
                .collect(Collectors.toList());
    }
}
