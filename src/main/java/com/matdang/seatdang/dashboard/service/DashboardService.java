package com.matdang.seatdang.dashboard.service;

import com.matdang.seatdang.dashboard.dto.WaitingDashboardResponseDto;
import com.matdang.seatdang.waiting.entity.Waiting;
import com.matdang.seatdang.waiting.entity.WaitingStatus;
import com.matdang.seatdang.waiting.repository.WaitingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {
    private final WaitingRepository waitingRepository;

    public Map<LocalDate, Long> getWeeklyWaitingCnt(Long storeId){
        LocalDateTime startOfWeek = LocalDateTime.now().minusWeeks(1).toLocalDate().atStartOfDay();
        LocalDateTime endOfWeek = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999);

//        LocalDateTime endOfWeek = LocalDateTime.now();
//        LocalDateTime startOfWeek = endOfWeek.minusWeeks(1);
        List<Waiting> waitings = waitingRepository.findVisitedWithinCertainPeriod(storeId, startOfWeek, endOfWeek, WaitingStatus.VISITED);

        Map<LocalDate, Long> dailyCounts = new LinkedHashMap<>();
        for (int i = 0; i < 7; i++) {
            LocalDate date = startOfWeek.plusDays(i).toLocalDate();
            dailyCounts.put(date, 0L);
        }

        for (Waiting waiting : waitings) {
            LocalDate visitDate = waiting.getVisitedTime().toLocalDate();
            dailyCounts.put(visitDate, dailyCounts.getOrDefault(visitDate, 0L) + 1);
        }

        return dailyCounts;
    }

//    public long getWeeklyWaitingCnt(Long storeId){
//        LocalDateTime startOfWeek = LocalDateTime.now().minusWeeks(1); // 1주일 이내
//        LocalDateTime endOfWeek = LocalDateTime.now();
//        return waitingRepository.findVisitedWithinCertainPeriod(storeId, startOfWeek, endOfWeek, WaitingStatus.VISITED).size();
//    }

//    public Map<LocalDate, Long> getWeeklyWaitingCnt(Long storeId) {
//        log.debug("storeId = {}", storeId);
//        LocalDateTime endDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999);
//        LocalDateTime startDay = endDay.minusDays(6).withHour(0).withMinute(0).withSecond(0).withNano(0);
//
//        List<WaitingDashboardResponseDto> dailyCounts = waitingRepository.findVisitedWithinCertainPeriod(storeId, startDay, endDay, WaitingStatus.VISITED);
//
//        // 날짜별로 집계하고 빈 날짜 채우기
//        Map<LocalDate, Long> weeklyCounts = new LinkedHashMap<>();
//        for (int i = 0; i < 7; i++) {
//            LocalDate date = startDay.plusDays(i).toLocalDate();
//            weeklyCounts.put(date, 0L);
//        }
//
//        for (WaitingDashboardResponseDto dto : dailyCounts) {
//            weeklyCounts.put(dto.getVisitedTime(), dto.getCnt());
//        }
//
//        return weeklyCounts;
//    }
}
