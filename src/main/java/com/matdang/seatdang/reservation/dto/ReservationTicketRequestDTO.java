package com.matdang.seatdang.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationTicketRequestDTO {
    private Long storeId;
    private LocalDate date;
    private LocalTime time;
    private int maxReservation;
}
