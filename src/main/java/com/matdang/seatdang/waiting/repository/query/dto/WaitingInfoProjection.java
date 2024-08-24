package com.matdang.seatdang.waiting.repository.query.dto;

import com.matdang.seatdang.waiting.entity.WaitingStatus;

public interface WaitingInfoProjection {
    Long getId();
    String getStoreName();
    Long getWaitingNumber();
    Integer getPeopleCount();
    WaitingStatus getWaitingStatus();
}