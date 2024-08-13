package com.matdang.seatdang.payment.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
// TODO : @Setter 테스트 필요
@Data
@Setter(AccessLevel.PRIVATE)
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ApproveFail {
    private Integer errorCode;
    private String errorMessage;
    private Extras extras;
}
