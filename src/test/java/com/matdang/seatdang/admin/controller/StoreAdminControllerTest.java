package com.matdang.seatdang.admin.controller;

import com.matdang.seatdang.admin.dto.StoreDetailReponseDto;
import com.matdang.seatdang.admin.repository.StoreAdminRepository;
import com.matdang.seatdang.admin.service.StoreAdminService;
import com.matdang.seatdang.store.entity.Store;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StoreAdminControllerTest {
    @Autowired
    private StoreAdminRepository storeAdminRepository;

//    @Test
//    @DisplayName("storeList 조회")
//    void test1(){
//        Long storeId = 1000L;
//        storeAdminRepository.findByStoreId(storeId);
//        assertThat(store).allMatch

//    }




