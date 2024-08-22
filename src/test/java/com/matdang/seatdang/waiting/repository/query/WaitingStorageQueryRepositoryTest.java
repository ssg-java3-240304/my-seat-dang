package com.matdang.seatdang.waiting.repository.query;

import static org.assertj.core.api.Assertions.assertThat;

import com.matdang.seatdang.store.entity.Store;
import com.matdang.seatdang.store.repository.StoreRepository;
import com.matdang.seatdang.waiting.entity.CustomerInfo;
import com.matdang.seatdang.waiting.entity.WaitingStatus;
import com.matdang.seatdang.waiting.entity.WaitingStorage;
import com.matdang.seatdang.waiting.repository.WaitingStorageRepository;
import com.matdang.seatdang.waiting.repository.query.dto.WaitingInfoDto;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class WaitingStorageQueryRepositoryTest {
    @Autowired
    private WaitingStorageQueryRepository waitingStorageQueryRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private WaitingStorageRepository waitingStorageRepository;
    @Autowired
    private EntityManager em;

    @ParameterizedTest
    @ValueSource(strings = {"WAITING", "VISITED", "CUSTOMER_CANCELED"})
    @DisplayName("고객 id로 특정 상태의 웨이팅들을 가져오기")
    void findStoreNamesByCustomerIdAndWaitingStatus(String status) {
        // given
        Store storeA = storeRepository.save(Store.builder()
                .storeName("마싯당")
                .build());

        WaitingStorage waiting = waitingStorageRepository.save(WaitingStorage.builder()
                .waitingNumber(1L)
                .waitingOrder(1L)
                .storeId(storeA.getStoreId())
                .customerInfo(new CustomerInfo(1L, "010-1111-1111", ((int) (Math.random() * 3 + 1))))
                .waitingStatus(WaitingStatus.valueOf(status))
                .visitedTime(null)
                .build());
        WaitingStorage waiting2 = waitingStorageRepository.save(WaitingStorage.builder()
                .waitingNumber(1L)
                .waitingOrder(1L)
                .storeId(storeA.getStoreId())
                .customerInfo(new CustomerInfo(1L, "010-1111-1111", ((int) (Math.random() * 3 + 1))))
                .waitingStatus(WaitingStatus.SHOP_CANCELED)
                .visitedTime(null)
                .build());
        em.flush();
        em.clear();

        PageRequest pageable = PageRequest.of(0, 100);

        // when
        Page<WaitingInfoDto> findResult = waitingStorageQueryRepository.findAllByCustomerIdAndWaitingStatus(
                1L, WaitingStatus.valueOf(status), pageable);

        // then
        assertThat(findResult.getContent().size()).isEqualTo(1);
        assertThat(findResult.getContent()).extracting(WaitingInfoDto::getWaitingStatus)
                .containsExactly(WaitingStatus.valueOf(status));
        assertThat(findResult.getContent()).extracting(WaitingInfoDto::getStoreName).containsExactly("마싯당");
    }

    @Test
    @DisplayName("고객 id로 취소상태의 웨이팅들 가져오기")
    void findAllByCustomerIdAndCancelStatus() {
        // given
        Store storeA = storeRepository.save(Store.builder()
                .storeName("마싯당")
                .build());

        for (WaitingStatus status : WaitingStatus.values()) {
            waitingStorageRepository.save(WaitingStorage.builder()
                    .waitingNumber(1L)
                    .waitingOrder(1L)
                    .storeId(storeA.getStoreId())
                    .customerInfo(new CustomerInfo(1L, "010-1111-1111", ((int) (Math.random() * 3 + 1))))
                    .waitingStatus(status)
                    .visitedTime(null)
                    .build());
        }
        em.flush();
        em.clear();

        PageRequest pageable = PageRequest.of(0, 100);

        // when
        Page<WaitingInfoDto> findResult = waitingStorageQueryRepository.findAllByCustomerIdAndCancelStatus(
                1L, pageable);

        // then
        assertThat(findResult.getContent().size()).isEqualTo(3);
        assertThat(findResult.getContent()).extracting(WaitingInfoDto::getWaitingStatus)
                .containsExactlyInAnyOrder(WaitingStatus.NO_SHOW, WaitingStatus.CUSTOMER_CANCELED,
                        WaitingStatus.SHOP_CANCELED);
        assertThat(findResult.getContent()).extracting(WaitingInfoDto::getStoreName).contains("마싯당");
    }

    @ParameterizedTest
    @ValueSource(strings = {"WAITING", "VISITED"})
    @DisplayName("고객id로 특정 웨이팅 상태 개수 찾기")
    void countWaitingStorageByCustomerIdAndWaitingStatus(String status) {
        // given
        Store storeA = storeRepository.save(Store.builder()
                .storeName("마싯당")
                .build());

        for (WaitingStatus waitingStatus : WaitingStatus.values()) {
            waitingStorageRepository.save(WaitingStorage.builder()
                    .waitingNumber(1L)
                    .waitingOrder(1L)
                    .storeId(storeA.getStoreId())
                    .customerInfo(new CustomerInfo(1L, "010-1111-1111", ((int) (Math.random() * 3 + 1))))
                    .waitingStatus(waitingStatus)
                    .visitedTime(null)
                    .build());
        }
        em.flush();
        em.clear();

        // when
        int findResult = waitingStorageQueryRepository.countWaitingStorageByCustomerIdAndWaitingStatus(1L,
                WaitingStatus.valueOf(status));

        // then
        assertThat(findResult).isEqualTo(1);
    }

    @Test
    @DisplayName("고객id로 취소 상태 웨이팅 개수 찾기")
    void countWaitingStorageByCustomerIdAndCancelStatus() {
        // given
        Store storeA = storeRepository.save(Store.builder()
                .storeName("마싯당")
                .build());

        for (WaitingStatus waitingStatus : WaitingStatus.values()) {
            waitingStorageRepository.save(WaitingStorage.builder()
                    .waitingNumber(1L)
                    .waitingOrder(1L)
                    .storeId(storeA.getStoreId())
                    .customerInfo(new CustomerInfo(1L, "010-1111-1111", ((int) (Math.random() * 3 + 1))))
                    .waitingStatus(waitingStatus)
                    .visitedTime(null)
                    .build());
        }
        em.flush();
        em.clear();

        // when
        int findResult = waitingStorageQueryRepository.countWaitingStorageByCustomerIdAndCancelStatus(1L);

        // then
        assertThat(findResult).isEqualTo(3);
    }
}