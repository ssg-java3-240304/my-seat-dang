package com.matdang.seatdang.store.entity;

import com.matdang.seatdang.common.storeEnum.StoreType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity(name = "store")
@Table(name = "store")
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long storeId;
    @Column(name = "store_name")
    private String storeName;
    @Column(name = "store_address")
    private String storeAddress;
    @Column(name = "store_type")
    @Enumerated(EnumType.STRING)
    private StoreType storeType;
    @Column(name = "open_time")
    private LocalTime openTime;
    @Column(name = "start_break_time")
    private LocalTime startBreakTime;
    @Column(name = "end_break_time")
    private LocalTime endBreakTime;
    @Column(name = "last_order")
    private LocalTime lastOrder;
    @Column(name = "close_time")
    private LocalTime closeTime;
    @Column(name = "regular_day_off")
    private String regularDayOff;
    @Column(name = "thumbnail")
    private String thumbnail;
    @Column(name = "image")
    private String image;
    @Column(name = "description")
    private String description;
    @Column(name = "notice")
    private String notice;
    @Column(name = "phone")
    private String phone;
    @Column(name = "star_rating")
    private double starRating;
//    @Column(name = "setting")
//    @Embedded
//    private StoreSetting setting;

    // menu 필요

}
