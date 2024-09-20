package com.matdang.seatdang.reservation.service;

import com.matdang.seatdang.reservation.dto.ReservationTicketRequestDTO;
import com.matdang.seatdang.reservation.vo.ReservationTicket;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReservationSlotLockFacadeTest {
    @Autowired
    private ReservationSlotLockFacade reservationSlotLockFacade;
    @Autowired
    private ReservationSlotCommandService reservationSlotCommandService;

    @Transactional
    @DisplayName("1건 예약슬롯 획득")
    @Test
    public void test1() {
        //given
        int numberOfRequests = 1;
        int maxReservation = 5;
        Long storeId = 1L;
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.of(12, 30);

        //동시 요청을 시뮬레이션을 하기 위한 Executor 준비
        ExecutorService executor = Executors.newFixedThreadPool(numberOfRequests);
        CountDownLatch latch = new CountDownLatch(numberOfRequests);

        //요청을 만드는 callable 정의
        Callable<ReservationTicket> requestTask = () ->{
            try{
                return reservationSlotLockFacade.getTicket(new ReservationTicketRequestDTO(storeId, date, time, maxReservation));
            }finally {
                latch.countDown();
            }
        };

        //작업 제출
        Future<ReservationTicket>[] futures = new Future[numberOfRequests];
        for (int i = 0; i < numberOfRequests; i++) {
            futures[i] = executor.submit(requestTask);
        }

        //when

        int availableCnt = 0;
        try {
            // 모든 작업이 세팅될때까지 대기
            latch.await();

            for (Future<ReservationTicket> future : futures) {
                if (future.get() == ReservationTicket.AVAILABLE) {
                    availableCnt++;
                }
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
        //then
        System.out.println(availableCnt);
        assertThat(availableCnt).isEqualTo(1);
        executor.shutdown();
    }

    @Transactional(rollbackFor = Exception.class)
    @DisplayName("100건 동시 예약슬롯 획득 시도")
    @Test
    public void test2() throws InterruptedException {
        //given
        int numberOfRequests = 100;
        int maxReservation = 5;
        Long storeId = 2L;
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.of(12, 30);

        //동시 요청을 시뮬레이션을 하기 위한 Executor 준비
        ExecutorService executor = Executors.newFixedThreadPool(numberOfRequests);
        CountDownLatch latch = new CountDownLatch(numberOfRequests);

        //요청을 만드는 callable 정의
        Callable<ReservationTicket> requestTask = () ->{
            try{
                return reservationSlotLockFacade.getTicket(new ReservationTicketRequestDTO(storeId, date, time, maxReservation));
            }finally {
                latch.countDown();
            }
        };

        //작업 제출
        Future<ReservationTicket>[] futures = new Future[numberOfRequests];
        for (int i = 0; i < numberOfRequests; i++) {
            futures[i] = executor.submit(requestTask);
        }

        //when

        int availableCnt = 0;
        try {
            // 모든 작업이 세팅될때까지 대기
            latch.await();

            for (Future<ReservationTicket> future : futures) {
                if (future.get() == ReservationTicket.AVAILABLE) {
                    availableCnt++;
                }
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
        //then
        System.out.println(availableCnt);
        assertThat(availableCnt).isEqualTo(maxReservation);
        executor.shutdown();
    }
}