package com.matdang.seatdang.reservation.entity;

import com.matdang.seatdang.common.exception.ReservationException;
import com.matdang.seatdang.reservation.dto.ReservationCancelRequestDto;
import com.matdang.seatdang.reservation.vo.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Entity
@Table(name = "tbl_reservation")
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "reservation_id")
    private long id;
    @Embedded
    private StoreOwnerInfo storeOwner;
    @Embedded
    private CustomerInfo customer;
    @Embedded
    private StoreInfo store;
    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime reservedAt;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "tbl_orderedMenu"
            , joinColumns = @JoinColumn(name = "menu_id", referencedColumnName = "id")
    )
    private List<OrderedMenu> orderedMenuList;
    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;
    @Embedded
    ReservationCancellationRecord cancellationRecord;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public void updateStatus(ReservationStatus newStatus) {
        if(reservationStatus.canTransitionTo(newStatus)){
            this.reservationStatus = newStatus;
        }else{
            throw(new ReservationException("이전 예약 상태로 되돌아갈수 없습니다"));
        }
    }

    public void cancel(ReservationCancellationRecord record) {
        if(this.reservationStatus == ReservationStatus.CANCELED){
            throw new IllegalStateException("이미 취소된 예약입니다");
        }else if(!this.reservationStatus.canTransitionTo(ReservationStatus.CANCELED)){
            throw new IllegalStateException("완료된 예약은 취소할 수 없습니다");
        }
        this.reservationStatus = ReservationStatus.CANCELED;
        this.cancellationRecord = record;
    }
}