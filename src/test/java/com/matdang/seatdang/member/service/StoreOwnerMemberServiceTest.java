package com.matdang.seatdang.member.service;

import com.matdang.seatdang.member.entity.StoreOwner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StoreOwnerMemberServiceTest {

    @Autowired
    private StoreOwnerMemberService storeOwnerMemberService;

    @DisplayName("상점ID로 점주ID 조회")
    @Test
    public void test1() {
        //given
        Long storeId = 6L;
        StoreOwner storeOwner = storeOwnerMemberService.findByStoreId(storeId);
        assertThat(storeOwner.getStore().getStoreId()).isEqualTo(storeId);
        //when
        //then
    }
}