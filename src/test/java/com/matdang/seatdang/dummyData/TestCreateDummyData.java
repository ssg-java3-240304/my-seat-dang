package com.matdang.seatdang.dummyData;

import com.matdang.seatdang.auth.service.AuthService;
import com.matdang.seatdang.common.storeEnum.StoreType;
import com.matdang.seatdang.member.entity.*;
import com.matdang.seatdang.member.repository.MemberRepository;
import com.matdang.seatdang.member.vo.StoreVo;
import com.matdang.seatdang.store.entity.Store;
import com.matdang.seatdang.store.repository.StoreRepository;
import com.matdang.seatdang.store.vo.Status;
import com.matdang.seatdang.store.vo.StoreSetting;
import com.matdang.seatdang.store.vo.WaitingStatus;
import com.matdang.seatdang.store.vo.WaitingTime;
import com.matdang.seatdang.waiting.entity.CustomerInfo;
import com.matdang.seatdang.waiting.entity.WaitingStorage;
import com.matdang.seatdang.waiting.repository.WaitingStorageRepository;
import com.matdang.seatdang.waiting.service.facade.RedissonLockWaitingCustomerFacade;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

//@Disabled
@SpringBootTest
public class TestCreateDummyData {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private RedissonLockWaitingCustomerFacade redissonLockWaitingCustomerFacade;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private WaitingStorageRepository waitingStorageRepository;
    @MockBean
    private AuthService authService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;



    static Stream<Arguments> customerProvider() {
        String[] names = {
                "김민준", "이서윤", "박서연", "최지후", "김도윤", "이주원", "박지호", "김서준", "정예린", "박하준",
                "이하은", "김수빈", "박현우", "정지아", "최하늘", "김예준", "이하진", "박채원", "정민서", "최윤서",
                "김현우", "이지안", "박예원", "정다인", "최민재", "김지우", "이준서", "박지민", "정지원", "최수아",
                "김지훈", "이하율", "박서현", "정예원", "최다은", "김서영", "이하린", "박서윤", "정하율", "최지호",
                "김도현", "이준우", "박채연", "정수빈", "최윤아", "김민서", "이지후", "박서하", "정지우", "최하윤",
                "김주원", "이서현", "박하린", "정예린", "최민서", "김예린", "이지아", "박하율", "정다은", "최서연",
                "김하은", "이하영", "박채윤", "정서윤", "최하린", "김현서", "이지훈", "박하진", "정하은", "최도윤",
                "김주호", "이도윤", "박서우", "정민주", "최주원", "김예은", "이지은", "박하은", "정수현", "최다윤",
                "김하율", "이서영", "박채은", "정하윤", "최민호", "김채원", "이지안", "박서은", "정지호", "최하준",
                "김다은", "이주하", "박서린", "정서현", "최하영", "김주은", "이지훈", "박하윤", "정수아", "최도현"
        };
        return IntStream.rangeClosed(1, 100).mapToObj(i -> {
            String email = "customer" + i + "@naver.com";
            String name = names[i - 1];
            return Arguments.of(email, name);
        });
    }

    @DisplayName("일반 회원 더미 데이터 세팅")
    @ParameterizedTest
    @MethodSource("customerProvider")
    public void Test1(String email, String memberName) {
        //given
        Customer customer = Customer.builder()
                .memberEmail(email)
                .joinDate(LocalDate.now())
                .memberName(memberName)
                .memberPassword(bCryptPasswordEncoder.encode("1234"))
                .memberPhone(generateRandomKoreanPhoneNumber())
                .memberRole(MemberRole.ROLE_CUSTOMER)
                .memberStatus(MemberStatus.APPROVED)
                .imageGenLeft(5)
                .customerGender(Gender.MALE)
                .customerBirthday(LocalDate.of(1990, 1, 1))
                .customerNickName("미식가" + memberName)
                .customerProfileImage("https://kr.object.ncloudstorage.com/myseatdang-bucket/profile-images/1/abstract-user-flat-4.png")
                .build();
        //when
        Customer savedCustomer = (Customer) memberRepository.save(customer);
        //then
        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getMemberId()).isNotNull();
    }

    private static String generateRandomKoreanPhoneNumber() {
        int exchangeCode = new Random().nextInt(9000) + 1000;
        int subscriberNumber = new Random().nextInt(9000) + 1000;

        return String.format("010-%04d-%04d", exchangeCode, subscriberNumber);
    }

    @Disabled
    @Test
    @DisplayName("웨이팅 오늘 데이터 생성")
    void createWaitingToday() {
        RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
        connection.flushAll(); // 모든 데이터 삭제

        for (long i = 1; i <= 15; i++) {
            Customer mockCustomer = Customer.builder()
                    .memberId(i)
                    .memberPhone(generateRandomKoreanPhoneNumber())
                    .build();
            when(authService.getAuthenticatedMember()).thenReturn(mockCustomer);
            redissonLockWaitingCustomerFacade.createWaiting(1L, ((int) (Math.random() * 3 + 1)));
        }

    }

    @Test
    @DisplayName("웨이팅 기록 데이터 생성")
    void createWaitingHistory() {
        long i = 51L;
        for (com.matdang.seatdang.waiting.entity.WaitingStatus value : com.matdang.seatdang.waiting.entity.WaitingStatus.values()) {
            if (value != com.matdang.seatdang.waiting.entity.WaitingStatus.WAITING) {

                for (int j = 0; j < 10; j++, i++) {
                    waitingStorageRepository.save(WaitingStorage.builder()
                            .waitingNumber(i)
                            .waitingOrder(i)
                            .storeId(1L)
                            .customerInfo(new CustomerInfo(1L, "010-1111-1111", ((int) (Math.random() * 3 + 1))))
                            .createdDate(LocalDateTime.now())
                            .waitingStatus(value)
                            .visitedTime(null)
                            .build());
                }
            }
        }
    }

    @DisplayName("상점 더미데이터 생성 (6개만)")
    @Test
    public void test2() {
        //given
        String csvFile = "csv/store_sample_seoul.csv"; // CSV 파일 경로 설정
        String[] suffixes = {"에그타르트", "브라우니", "케이크", "에클레어", "마카롱", "소라빵", "단팥빵", "식빵"};
        Random random = new Random();
        int storeTypeFixer = 0;

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(csvFile);
             CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {

            reader.readNext();

            String[] line;
            while ((line = reader.readNext()) != null && storeTypeFixer < 6) {
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
                // StoreType의 모든 값 중 하나를 무작위로 선택
                StoreType[] storeTypes = StoreType.values();
                StoreType randomStoreType = storeTypes[random.nextInt(storeTypes.length)];
                // 수정된 사업장명과 함께 데이터 출력
                System.out.println("사업장명: " + 사업장명);
                System.out.println("도로명주소: " + 도로명주소);
                System.out.println("지번주소: " + 지번주소);
                System.out.println("전화번호: " + 전화번호);
                System.out.println("좌표정보(X): " + 좌표정보X);
                System.out.println("좌표정보(Y): " + 좌표정보Y);
                System.out.println("---------------");
                String addr = String.format(도로명주소);
                String name = String.format(사업장명);
                List<String> imageList = List.of(
                        "https://kr.object.ncloudstorage.com/myseatdang-bucket/sample-folder/0a2e250f-1a1a-4805-bf17-559d7c4105cf.png");

                Store store;
                if (storeTypeFixer <= 1) {
                    store = Store.builder()
                            .storeName(name)
                            .storeAddress(addr)
                            .images(imageList)
                            .storeType(StoreType.GENERAL_WAITING)
                            .openTime(LocalTime.of(8, 0))
                            .startBreakTime(LocalTime.of(15, 0))
                            .endBreakTime(LocalTime.of(16, 0))
                            .lastOrder(LocalTime.of(21, 30))
                            .closeTime(LocalTime.of(22, 0))
                            .regularDayOff("목요일")
                            .thumbnail("https://kr.object.ncloudstorage.com/myseatdang-bucket/menu/99397_60912_2445.jpg")
                            .storeSetting(new StoreSetting())
                            .notice("Closed on public holidays.")
                            .phone("555-1234-567")
                            .starRating(4.5)
                            .storeSetting(StoreSetting.builder()
                                    .reservationOpenTime(LocalTime.of(10, 0))
                                    .reservationCloseTime(LocalTime.of(20, 0))
                                    .reservationStatus(Status.OFF)
                                    .waitingTime(new WaitingTime(LocalTime.of(10, 0), LocalTime.of(20, 0),
                                            LocalTime.of(0, 10)))
                                    .waitingStatus(WaitingStatus.OPEN)
                                    .waitingTime(WaitingTime.builder()
                                            .waitingOpenTime(LocalTime.of(9, 0))
                                            .waitingCloseTime(LocalTime.of(23, 0))
                                            .estimatedWaitingTime(LocalTime.of(0, 10))
                                            .build())
                                    .maxReservationInDay(1000)
                                    .maxReservationInTime(5)
                                    .build()
                            )
                            .build();
                } else if (2 <= storeTypeFixer && storeTypeFixer < 4) {
                    store = Store.builder()
                            .storeName(name)
                            .storeAddress(addr)
                            .images(imageList)
                            .storeType(StoreType.GENERAL_RESERVATION)
                            .openTime(LocalTime.of(8, 0))
                            .startBreakTime(LocalTime.of(15, 0))
                            .endBreakTime(LocalTime.of(16, 0))
                            .lastOrder(LocalTime.of(21, 30))
                            .closeTime(LocalTime.of(22, 0))
                            .regularDayOff("금요일")
                            .thumbnail("https://cdn.foodnews.co.kr/news/photo/202010/75572_32980_2446.jpg")
                            .description("사전 주문 케이크만 판매 합니다. 예약 후 방문 해주세요")
                            .notice("Closed on public holidays.")
                            .phone("555-1234-567")
                            .starRating(4.5)
                            .storeSetting(StoreSetting.builder()
                                    .reservationOpenTime(LocalTime.of(10, 0))
                                    .reservationCloseTime(LocalTime.of(20, 0))
                                    .reservationStatus(Status.ON)
                                    .waitingTime(new WaitingTime(LocalTime.of(10, 0), LocalTime.of(20, 0),
                                            LocalTime.of(0, 10)))
                                    .waitingStatus(WaitingStatus.UNAVAILABLE)
                                    .maxReservationInDay(1000)
                                    .maxReservationInTime(5)
                                    .build()
                            )
                            .build();
                } else {
                    store = Store.builder()
                            .storeName(name)
                            .storeAddress(addr)
                            .images(imageList)
                            .storeType(StoreType.CUSTOM)
                            .openTime(LocalTime.of(8, 0))
                            .startBreakTime(LocalTime.of(15, 0))
                            .endBreakTime(LocalTime.of(16, 0))
                            .lastOrder(LocalTime.of(21, 30))
                            .closeTime(LocalTime.of(22, 0))
                            .regularDayOff("Sunday")
                            .thumbnail(
                                    "https://mblogthumb-phinf.pstatic.net/MjAyMzAyMTZfODAg/MDAxNjc2NTUxMTc4OTgw.b8ib9BJyeXqkY2AdlAPsqw-sbrbyEALUtia9w2Ch3igg.J0Hhf7VXwYwuw4JIb6CJsD2t0lbLQNzOxVhRJNtMcCAg.JPEG.ok-cake/IMG_5088.jpg?type=w800")
                            .description("특별한 날에 걸맞는 특별한 케이크를 준비 해드립니다")
                            .notice("공휴일에는 쉽니다")
                            .phone("555-1234-567")
                            .starRating(4.5)
                            .storeSetting(StoreSetting.builder()
                                    .reservationOpenTime(LocalTime.of(10, 0))
                                    .reservationCloseTime(LocalTime.of(20, 0))
                                    .reservationStatus(Status.ON)
                                    .waitingTime(new WaitingTime(LocalTime.of(10, 0), LocalTime.of(20, 0),
                                            LocalTime.of(0, 10)))
                                    .waitingStatus(WaitingStatus.UNAVAILABLE)
                                    .maxReservationInDay(1000)
                                    .maxReservationInTime(5)
                                    .build()
                            )
                            .build();
                }
                //when
                store = storeRepository.save(store);
                storeTypeFixer++;
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }

    //각각 마싯당1~6호점 점주
    //마싯당1~2호점 = 웨이팅
    //마싯당3~4호점 = 일반주문
    //마싯당5~6호점 = 주문제작
    @CsvSource({
            "waiting1@naver.com, 1, '마싯당'",
            "waiting2@naver.com, 2, '김나경'",
            "general1@naver.com, 3, '박정은'",
            "general2@naver.com, 4, '변성일'",
            "custom1@naver.com, 5, '이용준'",
            "custom2@naver.com, 6, '뽀로로'"
    })
    @DisplayName("상점 회원과 상점 더미데이터 세팅")
    @ParameterizedTest
    public void test3(String email, Long storeId, String memberName) {
        //given
        Store store = storeRepository.findByStoreId(storeId);
        StoreVo storeVo = new StoreVo(store.getStoreId(), store.getStoreName(), store.getStoreType(),
                store.getStoreAddress());
        StoreOwner storeOwner = StoreOwner.builder()
                .memberEmail(email)
                .joinDate(LocalDate.now())
                .memberName(memberName)
                .memberPassword(bCryptPasswordEncoder.encode("1234"))
                .memberPhone("010-1234-5678")
                .memberRole(MemberRole.ROLE_STORE_OWNER)
                .memberStatus(MemberStatus.APPROVED)
                .businessLicenseImage("business_license.jpg")
                .businessLicense("123-45-67890")
                .bankAccountCopy("bank_account.jpg")
                .bankAccount("123-456-789")
                .storeOwnerProfileImage("profile.jpg")
                .store(storeVo)
                .build();
        //when
        StoreOwner savedStoreOwner = (StoreOwner) memberRepository.save(storeOwner);
        //then
        assertThat(savedStoreOwner).isNotNull();
        assertThat(savedStoreOwner.getMemberId()).isNotNull();
    }


    //**********주의**********
    //실행시 PC가 정지할수 있습니다
    @Disabled
    @DisplayName("모든 상점 더미데이터 생성")
    @Test
    public void test4() {
        //given
        String csvFile = "csv/store_sample_seoul.csv"; // CSV 파일 경로 설정
        String[] suffixes = {"에그타르트", "브라우니", "케이크", "에클레르", "마카롱", "소라빵", "단팥빵", "식빵"};
        Random random = new Random();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(csvFile);
             CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {

            int cnt = 0;
            while (cnt < 6) {
                reader.readNext();
                cnt++;
            }

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
                String modified사업장명 = "The " + randomSuffix + " " + 사업장명;
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
                List<String> imageList = List.of(
                        "https://kr.object.ncloudstorage.com/myseatdang-bucket/sample-folder/0a2e250f-1a1a-4805-bf17-559d7c4105cf.png");

                Store store;
                if (randomStoreType.equals(StoreType.GENERAL_WAITING)) {
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
                                    .reservationOpenTime(LocalTime.of(10, 0))
                                    .reservationCloseTime(LocalTime.of(20, 0))
                                    .reservationStatus(Status.OFF)
                                    .waitingTime(new WaitingTime(LocalTime.of(10, 0), LocalTime.of(20, 0), LocalTime.of(0, 10)))
                                    .waitingStatus(WaitingStatus.OPEN)
                                    .build()
                            )
                            .build();
                } else if (randomStoreType.equals(StoreType.GENERAL_RESERVATION)) {
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
                                    .reservationOpenTime(LocalTime.of(10, 0))
                                    .reservationCloseTime(LocalTime.of(20, 0))
                                    .reservationStatus(Status.ON)
                                    .waitingTime(new WaitingTime(LocalTime.of(10, 0), LocalTime.of(20, 0), LocalTime.of(0, 10)))
                                    .waitingStatus(WaitingStatus.UNAVAILABLE)
                                    .build()
                            )
                            .build();
                } else {
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
                            .regularDayOff("일요일")
                            .thumbnail("https://kr.object.ncloudstorage.com/myseatdang-bucket/sample-folder/0a2e250f-1a1a-4805-bf17-559d7c4105cf.png")
                            .description("A cozy place to enjoy specialty coffee and pastries.")
                            .notice("Closed on public holidays.")
                            .phone("555-1234-567")
                            .starRating(4.5)
                            .storeSetting(StoreSetting.builder()
                                    .reservationOpenTime(LocalTime.of(10, 0))
                                    .reservationCloseTime(LocalTime.of(20, 0))
                                    .reservationStatus(Status.ON)
                                    .waitingTime(new WaitingTime(LocalTime.of(10, 0), LocalTime.of(20, 0), LocalTime.of(0, 10)))
                                    .waitingStatus(WaitingStatus.UNAVAILABLE)
                                    .build()
                            )
                            .build();
                }
                //when
                store = storeRepository.save(store);
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }
}
