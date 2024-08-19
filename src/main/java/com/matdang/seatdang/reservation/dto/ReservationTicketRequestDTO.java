package com.matdang.seatdang.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationTicketRequestDTO {
    private Long storeId;
    private LocalDate date;
    private LocalTime time;
    private int maxReservation;
}
