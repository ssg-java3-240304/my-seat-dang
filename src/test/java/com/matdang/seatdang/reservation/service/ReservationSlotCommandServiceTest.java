package com.matdang.seatdang.reservation.service;

import com.matdang.seatdang.reservation.dto.ReservationTicketRequestDTO;
import com.matdang.seatdang.reservation.entity.ReservationSlot;
import com.matdang.seatdang.reservation.repository.ReservationSlotRepository;
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
class ReservationSlotCommandServiceTest {
    @Autowired
    private ReservationSlotCommandService reservationSlotCommandService;
    @Autowired
    private ReservationSlotRepository reservationSlotRepository;

    @Transactional
    @DisplayName("예약제한 5개인 자원에 동시에 10개의 요청이 발생했을때 처리")
    @Test
    public void test1() throws Exception {
        //given
        int numberOfRequests = 100;
        int maxReservation = 5;
        Long storeId = 1L;
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.of(12, 30);

//        reservationSlotRepository.save(new ReservationSlot(storeId, date, time, maxReservation));

        //동시 요청을 시뮬레이션을 하기 위한 Executor 준비
        ExecutorService executor = Executors.newFixedThreadPool(numberOfRequests);
        CountDownLatch latch = new CountDownLatch(numberOfRequests);

        //요청을 만드는 callable 정의
        Callable<ReservationTicket> requestTask = () ->{
            try{
                return reservationSlotCommandService.getReservationTicket(new ReservationTicketRequestDTO(storeId, date, time, maxReservation));
            }finally {
                latch.countDown();
            }
        };

        //작업 제출
        Future<ReservationTicket>[] futures = new Future[numberOfRequests];
        for (int i = 0; i < numberOfRequests; i++) {
            futures[i] = executor.submit(requestTask);
        }

        // 모든 작업이 세팅될때까지 대기
        latch.await();

        //when
        int availableCnt = 0;
        for(Future<ReservationTicket> future : futures){
            if(future.get() == ReservationTicket.AVAILABLE){
                availableCnt++;
            }
        }
        //then
        System.out.println(availableCnt);
        assertThat(availableCnt).isEqualTo(maxReservation);
        executor.shutdown();
    }

    @DisplayName("예약 티켓 1건 발권")
    @Test
    public void test2() {
        //given
        int maxReservation = 5;
        Long storeId = 1L;
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.of(12, 30);

        //when
        ReservationTicket ticket = reservationSlotCommandService.getReservationTicket(new ReservationTicketRequestDTO(storeId, date, time, maxReservation));
        //then
        assertThat(ticket).isEqualTo(ReservationTicket.AVAILABLE);
    }
}