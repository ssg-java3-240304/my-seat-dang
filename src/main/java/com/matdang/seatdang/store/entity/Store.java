package com.matdang.seatdang.store.entity;

import com.matdang.seatdang.common.storeEnum.StoreType;
import com.matdang.seatdang.menu.entity.MenuList;
import com.matdang.seatdang.store.vo.StoreSetting;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.List;

@Entity(name = "store")
@Table(name = "store")
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long storeId;
    @Column(name = "store_name")
    private String storeName;
    @Column(name = "store_address")
    private String storeAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "store_type")
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
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "tbl_store_images"
            , joinColumns = @JoinColumn(name = "image_id", referencedColumnName = "store_id")
    )
    private List<String> images;
//    @Column(name = "image1")
//    private String image1;
//    @Column(name = "image2")
//    private String image2;
//    @Column(name = "image3")
//    private String image3;
    @Column(name = "description")
    private String description;
    @Column(name = "notice")
    private String notice;
    @Column(name = "phone")
    private String phone;
    @Column(name = "starRating")
    private double starRating;

    @Embedded
    @Column(name = "setting")
    private StoreSetting setting;

    @Column(name = "menus")
    private Long menuListId;
}
