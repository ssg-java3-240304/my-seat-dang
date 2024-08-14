package com.matdang.seatdang.waiting.controller;

import com.matdang.seatdang.waiting.entity.CustomerInfo;
import com.matdang.seatdang.waiting.entity.Waiting;
import com.matdang.seatdang.waiting.entity.WaitingStatus;
import com.matdang.seatdang.waiting.repository.WaitingRepository;
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


    @GetMapping
    public String showWaiting(@RequestParam int statusIndex, Model model) {
        List<Waiting> waitings = waitingRepository.findAllByStoreIdAndWaitingStatus(1L,WaitingStatus.findWaiting(statusIndex));
        model.addAttribute("waitings", waitings);
        return "store/waiting/main";
    }

    /**
     * test 실행시 주석 필요
     */
    @PostConstruct
    public void initData() {
        for (long i = 0; i < 10; i++) {
            waitingRepository.save(Waiting.builder()
                    .waitingId(i)
                    .waitingNumber(i)
                    .storeId(1L)
                    .customerInfo(new CustomerInfo(i, "010-1111-1111"))
                    .waitingStatus(WaitingStatus.WAITING)
                    .createdAt(LocalDateTime.now())
                    .visitedTime(null)
                    .build());

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

        for (long i = 10; i < 20; i++) {
            waitingRepository.save(Waiting.builder()
                    .waitingId(i)
                    .waitingNumber(i)
                    .storeId(1L)
                    .customerInfo(new CustomerInfo(i, "010-1111-1111"))
                    .waitingStatus(WaitingStatus.VISITED)
                    .createdAt(LocalDateTime.now())
                    .visitedTime(null)
                    .build());

            waitingRepository.save(Waiting.builder()
                    .waitingId(i)
                    .waitingNumber(i)
                    .storeId(1L)
                    .customerInfo(new CustomerInfo(i, "010-1111-1111"))
                    .waitingStatus(WaitingStatus.NO_SHOW)
                    .createdAt(LocalDateTime.now())
                    .visitedTime(null)
                    .build());
        }
    }
}
