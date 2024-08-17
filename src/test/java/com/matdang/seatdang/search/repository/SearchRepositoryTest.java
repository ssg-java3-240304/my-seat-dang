package com.matdang.seatdang.search.repository;

import com.matdang.seatdang.store.entity.Store;
import com.matdang.seatdang.store.repository.StoreRepository;
import com.matdang.seatdang.store.vo.Status;
import com.matdang.seatdang.store.vo.StoreSetting;
import com.matdang.seatdang.store.vo.WaitingStatus;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.matdang.seatdang.common.storeEnum.StoreType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

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
        System.out.println(storePage);
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
//        storePage.forEach(System.out::println);
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
        System.out.println(storePage);
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


    static Set<String> storeNames = Set.of("마카롱", "케이크", "에클레어", "식빵", "소라빵");
    static Set<String> storeAddresses = Set.of("선릉", "마포", "노량진", "중랑", "공덕");

    /**
     * storeNames로 스트림을 생성
     * 스트림의 스트림을 생성하고 2차원 스트림을 하나의 스트림으로 만들기 위해 flatMap을 사용
     * storeNames의 각 요소에 대해 storeAddresses 스트림 생성
     * storeAddress와 storeName 쌍에 대해 Arguments.of()를 생성
     */
    static Stream<Arguments> storeDataProvider() {
        return storeNames.stream()
                .flatMap(storeName -> storeAddresses.stream()
                        .map(storeAddress -> Arguments.of(storeName, storeAddress)));
    }

    @DisplayName("매장명+지역명으로 검색")
    @ParameterizedTest
    @MethodSource("storeDataProvider")
    void test6(String storeName, String storeAddress) {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        //when
        Page<Store> storePage = searchRepository.findByStoreNameContainingAndStoreAddressContainingOrderByStoreAddressDesc(storeName, storeAddress, pageable);
        //then
        assertThat(storePage).isNotNull();
        assertThat(storePage.getNumberOfElements()).isEqualTo(storePage.getContent().size());
        assertThat(storePage.getContent()).allMatch(store->
            store.getStoreName().contains(storeName)
                &&store.getStoreAddress().contains(storeAddress)
        );

    }

    @DisplayName("매장 하나 검색")
    @Test
    public void test7() {
        //given
        long id = 2L;
        //when
        Store store = searchRepository.findByStoreId(id);
        //then
        assertThat(store.getStoreId()).isEqualTo(id);
    }

    @Disabled
    @DisplayName("더미데이터 생성")
    @Test
    public void test8() {
        //given
        String csvFile = "C:\\workspace\\my-seat-dang\\src\\main\\resources\\csv\\store_seoul.csv"; // CSV 파일 경로 설정
        String[] suffixes = {"에그타르트", "브라우니", "케이크", "에클레어", "마카롱", "소라빵", "단팥빵", "식빵"};
        Random random = new Random();
        try (CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(csvFile), Charset.forName("EUC-KR")))) {
            // 첫 번째 라인을 읽어서 헤더로 처리, 저장하지 않음
            reader.readNext();

            String[] line;
            while ((line = reader.readNext()) != null) {
                // 해당 열의 인덱스에 따라 데이터 추출
                String 사업장명 = line[18];
                String 도로명주소 = line[16];
                String 지번주소 = line[15];
                String 전화번호 = line[12];
                String 좌표정보X = line[23];
                String 좌표정보Y = line[24];

                // 도로명주소가 비어있거나 null인 경우, 다음 루프로 넘어감
                if (도로명주소 == null || 도로명주소.trim().isEmpty()) {
                    continue;
                }

                // 무작위로 접미사를 선택하여 사업장명 뒤에 추가
                String randomSuffix = suffixes[random.nextInt(suffixes.length)];
                String modified사업장명 = "The " + randomSuffix +" "+사업장명;
                // StoreType의 모든 값 중 하나를 무작위로 선택
                StoreType[] storeTypes = StoreType.values();
                StoreType randomStoreType = storeTypes[random.nextInt(storeTypes.length)];
                // 수정된 사업장명과 함께 데이터 출력
                System.out.println("사업장명: " + modified사업장명);
                System.out.println("도로명주소: " + 도로명주소);
                System.out.println("지번주소: " + 지번주소);
                System.out.println("전화번호: " + 전화번호);
                System.out.println("좌표정보(X): " + 좌표정보X);
                System.out.println("좌표정보(Y): " + 좌표정보Y);
                System.out.println("---------------");
                String addr = String.format(도로명주소);
                String name = String.format(modified사업장명);
                List<String> imageList = List.of("https://kr.object.ncloudstorage.com/myseatdang-bucket/sample-folder/0a2e250f-1a1a-4805-bf17-559d7c4105cf.png");

                Store store;
                if(randomStoreType.equals(StoreType.GENERAL_WAITING)){
                    store = Store.builder()
                            .storeName(name)
                            .storeAddress(addr)
                            .images(imageList)
                            .storeType(randomStoreType)
                            .openTime(LocalTime.of(8, 0))
                            .startBreakTime(LocalTime.of(15, 0))
                            .endBreakTime(LocalTime.of(16, 0))
                            .lastOrder(LocalTime.of(21, 30))
                            .closeTime(LocalTime.of(22, 0))
                            .regularDayOff("Sunday")
                            .thumbnail("https://kr.object.ncloudstorage.com/myseatdang-bucket/sample-folder/0a2e250f-1a1a-4805-bf17-559d7c4105cf.png")
                            .description("A cozy place to enjoy specialty coffee and pastries.")
                            .notice("Closed on public holidays.")
                            .phone("555-1234-567")
                            .starRating(4.5)
                            .storeSetting(StoreSetting.builder()
                                    .reservationOpenTime(LocalTime.of(10,0))
                                    .reservationCloseTime(LocalTime.of(20,0))
                                    .reservationStatus(Status.OFF)
                                    .waitingStatus(WaitingStatus.OPEN)
                                    .build()
                            )
                            .build();
                }else if(randomStoreType.equals(StoreType.GENERAL_RESERVATION)){
                    store = Store.builder()
                            .storeName(name)
                            .storeAddress(addr)
                            .images(imageList)
                            .storeType(randomStoreType)
                            .openTime(LocalTime.of(8, 0))
                            .startBreakTime(LocalTime.of(15, 0))
                            .endBreakTime(LocalTime.of(16, 0))
                            .lastOrder(LocalTime.of(21, 30))
                            .closeTime(LocalTime.of(22, 0))
                            .regularDayOff("Sunday")
                            .thumbnail("https://kr.object.ncloudstorage.com/myseatdang-bucket/sample-folder/0a2e250f-1a1a-4805-bf17-559d7c4105cf.png")
                            .description("A cozy place to enjoy specialty coffee and pastries.")
                            .notice("Closed on public holidays.")
                            .phone("555-1234-567")
                            .starRating(4.5)
                            .storeSetting(StoreSetting.builder()
                                    .reservationOpenTime(LocalTime.of(10,0))
                                    .reservationCloseTime(LocalTime.of(20,0))
                                    .reservationStatus(Status.ON)
                                    .waitingStatus(WaitingStatus.CLOSE)
                                    .build()
                            )
                            .build();
                }else{
                    store = Store.builder()
                            .storeName(name)
                            .storeAddress(addr)
                            .images(imageList)
                            .storeType(randomStoreType)
                            .openTime(LocalTime.of(8, 0))
                            .startBreakTime(LocalTime.of(15, 0))
                            .endBreakTime(LocalTime.of(16, 0))
                            .lastOrder(LocalTime.of(21, 30))
                            .closeTime(LocalTime.of(22, 0))
                            .regularDayOff("Sunday")
                            .thumbnail("https://kr.object.ncloudstorage.com/myseatdang-bucket/sample-folder/0a2e250f-1a1a-4805-bf17-559d7c4105cf.png")
                            .description("A cozy place to enjoy specialty coffee and pastries.")
                            .notice("Closed on public holidays.")
                            .phone("555-1234-567")
                            .starRating(4.5)
                            .storeSetting(StoreSetting.builder()
                                    .reservationOpenTime(LocalTime.of(10,0))
                                    .reservationCloseTime(LocalTime.of(20,0))
                                    .reservationStatus(Status.ON)
                                    .waitingStatus(WaitingStatus.UNAVAILABLE)
                                    .build()
                            )
                            .build();
                }
                //when
                store = searchRepository.save(store);
//                System.out.println(store);
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        //when
        //then
    }
}