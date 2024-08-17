package com.matdang.seatdang.waiting.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class WaitingStatusTest {

    @ParameterizedTest
    @CsvSource(value = {"0,WAITING","1,VISITED","2,SHOP_CANCELED","3,NO_SHOW","4,CUSTOMER_CANCELED"})
    @DisplayName("인덱스로 웨이팅 찾기")
    void findWaiting(int waitingIndex, String status) {
        // given
        // when
        WaitingStatus waiting = WaitingStatus.findWaiting(waitingIndex);
        // then
        assertThat(waiting).isEqualTo(WaitingStatus.valueOf(status));
    }
}

