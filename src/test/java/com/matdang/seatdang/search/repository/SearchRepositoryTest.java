package com.matdang.seatdang.search.repository;

import com.matdang.seatdang.menu.repository.MenuRepository;
import com.matdang.seatdang.store.entity.Store;
import com.matdang.seatdang.store.repository.StoreRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SearchRepositoryTest {
//    @Autowired
//    private SearchRepository searchRepository;
    @Autowired
    private StoreRepository storeRepository;

    @DisplayName("지역명으로 검색")
    @Test
    public void test2() {
        //given
        //when
        //then
    }

    @DisplayName("주소로 검색")
    @Test
    public void test3() {
        //given
        //when
        //then
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
                    .image("https://kr.object.ncloudstorage.com/myseatdang-bucket/sample-folder/0a2e250f-1a1a-4805-bf17-559d7c4105cf.png")
                    .build();
            //when
            store = storeRepository.save(store);
            System.out.println(store);
        }

        //then
//        assertThat(store.getStoreId()).isNotZero();
    }

    @Disabled
    @DisplayName("매장데이터 생성")
    @Test
    public void test5() {
        //given
        String csvFile = "C:\\workspace\\my-seat-dang\\src\\main\\resources\\csv\\store_seoul.csv"; // CSV 파일 경로 설정
        String[] suffixes = {"에그타르트", "브라우니", "케이크", "에클레어", "마카롱", "소라빵", "단팥빵", "식빵"};
        Random random = new Random();

        try (CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(csvFile), Charset.forName("EUC-KR")))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                // 해당 열의 인덱스에 따라 데이터 추출
                String 사업장명 = line[18];
                String 도로명주소 = line[16];
                String 지번주소 = line[15];
                String 전화번호 = line[12];
                String 좌표정보X = line[23];
                String 좌표정보Y = line[24];

                // 무작위로 접미사를 선택하여 사업장명 뒤에 추가
                String randomSuffix = suffixes[random.nextInt(suffixes.length)];
                String modified사업장명 = "The" + randomSuffix +" "+사업장명;

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
                Store store = Store.builder()
                        .storeName(name)
                        .storeAddress(addr)
                        .image("https://kr.object.ncloudstorage.com/myseatdang-bucket/sample-folder/0a2e250f-1a1a-4805-bf17-559d7c4105cf.png")
                        .build();
                //when
                store = storeRepository.save(store);
                System.out.println(store);
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        //when
        //then
    }
}