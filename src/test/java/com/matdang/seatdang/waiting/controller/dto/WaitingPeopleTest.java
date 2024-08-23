package com.matdang.seatdang.waiting.controller.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.matdang.seatdang.waiting.repository.query.dto.WaitingDto;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class WaitingPeopleTest {

    @Test
    @DisplayName("현재 웨이팅 상태에 대한 총 팀, 인원수 생성")
    void create() {
        // given
        List<WaitingDto> waitingDtoList = new ArrayList<>();
        waitingDtoList.add(new WaitingDto(1L, 1L, 1L, "010-1111-1111",
                2, null, null, null, null));
        waitingDtoList.add(new WaitingDto(1L, 1L, 1L, "010-1111-1111",
                3, null, null, null, null));
        waitingDtoList.add(new WaitingDto(1L, 1L, 1L, "010-1111-1111",
                5, null, null, null, null));
        // when
        WaitingPeople waitingPeople = WaitingPeople.create(waitingDtoList);

        // then
        assertThat(waitingPeople.getPeopleCount()).isEqualTo(10L);
        assertThat(waitingPeople.getTeamCount()).isEqualTo(3);

    }
}