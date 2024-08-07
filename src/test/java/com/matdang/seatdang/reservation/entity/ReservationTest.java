package com.matdang.seatdang.reservation.entity;

import com.matdang.seatdang.menu.repository.MenuRepository;
import com.matdang.seatdang.reservation.repository.ReservationRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReservationTest {
    @Autowired
    private ReservationRepository reservationRepository;

    @DisplayName("Reservation ddl-auto=create 확인")
    @Test
    public void ReservationTest1() {
        //given
        //when
        //then
    }

}