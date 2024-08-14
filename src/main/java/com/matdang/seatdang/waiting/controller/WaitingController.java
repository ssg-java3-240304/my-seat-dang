package com.matdang.seatdang.waiting.controller;

import com.matdang.seatdang.waiting.entity.CustomerInfo;
import com.matdang.seatdang.waiting.entity.Waiting;
import com.matdang.seatdang.waiting.entity.WaitingStatus;
import com.matdang.seatdang.waiting.repository.WaitingRepository;
import com.matdang.seatdang.waiting.service.WaitingService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/store/waiting")
public class WaitingController {
    private final WaitingRepository waitingRepository;
    private final WaitingService waitingService;

    @GetMapping
    public String showWaiting(@RequestParam(defaultValue = "0") int status, Model model) {
        List<Waiting> waitings =waitingService.showWaiting(1L, status);
        model.addAttribute("waitings", waitings);
        return "store/waiting/main";
    }

    /**
     * test 실행시 주석 필요
     */
    @PostConstruct
    public void initData() {
        {
            long i = 0;
            for (WaitingStatus value : WaitingStatus.values()) {
                for (int j = 0; j < 10; j++, i++) {
                    waitingRepository.save(Waiting.builder()
                            .waitingId(i)
                            .waitingNumber(i)
                            .storeId(1L)
                            .customerInfo(new CustomerInfo(i, "010-1111-1111"))
                            .waitingStatus(value)
                            .createdAt(LocalDateTime.now())
                            .visitedTime(null)
                            .build());

                }
            }
        }

        for (long i = 0; i < 10; i++) {
            waitingRepository.save(Waiting.builder()
                    .waitingId(i)
                    .waitingNumber(i)
                    .storeId(2L)
                    .customerInfo(new CustomerInfo(i, "010-1111-1111"))
                    .waitingStatus(WaitingStatus.WAITING)
                    .createdAt(LocalDateTime.now())
                    .visitedTime(null)
                    .build());
        }

    }
}
