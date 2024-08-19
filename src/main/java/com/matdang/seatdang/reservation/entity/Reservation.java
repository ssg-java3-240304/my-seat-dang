package com.matdang.seatdang.reservation.entity;

import com.matdang.seatdang.reservation.dto.ReservationDto;
import com.matdang.seatdang.reservation.vo.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

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
    private LocalDateTime createdAt;
    private LocalDateTime reservedAt;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "tbl_orderedMenu"
            , joinColumns = @JoinColumn(name = "menu_id", referencedColumnName = "id")
    )
    private Set<OrderedMenu> orderedMenuSet;
    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;
    @Embedded
    private ChatRoom chatRoom;

    public void setEmpty(ReservationDto dto){
        this.store = dto.getStore();
        this.customer = dto.getCustomer();
        this.storeOwner = dto.getStoreOwner();
        this.reservedAt = dto.getReservedAt();
        this.chatRoom = dto.getChatRoom();
    }
}