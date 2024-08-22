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

    public ReservationTicket tryIncreaseSlot(){
        if(usedSlots < maxReservation){
            usedSlots++;
            return ReservationTicket.AVAILABLE;
        }else
            return ReservationTicket.UNAVAILABLE;
    }

    public void returnSlot(){
        System.out.println("이게 왜 0이냐"+this.usedSlots);
        if(0 < this.usedSlots){
            usedSlots--;
        }else if(this.usedSlots == 0){
            throw(new IllegalStateException("예약 슬롯이 0입니다. 더 이상 차감할수 없습니다"));
        }
    }
}