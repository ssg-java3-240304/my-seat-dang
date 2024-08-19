package com.matdang.seatdang.reservation.entity;

import com.matdang.seatdang.reservation.vo.ReservationSlotId;
import com.matdang.seatdang.reservation.vo.ReservationTicket;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@Entity
@Table(
        name = "reservation_slot",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"store_id", "date", "time"})
        }
)
//public class ReservationSlot {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    @Column(nullable = false)
//    private Long storeId;
//    @Column(nullable = false)
//    private LocalDate date;
//    @Column(nullable = false)
//    private LocalTime time;
//    @Column(nullable = false)
//    private int maxReservation;
//    @Column(nullable = false)
//    private int usedSlots;
//
//    public ReservationSlot(Long storeId, LocalDate date, LocalTime time, int maxReservation) {
//        this.storeId = storeId;
//        this.date = date;
//        this.time = time;
//        this.maxReservation = maxReservation;
//        this.usedSlots = 0;
//    }
//
//    public ReservationTicket tryAddSlot(){
//        if(usedSlots < maxReservation){
//            usedSlots++;
//            return ReservationTicket.AVAILABLE;
//        }else
//            return ReservationTicket.UNAVAILABLE;
//    }
//}

public class ReservationSlot {

    @EmbeddedId
    private ReservationSlotId reservationSlotId;
    @Column(nullable = false)
    private int maxReservation;
    @Column(nullable = false)
    private int usedSlots;

    public ReservationSlot(Long storeId, LocalDate date, LocalTime time, int maxReservation) {
        this.reservationSlotId = new ReservationSlotId(storeId, date, time);
        this.maxReservation = maxReservation;
        this.usedSlots = 0;
    }

    public ReservationTicket tryAddSlot(){
        if(usedSlots < maxReservation){
            usedSlots++;
            return ReservationTicket.AVAILABLE;
        }else
            return ReservationTicket.UNAVAILABLE;
    }
}