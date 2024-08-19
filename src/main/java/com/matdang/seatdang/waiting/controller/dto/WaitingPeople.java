package com.matdang.seatdang.waiting.controller.dto;

import com.matdang.seatdang.waiting.repository.query.dto.WaitingDto;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WaitingPeople {
    private int teamCount;
    private int peopleCount;

    public static WaitingPeople create(List<WaitingDto> waitings) {
        int sum = 0;
        for (WaitingDto waiting : waitings) {
            sum += waiting.getPeopleCount();
        }

        return new WaitingPeople(waitings.size(), sum);
    }
}
