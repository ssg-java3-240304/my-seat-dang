package com.matdang.seatdang.waiting.redis;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class WaitingNumbers {
    List<Long> waitingNumbers;

    public WaitingNumbers() {
        this.waitingNumbers = new ArrayList<>();
    }
}
