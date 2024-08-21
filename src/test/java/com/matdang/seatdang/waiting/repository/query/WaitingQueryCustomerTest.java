package com.matdang.seatdang.waiting.repository.query;

import static org.assertj.core.api.Assertions.assertThat;

import com.matdang.seatdang.store.entity.Store;
import com.matdang.seatdang.store.repository.StoreRepository;
import com.matdang.seatdang.waiting.entity.CustomerInfo;
import com.matdang.seatdang.waiting.entity.Waiting;
import com.matdang.seatdang.waiting.entity.WaitingStatus;
import com.matdang.seatdang.waiting.repository.WaitingRepository;
import com.matdang.seatdang.waiting.repository.query.dto.WaitingInfoDto;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class WaitingQueryCustomerTest {
    @Autowired
    private WaitingQueryRepository waitingQueryRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private WaitingRepository waitingRepository;
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
        em.flush();
        em.clear();

        // when
        List<WaitingInfoDto> findResult = waitingQueryRepository.findAllByCustomerIdAndWaitingStatus(
                1L, WaitingStatus.valueOf(status));
        System.out.println("findResult = " + findResult);

        // then
        assertThat(findResult.size()).isEqualTo(1);
        assertThat(findResult).extracting(WaitingInfoDto::getWaitingStatus)
                .containsExactly(WaitingStatus.valueOf(status));
        assertThat(findResult).extracting(WaitingInfoDto::getStoreName).containsExactly("마싯당");
    }
}
