package com.matdang.seatdang.waiting.repository.query;

import static org.assertj.core.api.Assertions.assertThat;

import com.matdang.seatdang.store.entity.Store;
import com.matdang.seatdang.store.repository.StoreRepository;
import com.matdang.seatdang.waiting.entity.CustomerInfo;
import com.matdang.seatdang.waiting.entity.Waiting;
import com.matdang.seatdang.waiting.entity.WaitingStatus;
import com.matdang.seatdang.waiting.entity.WaitingStorage;
import com.matdang.seatdang.waiting.repository.WaitingRepository;
import com.matdang.seatdang.waiting.repository.WaitingStorageRepository;
import com.matdang.seatdang.waiting.repository.query.dto.WaitingInfoDto;
import com.matdang.seatdang.waiting.repository.query.dto.WaitingInfoProjection;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class WaitingQueryCustomerTest {
    @Autowired
    private WaitingQueryRepository waitingQueryRepository;
    @Autowired
    private WaitingStorageRepository waitingStorageRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private WaitingRepository waitingRepository;
    @Autowired
    private EntityManager em;
//
//    @ParameterizedTest
//    @ValueSource(strings = {"WAITING", "VISITED", "CUSTOMER_CANCELED"})
//    @DisplayName("고객 id로 특정 상태의 웨이팅들을 가져오기")
//    void findStoreNamesByCustomerIdAndWaitingStatus(String status) {
//        // given
//        Store storeA = storeRepository.save(Store.builder()
//                .storeName("마싯당")
//                .build());
//
//        Waiting waiting = waitingRepository.save(Waiting.builder()
//                .waitingNumber(1L)
//                .waitingOrder(1L)
//                .storeId(storeA.getStoreId())
//                .customerInfo(new CustomerInfo(1L, "010-1111-1111", ((int) (Math.random() * 3 + 1))))
//                .waitingStatus(WaitingStatus.valueOf(status))
//                .visitedTime(null)
//                .build());
//        Waiting waiting2 = waitingRepository.save(Waiting.builder()
//                .waitingNumber(1L)
//                .waitingOrder(1L)
//                .storeId(storeA.getStoreId())
//                .customerInfo(new CustomerInfo(1L, "010-1111-1111", ((int) (Math.random() * 3 + 1))))
//                .waitingStatus(WaitingStatus.SHOP_CANCELED)
//                .visitedTime(null)
//                .build());
//        em.flush();
//        em.clear();
//
//        PageRequest pageable = PageRequest.of(0, 100);
//
//        // when
//        Page<WaitingInfoDto> findResult = waitingQueryRepository.findAllByCustomerIdAndWaitingStatus(
//                1L, WaitingStatus.valueOf(status), pageable);
//
//        // then
//        assertThat(findResult.getContent().size()).isEqualTo(1);
//        assertThat(findResult.getContent()).extracting(WaitingInfoDto::getWaitingStatus)
//                .containsExactly(WaitingStatus.valueOf(status));
//        assertThat(findResult.getContent()).extracting(WaitingInfoDto::getStoreName).containsExactly("마싯당");
//    }

//    @Test
//    @DisplayName("고객 id로 취소상태의 웨이팅들 가져오기")
//    void findAllByCustomerIdAndCancelStatus() {
//        // given
//        Store storeA = storeRepository.save(Store.builder()
//                .storeName("마싯당")
//                .build());
//
//        for (WaitingStatus status : WaitingStatus.values()) {
//            waitingRepository.save(Waiting.builder()
//                    .waitingNumber(1L)
//                    .waitingOrder(1L)
//                    .storeId(storeA.getStoreId())
//                    .customerInfo(new CustomerInfo(1L, "010-1111-1111", ((int) (Math.random() * 3 + 1))))
//                    .waitingStatus(status)
//                    .visitedTime(null)
//                    .build());
//        }
//        em.flush();
//        em.clear();
//
//        PageRequest pageable = PageRequest.of(0, 100);
//
//        // when
//        Page<WaitingInfoDto> findResult = waitingQueryRepository.findAllByCustomerIdAndCancelStatus(
//                1L, pageable);
//
//        // then
//        assertThat(findResult.getContent().size()).isEqualTo(3);
//        assertThat(findResult.getContent()).extracting(WaitingInfoDto::getWaitingStatus)
//                .containsExactlyInAnyOrder(WaitingStatus.NO_SHOW, WaitingStatus.CUSTOMER_CANCELED,
//                        WaitingStatus.SHOP_CANCELED);
//        assertThat(findResult.getContent()).extracting(WaitingInfoDto::getStoreName).contains("마싯당");
//    }

    @ParameterizedTest
    @ValueSource(strings = {"WAITING", "VISITED", "CUSTOMER_CANCELED"})
    @DisplayName("고객 id로 특정 상태의 웨이팅(기존 + 저장소) 모두 가져오기")
    void findUnionAllByCustomerIdAndWaitingStatus(String status) {
        // given
        Store storeA = storeRepository.save(Store.builder()
                .storeName("마싯당")
                .build());

        Waiting waiting = waitingRepository.save(Waiting.builder()
                .waitingNumber(1L)
                .waitingOrder(1L)
                .storeId(storeA.getStoreId())
                .customerInfo(new CustomerInfo(1L, "010-1111-1111", ((int) (Math.random() * 3 + 1))))
                .waitingStatus(WaitingStatus.valueOf(status))
                .visitedTime(null)
                .build());
        Waiting waiting2 = waitingRepository.save(Waiting.builder()
                .waitingNumber(1L)
                .waitingOrder(1L)
                .storeId(storeA.getStoreId())
                .customerInfo(new CustomerInfo(1L, "010-1111-1111", ((int) (Math.random() * 3 + 1))))
                .waitingStatus(WaitingStatus.SHOP_CANCELED)
                .visitedTime(null)
                .build());

        WaitingStorage waiting3 = waitingStorageRepository.save(WaitingStorage.builder()
                .waitingNumber(1L)
                .waitingOrder(1L)
                .storeId(storeA.getStoreId())
                .customerInfo(new CustomerInfo(1L, "010-1111-1111", ((int) (Math.random() * 3 + 1))))
                .waitingStatus(WaitingStatus.valueOf(status))
                .visitedTime(null)
                .build());
        WaitingStorage waiting4 = waitingStorageRepository.save(WaitingStorage.builder()
                .waitingNumber(1L)
                .waitingOrder(1L)
                .storeId(storeA.getStoreId())
                .customerInfo(new CustomerInfo(1L, "010-1111-1111", ((int) (Math.random() * 3 + 1))))
                .waitingStatus(WaitingStatus.SHOP_CANCELED)
                .visitedTime(null)
                .build());
        em.flush();
        em.clear();

        PageRequest pageable = PageRequest.of(0, 10);

        // when
        Page<WaitingInfoProjection> findResult = waitingQueryRepository.findUnionAllByCustomerIdAndWaitingStatus(
                1L, WaitingStatus.valueOf(status), pageable);

        // then
        assertThat(findResult.getTotalElements()).isEqualTo(2);
        assertThat(findResult.getContent()).extracting(WaitingInfoProjection::getWaitingStatus)
                .containsOnly(WaitingStatus.valueOf(status));
        assertThat(findResult.getContent()).extracting(WaitingInfoProjection::getStoreName).containsOnly("마싯당");
    }

    @Test
    @DisplayName("고객 id로 취소상태의 웨이팅(기존 + 저장소) 모두 가져오기")
    void findUnionAllByCustomerIdAndCancelStatus() {
        // given
        Store storeA = storeRepository.save(Store.builder()
                .storeName("마싯당")
                .build());

        for (WaitingStatus status : WaitingStatus.values()) {
            waitingRepository.save(Waiting.builder()
                    .waitingNumber(1L)
                    .waitingOrder(1L)
                    .storeId(storeA.getStoreId())
                    .customerInfo(new CustomerInfo(1L, "010-1111-1111", ((int) (Math.random() * 3 + 1))))
                    .waitingStatus(status)
                    .visitedTime(null)
                    .build());
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

        PageRequest pageable = PageRequest.of(0, 10);

        // when
        Page<WaitingInfoProjection> findResult = waitingQueryRepository.findUnionAllByCustomerIdAndCancelStatus(
                1L, pageable);

        // then
        assertThat(findResult.getTotalElements()).isEqualTo(3+3);
        assertThat(findResult.getContent()).extracting(WaitingInfoProjection::getWaitingStatus)
                .containsOnly(WaitingStatus.NO_SHOW, WaitingStatus.CUSTOMER_CANCELED,
                        WaitingStatus.SHOP_CANCELED);
        assertThat(findResult.getContent()).extracting(WaitingInfoProjection::getStoreName).containsOnly("마싯당");
    }
}
