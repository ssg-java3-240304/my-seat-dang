package com.matdang.seatdang.waiting.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.matdang.seatdang.search.repository.SearchRepository;
import com.matdang.seatdang.store.entity.Store;
import com.matdang.seatdang.store.repository.StoreRepository;
import com.matdang.seatdang.store.vo.StoreSetting;
import com.matdang.seatdang.waiting.entity.CustomerInfo;
import com.matdang.seatdang.waiting.entity.Waiting;
import com.matdang.seatdang.waiting.entity.WaitingStatus;
import com.matdang.seatdang.waiting.entity.WaitingStorage;
import com.matdang.seatdang.waiting.repository.WaitingRepository;
import com.matdang.seatdang.waiting.repository.WaitingStorageRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class SchedulerServiceTest {
    @Autowired
    private SchedulerService schedulerService;
    @Autowired
    private WaitingRepository waitingRepository;
    @Autowired
    private WaitingStorageRepository waitingStorageRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("웨이팅 종료상태(마감, 이용불가)시 웨이팅 데이터 이동")
    void relocateWaitingData() {
        // given

        Store storeA = storeRepository.save(Store.builder()
                .storeSetting(StoreSetting.builder()
                        .waitingStatus(com.matdang.seatdang.store.vo.WaitingStatus.CLOSE)
                        .build())
                .build());

        Store storeB = storeRepository.save(Store.builder()
                .storeSetting(StoreSetting.builder()
                        .waitingStatus(com.matdang.seatdang.store.vo.WaitingStatus.UNAVAILABLE)
                        .build())
                .build());

        Store storeC = storeRepository.save(Store.builder()
                .storeSetting(StoreSetting.builder()
                        .waitingStatus(com.matdang.seatdang.store.vo.WaitingStatus.OPEN)
                        .build())
                .build());
        {
            long i = 0;
            for (WaitingStatus value : WaitingStatus.values()) {
                for (int j = 0; j < 10; j++, i++) {
                    waitingRepository.save(Waiting.builder()
                            .waitingNumber(i)
                            .waitingOrder(i)
                            .storeId(storeA.getStoreId())
                            .customerInfo(new CustomerInfo(i, "010-1111-1111", ((long) (Math.random() * 3 + 1))))
                            .waitingStatus(value)
                            .visitedTime(null)
                            .build());
                }
            }
        }

        for (long i = 0; i < 10; i++) {
            waitingRepository.save(Waiting.builder()
                    .waitingNumber(i)
                    .waitingOrder(i)
                    .storeId(storeB.getStoreId())
                    .customerInfo(new CustomerInfo(i, "010-1111-1111", ((long) (Math.random() * 3 + 1))))
                    .waitingStatus(WaitingStatus.VISITED)
                    .visitedTime(null)
                    .build());
        }

        for (long i = 0; i < 10; i++) {
            waitingRepository.save(Waiting.builder()
                    .waitingNumber(i)
                    .waitingOrder(i)
                    .storeId(storeC.getStoreId())
                    .customerInfo(new CustomerInfo(i, "010-1111-1111", ((long) (Math.random() * 3 + 1))))
                    .waitingStatus(WaitingStatus.VISITED)
                    .visitedTime(null)
                    .build());
        }

        em.flush();
        em.clear();
        // when
        schedulerService.relocateWaitingData();

        em.flush();
        em.clear();

        List<Waiting> findA = waitingRepository.findAllByStoreId(storeA.getStoreId());
        List<Waiting> findB = waitingRepository.findAllByStoreId(storeB.getStoreId());
        List<Waiting> findC = waitingRepository.findAllByStoreId(storeC.getStoreId());

        List<WaitingStorage> findSA = waitingStorageRepository.findAllByStoreId(storeA.getStoreId());
        List<WaitingStorage> findSB = waitingStorageRepository.findAllByStoreId(storeB.getStoreId());
        List<WaitingStorage> findSC = waitingStorageRepository.findAllByStoreId(storeC.getStoreId());

        // then
        assertThat(findA.size()).isEqualTo(0);
        assertThat(findB.size()).isEqualTo(0);
        assertThat(findC.size()).isEqualTo(10);

        assertThat(findSA.size()).isEqualTo(50);
        assertThat(findSB.size()).isEqualTo(10);
        assertThat(findSC.size()).isEqualTo(0);


    }
}