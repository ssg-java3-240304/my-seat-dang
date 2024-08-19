package com.matdang.seatdang.reservation.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Setter(AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "reservation_slot",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"store_id", "date", "time"})
        }
)
public class ReservationSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private int storeId;
    @Column(nullable = false)
    private LocalDate date;
    @Column(nullable = false)
    private LocalTime time;
    @Column(nullable = false)
    private int maxReservation;
    @Column(nullable = false)
    private int usedSlots;
}
