package com.matdang.seatdang.admin.controller;

import com.matdang.seatdang.store.repository.StoreOwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StoreAdminControllerTest {
    @Autowired
    private StoreOwnerRepository storeOwnerRepository;

//    @Test
//    @DisplayName("storeList 조회")
//    void test1(){
//        Long storeId = 1000L;
//        storeAdminRepository.findByStoreId(storeId);
//        assertThat(store).allMatch

    }
