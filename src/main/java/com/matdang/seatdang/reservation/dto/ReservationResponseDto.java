package com.matdang.seatdang.reservation.dto;

import com.matdang.seatdang.reservation.vo.OrderedMenu;
import com.matdang.seatdang.reservation.vo.ReservationCancellationRecord;
import com.matdang.seatdang.reservation.vo.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponseDto {
    private Long reservationId;
    private Long customerId;
    private String customerName;
    private Long storeId;
    private String storeName;
    private Long storeOwnerId;
    private String storeOwnerName;
    private String storePhoneNumber;
    private String thumbnail;

    private LocalDateTime createdAt;
    private LocalDateTime reservedAt;

    private List<OrderedMenu> orderedMenuList;
    private ReservationStatus reservationStatus;
}
