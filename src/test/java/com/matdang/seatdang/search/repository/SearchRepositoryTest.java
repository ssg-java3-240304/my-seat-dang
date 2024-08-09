package com.matdang.seatdang.search.repository;

import com.matdang.seatdang.menu.repository.MenuRepository;
import com.matdang.seatdang.store.entity.Store;
import com.matdang.seatdang.store.repository.StoreRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.matdang.seatdang.common.storeEnum.StoreType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LOCAL_DATE_TIME;
import static org.assertj.core.api.InstanceOfAssertFactories.LOCAL_TIME;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SearchRepositoryTest {
    @Autowired
    private StoreRepository searchRepository;

    @DisplayName("JPA 확인")
    @Test
    void test1() {
        assertThat(searchRepository).isNotNull();
    }

    @DisplayName("매장명으로 검색")
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5})
    public void test2(int pageNumber) {
        //given
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Store> storePage = searchRepository.findByStoreNameContaining("브라우니", pageable);
        System.out.println(storePage); // Page 1 of 7 containing com.sh.app.menu.entity.Menu instances
        //when
        //then
        System.out.println("조회한 내용 목록 : " + storePage.getContent());
        System.out.println("총 페이지 수 : " + storePage.getTotalPages());
        System.out.println("총 상점 수 : " + storePage.getTotalElements());
        System.out.println("해당 페이지에 표시 될 요소 수 : " + storePage.getSize());
        System.out.println("해당 페이지에 실제 요소 수 : " + storePage.getNumberOfElements());
        System.out.println("첫 페이지 여부 : " + storePage.isFirst());
        System.out.println("마지막 페이지 여부 : " + storePage.isLast());
        System.out.println("정렬 방식 : " + storePage.getSort());
        System.out.println("여러 페이지 중 현재 페이지(인덱스) : " + storePage.getNumber());
        storePage.forEach(System.out::println);
        assertThat(storePage.getNumberOfElements()).isEqualTo(storePage.getContent().size());
        storePage.getContent().forEach(store -> {
            assertThat(store.getStoreName()).contains("브라우니");
        });
    }

    @DisplayName("주소로 검색")
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5})
    public void test3(int pageNumber) {
        //given
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Store> storePage = searchRepository.findByStoreAddressContaining("삼성", pageable);
        System.out.println(storePage); // Page 1 of 7 containing com.sh.app.menu.entity.Menu instances
        //when
        //then
        System.out.println("조회한 내용 목록 : " + storePage.getContent());
        System.out.println("총 페이지 수 : " + storePage.getTotalPages());
        System.out.println("총 상점 수 : " + storePage.getTotalElements());
        System.out.println("해당 페이지에 표시 될 요소 수 : " + storePage.getSize());
        System.out.println("해당 페이지에 실제 요소 수 : " + storePage.getNumberOfElements());
        System.out.println("첫 페이지 여부 : " + storePage.isFirst());
        System.out.println("마지막 페이지 여부 : " + storePage.isLast());
        System.out.println("정렬 방식 : " + storePage.getSort());
        System.out.println("여러 페이지 중 현재 페이지(인덱스) : " + storePage.getNumber());
        storePage.forEach(System.out::println);
        assertThat(storePage.getNumberOfElements()).isEqualTo(storePage.getContent().size());
        storePage.getContent().forEach(store -> {
            assertThat(store.getStoreAddress()).contains("삼성");
        });
    }

    @Disabled
    @DisplayName("매장 생성")
    @Test
    public void test4() {
        //given
        for(int i=0; i<50; i++) {
            String addr = String.format("서울 강남구 삼성로 %d 삼성동 빌딩 19층", i*10);
            String name = String.format("마싯당%d호점", i);
            Store store = Store.builder()
                    .storeName(name)
                    .storeAddress(addr)
                    .images(List.of("https://kr.object.ncloudstorage.com/myseatdang-bucket/sample-folder/0a2e250f-1a1a-4805-bf17-559d7c4105cf.png"))
                    .build();
            //when
            store = searchRepository.save(store);
            System.out.println(store);
        }

        //then
//        assertThat(store.getStoreId()).isNotZero();
    }

    @DisplayName("매장명+지역명으로 검색")
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5})
    void test6(int pageNumber) {
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNumber,pageSize);

    }
}