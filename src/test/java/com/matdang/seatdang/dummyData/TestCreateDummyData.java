package com.matdang.seatdang.dummyData;

import com.matdang.seatdang.auth.service.AuthService;
import com.matdang.seatdang.common.storeEnum.StoreType;
import com.matdang.seatdang.member.entity.*;
import com.matdang.seatdang.member.repository.MemberRepository;
import com.matdang.seatdang.member.vo.StoreVo;
import com.matdang.seatdang.menu.entity.Menu;
import com.matdang.seatdang.menu.repository.MenuRepository;
import com.matdang.seatdang.menu.service.MenuCommandService;
import com.matdang.seatdang.menu.service.MenuQueryService;
import com.matdang.seatdang.menu.vo.CustomMenuOpt;
import com.matdang.seatdang.menu.vo.MenuStatus;
import com.matdang.seatdang.menu.vo.MenuType;
import com.matdang.seatdang.reservation.vo.ReservationStatus;
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
import com.opencsv.CSVReaderBuilder;
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
import java.util.ArrayList;
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
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private MenuQueryService menuQueryService;
    @Autowired
    private MenuCommandService menuCommandService;


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

    private static List<Menu> generateRandomMenuList(Store store){
        List<Menu> menuList = new ArrayList<>();
        String[] cakeImages = {
                "https://kr.object.ncloudstorage.com/myseatdang-bucket/menu/205cake.jpg",
                "https://kr.object.ncloudstorage.com/myseatdang-bucket/menu/hotelcake1.jpg",
                "https://kr.object.ncloudstorage.com/myseatdang-bucket/menu/hotelcake10.jpg",
                "https://kr.object.ncloudstorage.com/myseatdang-bucket/menu/hotelcake2.jpg",
                "https://kr.object.ncloudstorage.com/myseatdang-bucket/menu/hotelcake3.jpg",
                "https://kr.object.ncloudstorage.com/myseatdang-bucket/menu/hotelcake4.jpg",
                "https://kr.object.ncloudstorage.com/myseatdang-bucket/menu/hotelcake5.jpg",
                "https://kr.object.ncloudstorage.com/myseatdang-bucket/menu/hotelcake6.jfif",
                "https://kr.object.ncloudstorage.com/myseatdang-bucket/menu/hotelcake7.jpg",
                "https://kr.object.ncloudstorage.com/myseatdang-bucket/menu/hotelcake8.jpg",
                "https://kr.object.ncloudstorage.com/myseatdang-bucket/menu/hotelcake9.jpg",
        };

        String[] cakeNames = {
                "생크림 딸기듬뿍 케이크",
                "레드 발렌타임 시그니쳐 케이크",
                "망고에 망고 케이크",
                "블루베리 럭셔크 생크림 케이크",
                "레드로즈 블러썸 케이크",
                "딸기가 넘쳐나는 스트로베리 케이크",
                "시그니쳐 레드 케이싱 케이크",
                "하프케이싱 스노우 치즈 케이크",
                "망고 인 더 트로피칼 케이크",
                "복숭아 가득 생과일 케이크",
                "겨울나라 생크림 케이크"
        };

        String[] cakeDesc = {
                "부드러운 바닐라 시트에 신선한 딸기와 생크림이 가득한 딸기 케이크, 매일 새롭게 준비된 달콤한 유혹입니다.",
                "상큼한 레몬과 부드러운 머랭이 어우러진 레몬 머랭 파이, 새콤달콤한 맛으로 입안을 상큼하게 만들어줍니다.",
                "풍부한 아몬드 향과 부드러운 크림이 가득한 아몬드 브리오슈, 한 입 베어 물 때마다 느껴지는 고급스러운 맛.",
                "다채로운 과일이 가득 담긴 화려한 과일 케이크, 특별한 날에 특별함을 더해주는 신선하고 달콤한 디저트.",
                "부드럽고 풍부한 바나나 시트에 신선한 크림을 가득 채운 바나나 케이크, 달콤한 향과 맛이 일품입니다.",
                "겉은 바삭하고 속은 부드러운 시나몬 롤, 따뜻한 오븐에서 구워내어 향긋한 스파이스와 달콤함이 조화로운 최고의 간식.",
                "상큼한 오렌지와 부드러운 크림이 조화를 이루는 오렌지 케이크, 상큼함과 달콤함이 완벽하게 어우러진 디저트.",
                "부드러운 카스테라에 달콤한 생크림을 덮은 카스테라 케이크, 정성스럽게 구워낸 부드러운 풍미가 매력적인 디저트.",
                "신선한 과일과 향긋한 크림이 가득 담긴 상큼한 과일 파이, 매일 새롭게 구워내어 맛과 신선함이 가득합니다.",
                "촉촉한 커스터드와 신선한 사과가 어우러진 사과 파이, 부드러운 식감과 달콤한 맛이 조화로운 클래식한 디저트.",
                "부드러운 카스테라에 달콤한 생크림을 덮은 카스테라 케이크, 정성스럽게 구워낸 부드러운 풍미가 매력적인 디저트."
        };


        String customCakeImage = "https://kr.object.ncloudstorage.com/myseatdang-bucket/menu/motherfather.jpg";
        MenuType menuType;
        String menuDesc;
        String menuImage;
        CustomMenuOpt customMenuOpt = null;
        String menuName = "";

        Random random = new Random();
        int generatedNum = random.nextInt(cakeImages.length);

        if(store.getStoreType() == StoreType.CUSTOM){
            menuType = MenuType.ORDER_MADE;
            menuDesc = "특별한 날을 더 빛나게 해드릴 케이크를 만들어 드립니다";
            menuImage = customCakeImage;
            menuName = "주문제작 케이크";

            Menu menu = Menu.builder()
                    .storeId(store.getStoreId())
                    .menuName(menuName)
                    .image(menuImage)
                    .menuType(menuType)
                    .menuPrice(30000)
                    .menuStatus(MenuStatus.ORDERABLE)
                    .menuDesc(menuDesc)
                    .build();
            menuList.add(menu);

        }else{
            int menuIter=0;
            while(generatedNum < cakeImages.length && menuIter < 3) {
                menuType = MenuType.NORMAL;
                menuDesc = cakeDesc[generatedNum];
                menuImage = cakeImages[generatedNum];
                menuName = cakeNames[generatedNum];

                Menu menu = Menu.builder()
                        .storeId(store.getStoreId())
                        .menuName(menuName)
                        .image(menuImage)
                        .menuType(menuType)
                        .menuPrice(30000)
                        .menuStatus(MenuStatus.ORDERABLE)
                        .menuDesc(menuDesc)
                        .build();
                menuList.add(menu);
                generatedNum++;
                menuIter++;
            }
        }
        return menuList;
    }

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

    @DisplayName("상점 더미데이터 100개 빵집, 10호점씩 생성")
    @Test
    public void test2() {
        //given
        String[] desc ={"웨이팅을 받는 웨이팅 전용 매장입니다", "사전 주문 케이크만 판매 합니다. 예약 후 방문 해주세요", "특별한 날에 걸맞는 특별한 케이크를 준비 해드립니다"};
        String csvFile = "csv/store_sample_seoul.csv"; // CSV 파일 경로 설정
        String[] bakeryNames = {
                "마싯당", "빵굽는 마을", "행복한 빵집", "프랑스 마카롱", "따뜻한 식빵", "황금 크루아상",
                "구름빵집", "크림 가득한 빵집", "버터 향기", "마들렌의 집", "달콤한 케이크", "소보로의 미소",
                "바삭한 페스츄리", "파리의 아침", "슈크림 천국", "초코렛 브라우니", "베이커리 하우스", "오후의 도넛",
                "촉촉한 빵집", "에그타르트의 향기", "치즈의 유혹", "꿈꾸는 단팥빵", "바닐라 크림빵", "멜론빵 가게",
                "고소한 소라빵", "호두파이의 행복", "베이글 가든", "달콤한 초코파이", "단호박 머핀", "코코넛 러스크",
                "신선한 버터롤", "산딸기 파이", "치아바타의 꿈", "달콤한 카스테라", "카푸치노 베이커리", "오렌지 케이크",
                "블루베리 타르트", "레몬 머랭", "아몬드 브리오슈", "바닐라 치즈케이크", "촉촉한 미니롤", "쫀득한 팥도넛",
                "모카 번", "땅콩버터 빵집", "바나나 브레드", "꿀맛 밀크롤", "신선한 크림치즈빵", "바삭한 미니파이",
                "상큼한 과일파이", "촉촉한 초코케이크", "햇살 빵집", "푸른 마카롱", "별빛 케이크", "부드러운 브리오슈",
                "신선한 크로와상", "달콤한 꿀빵", "크림과 버터의 만남", "세련된 파이", "부드러운 롤케이크", "맛있는 앙버터",
                "아침의 빵집", "푸짐한 케이크", "크리스피 도넛", "진한 초콜릿빵", "애플 파이 하우스", "커피와 빵의 향기",
                "수제 초콜릿", "고급스러운 케이크", "사랑스러운 마들렌", "풍부한 타르트", "쫄깃한 젤리롤", "싱싱한 브레드",
                "맛있는 크로와상", "달콤한 시나몬롤", "고급 초콜릿", "향긋한 치즈케이크", "아름다운 파운드케이크",
                "부드러운 마카롱", "새콤한 레몬파이", "세련된 타르트", "부드러운 크림빵", "풍부한 앙트레", "모던한 빵집",
                "디저트의 천국", "햇살이 가득한 빵집", "쫄깃한 브라우니", "달콤한 프레즐", "예쁜 비스코티", "향기로운 소보로",
                "부드러운 단팥빵", "수제 타르트", "사랑의 롤케이크", "유럽풍 빵집", "매일매일 신선한 빵집", "달콤한 바게트"
        };
        Random random = new Random();
        List<String> addresses = new ArrayList<>();
        int csvCnt =0;
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(csvFile);
             CSVReader reader = new CSVReaderBuilder(new InputStreamReader(inputStream))
                     .withSkipLines(1)  // 첫 줄(헤더) 스킵
                     .build()) {

            String[] line;
            while ((line = reader.readNext()) != null && csvCnt < 5000) {
                if (line.length > 15) {
                    String 지번주소 = line[15];
                    // 지번주소가 비어있거나 null인 경우, 다음 루프로 넘어감
                    if (지번주소 != null && !지번주소.trim().isEmpty()) {
                        addresses.add(지번주소);
                    }
                }
                csvCnt++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (addresses.size() < bakeryNames.length * 10) {
            System.err.println("주소 데이터가 부족합니다.");
            return;
        }

        int addressIndex = 0;

        // 각 빵집 이름에 대해 10호점까지 생성
        for (String bakeryName : bakeryNames) {
            for (int i = 1; i <= 10; i++) {
                if (addressIndex >= addresses.size()) {
                    System.err.println("주소 데이터가 부족하여 생성을 종료합니다.");
                    return;
                }

                String name = String.format("%s %d호점", bakeryName, i);
                String addr = addresses.get(addressIndex++);  // 순차적으로 주소 선택

                // StoreType의 모든 값 중 하나를 무작위로 선택
                StoreType[] storeTypes = StoreType.values();
                StoreType randomStoreType = storeTypes[random.nextInt(storeTypes.length)];

                List<String> imageList = List.of(
                        "https://kr.object.ncloudstorage.com/myseatdang-bucket/menu/cake2.jpg");
                List<String> thumbList = List.of(
                        "https://kr.object.ncloudstorage.com/myseatdang-bucket/store-thumbnail/27cakerex-plzm-superJumbo.jpg",
                        "https://kr.object.ncloudstorage.com/myseatdang-bucket/store-thumbnail/300s.jpg",
                        "https://kr.object.ncloudstorage.com/myseatdang-bucket/store-thumbnail/360_F_144756103_CSn2v542YVI3tMrCqpMvb5l4axtQc2He.jpg",
                        "https://kr.object.ncloudstorage.com/myseatdang-bucket/store-thumbnail/58d22cbc33f099240a3ea7be6b969a55.jpg",
                        "https://kr.object.ncloudstorage.com/myseatdang-bucket/store-thumbnail/BW1A4089-2.jpg",
                        "https://kr.object.ncloudstorage.com/myseatdang-bucket/store-thumbnail/IMG_8420-2.webp",
                        "https://kr.object.ncloudstorage.com/myseatdang-bucket/store-thumbnail/Naked-Cake-Recipe-Card.jpg",
                        "https://kr.object.ncloudstorage.com/myseatdang-bucket/store-thumbnail/Real-Fruit-Cake-1.jpg",
                        "https://kr.object.ncloudstorage.com/myseatdang-bucket/store-thumbnail/Vanilal_fresh_fruit_cake-removebg-preview_9bcaa842-d16f-45f4-82e5-60d8b3580a86_600x.webp",
                        "https://kr.object.ncloudstorage.com/myseatdang-bucket/store-thumbnail/Vizcos-Store640-1.jpg",
                        "https://kr.object.ncloudstorage.com/myseatdang-bucket/store-thumbnail/default-cake-shops-8-250.jpg",
                        "https://kr.object.ncloudstorage.com/myseatdang-bucket/store-thumbnail/golden-bakery-bokaro-sector-4-bokaro-cake-shops-51d3wibygs-250.webp",
                        "https://kr.object.ncloudstorage.com/myseatdang-bucket/store-thumbnail/grgergerhdxaefawrs.jpg",
                        "https://kr.object.ncloudstorage.com/myseatdang-bucket/store-thumbnail/hq720.jpg",
                        "https://kr.object.ncloudstorage.com/myseatdang-bucket/store-thumbnail/images%20%281%29.jfif",
                        "https://kr.object.ncloudstorage.com/myseatdang-bucket/store-thumbnail/images%20%282%29.jfif",
                        "https://kr.object.ncloudstorage.com/myseatdang-bucket/store-thumbnail/images%20%283%29.jfif",
                        "https://kr.object.ncloudstorage.com/myseatdang-bucket/store-thumbnail/images%20%284%29.jfif",
                        "https://kr.object.ncloudstorage.com/myseatdang-bucket/store-thumbnail/images%20%285%29.jfif",
                        "https://kr.object.ncloudstorage.com/myseatdang-bucket/store-thumbnail/images%20%286%29.jfif",
                        "https://kr.object.ncloudstorage.com/myseatdang-bucket/store-thumbnail/images.jfif",
                        "https://kr.object.ncloudstorage.com/myseatdang-bucket/store-thumbnail/mr-bakers-azamgarh-cake-shops-iz0akkk773.webp",
                        "https://kr.object.ncloudstorage.com/myseatdang-bucket/store-thumbnail/patisserie-and-chocolatier-shop-carbillet-in-rue-des-forges-in-dijon-DRPA9F.jpg",
                        "https://kr.object.ncloudstorage.com/myseatdang-bucket/store-thumbnail/roll-cake-bakery-and.jpg",
                        "https://kr.object.ncloudstorage.com/myseatdang-bucket/store-thumbnail/start-a-bakery-bakery-display-case.jpg"
                );

                String thumb = thumbList.get(random.nextInt(thumbList.size()));

                Store store;
                if (i <= 2) {  // 초기 2개는 GENERAL_WAITING으로 설정
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
                            .thumbnail(thumb)
                            .description(desc[0])
                            .storeSetting(new StoreSetting())
                            .notice("Closed on public holidays.")
                            .phone(generateRandomKoreanPhoneNumber())
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
                                    .waitingPeopleCount(10)
                                    .maxReservationInDay(1000)
                                    .maxReservationInTime(5)
                                    .build()
                            )
                            .build();
                } else if (i <= 4) {  // 3호점과 4호점은 GENERAL_RESERVATION으로 설정
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
                            .thumbnail(thumb)
                            .description(desc[1])
                            .notice("Closed on public holidays.")
                            .phone(generateRandomKoreanPhoneNumber())
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
                } else if(i<=6) {  // 5~6은 CUSTOM으로 설정
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
                            .regularDayOff("일요일")
                            .thumbnail(thumb)
                            .description(desc[2])
                            .notice("공휴일에는 쉽니다")
                            .phone(generateRandomKoreanPhoneNumber())
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
                }else{
                    WaitingStatus waitingStatus = WaitingStatus.UNAVAILABLE;
                    Status revStatus = Status.OFF;
                    String description;
                    if(randomStoreType == StoreType.GENERAL_WAITING) {
                         waitingStatus = WaitingStatus.OPEN;
                         description = desc[0];
                    }else if(randomStoreType == StoreType.GENERAL_RESERVATION) {
                        revStatus = Status.ON;
                        description = desc[1];
                    }else{
                        revStatus = Status.ON;
                        description = desc[2];
                    }

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
                            .thumbnail(thumb)
                            .description("특별한 날에 걸맞는 특별한 케이크를 준비 해드립니다")
                            .notice("공휴일에는 쉽니다")
                            .phone(generateRandomKoreanPhoneNumber())
                            .starRating(4.5)
                            .storeSetting(StoreSetting.builder()
                                    .reservationOpenTime(LocalTime.of(10, 0))
                                    .reservationCloseTime(LocalTime.of(20, 0))
                                    .reservationStatus(revStatus)
                                    .waitingTime(new WaitingTime(LocalTime.of(10, 0), LocalTime.of(20, 0),
                                            LocalTime.of(0, 10)))
                                    .waitingStatus(waitingStatus)
                                    .maxReservationInDay(1000)
                                    .maxReservationInTime(5)
                                    .build()
                            )
                            .build();
                }
                store = storeRepository.save(store);
                List<Menu> menus = generateRandomMenuList(store);
                menuRepository.saveAll(menus);
            }
        }
    }

    //각각 마싯당1~6호점 점주
    //마싯당1~2호점 = 웨이팅
    //마싯당3~4호점 = 일반주문
    //마싯당5~6호점 = 주문제작
    static Stream<Arguments> storeOwnerProvider() {
        String[] names = {
                "김나경", "구민상", "박정은", "이용준", "변성일", "이주원", "박지호", "김서준", "정예린", "박하준",
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
        return IntStream.rangeClosed(1, 950).mapToObj(i -> {
            String email = "store" + i + "@naver.com";
            String name = names[(i - 1)%names.length];
            Long storeId = Integer.toUnsignedLong(i);
            return Arguments.of(email, storeId , name);
        });
    }
    @DisplayName("상점 회원과 상점 더미데이터 세팅")
    @MethodSource("storeOwnerProvider")
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
                .memberPhone(generateRandomKoreanPhoneNumber())
                .memberRole(MemberRole.ROLE_STORE_OWNER)
                .memberStatus(MemberStatus.APPROVED)
                .businessLicenseImage("business_license.jpg")
                .businessLicense("123-45-67890")
                .bankAccountCopy("bank_account.jpg")
                .bankAccount("123-456-789")
                .storeOwnerProfileImage("https://kr.object.ncloudstorage.com/myseatdang-bucket/profile-images/1/abstract-user-flat-4.png")
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
