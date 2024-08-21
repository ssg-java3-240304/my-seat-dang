package com.matdang.seatdang.waiting.service;

import com.matdang.seatdang.auth.service.AuthService;
import com.matdang.seatdang.member.entity.Customer;
import com.matdang.seatdang.member.entity.Member;
import com.matdang.seatdang.store.entity.Store;
import com.matdang.seatdang.store.repository.StoreRepository;
import com.matdang.seatdang.waiting.entity.CustomerInfo;
import com.matdang.seatdang.waiting.entity.Waiting;
import com.matdang.seatdang.waiting.entity.WaitingStatus;
import com.matdang.seatdang.waiting.repository.WaitingRepository;
import com.matdang.seatdang.waiting.repository.query.dto.WaitingInfoDto;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@Transactional
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class WaitingCustomerServiceTest {
    @Autowired
    private WaitingCustomerService waitingCustomerService;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private WaitingRepository waitingRepository;
    @Autowired
    private EntityManager em;
    @MockBean
    private AuthService authService;


    @Test
    @DisplayName("웨이팅 등록")
    void createWaiting() {
        // given
        Store storeA = storeRepository.save(Store.builder()
                .build());
        for (long i = 0; i < 5; i++) {
            waitingRepository.save(Waiting.builder()
                    .waitingNumber(i)
                    .waitingOrder(i)
                    .storeId(storeA.getStoreId())
                    .customerInfo(new CustomerInfo(i, "010-1111-1111", ((int) (Math.random() * 3 + 1))))
                    .waitingStatus(WaitingStatus.WAITING)
                    .visitedTime(null)
                    .build());
        }
        for (long i = 5; i < 10; i++) {
            waitingRepository.save(Waiting.builder()
                    .waitingNumber(i)
                    .waitingOrder(i)
                    .storeId(storeA.getStoreId())
                    .customerInfo(new CustomerInfo(i, "010-1111-1111", ((int) (Math.random() * 3 + 1))))
                    .waitingStatus(WaitingStatus.VISITED)
                    .visitedTime(null)
                    .build());
        }

        Store storeB = storeRepository.save(Store.builder()
                .build());
        for (long i = 0; i < 5; i++) {
            waitingRepository.save(Waiting.builder()
                    .waitingNumber(i)
                    .waitingOrder(i)
                    .storeId(storeB.getStoreId())
                    .customerInfo(new CustomerInfo(i, "010-1111-1111", ((int) (Math.random() * 3 + 1))))
                    .waitingStatus(WaitingStatus.WAITING)
                    .visitedTime(null)
                    .build());
        }
        em.flush();
        em.clear();

        Customer mockCustomer = Customer.builder()
                .memberId(50L)
                .memberPhone("010-1234-1234")
                .build();
        when(authService.getAuthenticatedMember()).thenReturn(mockCustomer);
        // when
        waitingCustomerService.createWaiting(storeA.getStoreId(), 2);
        em.flush();
        em.clear();
        List<Waiting> findResult = waitingRepository.findAllByCustomerId(50L);

        // then

        assertThat(findResult.size()).isEqualTo(1);
        assertThat(findResult.get(0).getWaitingOrder()).isEqualTo(5L);
        assertThat(findResult.get(0).getWaitingNumber()).isEqualTo(10L);
        assertThat(findResult.get(0).getCustomerInfo().getCustomerPhone()).isEqualTo("010-1234-1234");

    }

    @ParameterizedTest
    @CsvSource(value = {"0,10","1,10", "2,30"})
    @DisplayName("웨이팅 상태별 조회")
    void showWaiting(int status, int size) {
        Store storeA = storeRepository.save(Store.builder()
                        .storeName("마싯당")
                .build());
        // given
        {
            long i = 0;
            for (WaitingStatus value : WaitingStatus.values()) {
                for (int j = 0; j < 10; j++, i++) {
                    waitingRepository.save(Waiting.builder()
                            .waitingNumber(i)
                            .waitingOrder(i)
                            .storeId(storeA.getStoreId())
                            .customerInfo(new CustomerInfo(1L, "010-1111-1111", ((int) (Math.random() * 3 + 1))))
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
                    .storeId(2L)
                    .customerInfo(new CustomerInfo(2L, "010-1111-1111", ((int) (Math.random() * 3 + 1))))
                    .waitingStatus(WaitingStatus.WAITING)
                    .visitedTime(null)
                    .build());
        }
        em.flush();
        em.clear();

        Customer mockCustomer = Customer.builder()
                .memberId(1L)
                .memberPhone("010-1234-1234")
                .build();
        when(authService.getAuthenticatedMember()).thenReturn(mockCustomer);


        // when
        Page<WaitingInfoDto> findResult = waitingCustomerService.showWaiting(status,0);
        System.out.println("findResult = " + findResult);

        // then
        assertThat(findResult.getTotalElements()).isEqualTo(size);
    }
}
