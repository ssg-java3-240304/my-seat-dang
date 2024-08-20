package com.matdang.seatdang.reservation.entity;

import com.matdang.seatdang.reservation.vo.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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
    private ChatRoom chatRoom;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}