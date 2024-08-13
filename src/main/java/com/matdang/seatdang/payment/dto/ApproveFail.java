package com.matdang.seatdang.payment.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ApproveFail {
    private Integer errorCode;
    private Status status;
    private String errorMessage;
    private Extras extras;

    public void registerStatus(Status status) {
        this.status = status;
    }
}
