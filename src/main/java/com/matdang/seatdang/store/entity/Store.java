package com.matdang.seatdang.store.entity;

import com.matdang.seatdang.admin.dto.StoreDetailDto;
import com.matdang.seatdang.admin.dto.StoreUpdateRequestDto;
import com.matdang.seatdang.common.storeEnum.StoreType;
import com.matdang.seatdang.store.vo.StoreSetting;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.List;

@Entity
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
    @Column(name = "images")
    private List<String> images;
    @Column(name = "description")
    private String description;
    @Column(name = "notice")
    private String notice;
    @Column(name = "phone")
    private String phone;
    @Column(name = "starRating")
    private double starRating;

    @Embedded
    private StoreSetting storeSetting;

    public void update(StoreDetailDto dto) {
        if (dto.getStoreName() != null) {
            this.storeName = dto.getStoreName();
        }
        if (dto.getStoreAddress() != null) {
            this.storeAddress = dto.getStoreAddress();
        }
        if (dto.getStoreType() != null) {
            this.storeType = dto.getStoreType();
        }
        if (dto.getOpenTime() != null) {
            this.openTime = dto.getOpenTime();
        }
        if (dto.getCloseTime() != null) {
            this.closeTime = dto.getCloseTime();
        }
        if (dto.getStartBreakTime() != null) {
            this.startBreakTime = dto.getStartBreakTime();
        }
        if (dto.getEndBreakTime() != null) {
            this.endBreakTime = dto.getEndBreakTime();
        }
        if (dto.getLastOrder() != null) {
            this.lastOrder = dto.getLastOrder();
        }
        if (dto.getRegularDayOff() != null) {
            this.regularDayOff = dto.getRegularDayOff();
        }
        if (dto.getThumbnail() != null) {
            this.thumbnail = dto.getThumbnail();
        }
        if (dto.getImages() != null) {
            this.images = dto.getImages();
        }
        if (dto.getDescription() != null) {
            this.description = dto.getDescription();
        }
        if (dto.getNotice() != null) {
            this.notice = dto.getNotice();
        }
        if (dto.getPhone() != null) {
            this.phone = dto.getPhone();
        }
    }
}