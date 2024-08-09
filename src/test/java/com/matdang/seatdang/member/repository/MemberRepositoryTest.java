package com.matdang.seatdang.member.repository;

import com.matdang.seatdang.member.entitiy.*;
import com.matdang.seatdang.member.vo.StoreVo;
import common.storeEnum.StoreType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    private  MemberRepository memberRepository;

    @Autowired
    private  BCryptPasswordEncoder bCryptPasswordEncoder;





    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @DisplayName("Test")
    @Test
    void test() {

    }

    @Test
    public void testCreateCustomer() {
        Customer customer = Customer.builder()
                .memberEmail("customer@test.com")
                .joinDate(LocalDate.now())
                .memberName("Customer Name")
                .memberPassword(bCryptPasswordEncoder.encode("1234"))
                .memberPhone("010-1234-5678")
                .memberRole(MemberRole.ROLE_CUSTOMER)
                .memberStatus(MemberStatus.APPROVED)
                .imageGenLeft(5)
                .customerGender(Gender.MALE)
                .customerBirthday(LocalDate.of(1990, 1, 1))
                .customerNickName("nickname")
                .customerProfileImage("profile.jpg")
                .build();

        Customer savedCustomer = (Customer) memberRepository.save(customer);
        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getMemberId()).isNotNull();
    }

    @Test
    public void testCreateAdmin() {
        Admin admin = Admin.builder()
                .memberEmail("1234@naver.com")
                .joinDate(LocalDate.now())
                .memberName("Admin Name")
                .memberPassword(bCryptPasswordEncoder.encode("1234"))
                .memberPhone("010-1234-5678")
                .memberRole(MemberRole.ROLE_ADMIN)
                .memberStatus(MemberStatus.APPROVED)
                .build();

        Admin savedAdmin = (Admin) memberRepository.save(admin);
        assertThat(savedAdmin).isNotNull();
        assertThat(savedAdmin.getMemberId()).isNotNull();
    }

    @Test
    public void testCreateStoreOwner() {
        StoreVo storeVo = new StoreVo(1L,"달콤커피", StoreType.CUSTOM, "서울시강남구");
        StoreOwner storeOwner = StoreOwner.builder()
                .memberEmail("storeowner@naver.com")
                .joinDate(LocalDate.now())
                .memberName("Store Owner Name")
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

        StoreOwner savedStoreOwner = (StoreOwner) memberRepository.save(storeOwner);
        assertThat(savedStoreOwner).isNotNull();
        assertThat(savedStoreOwner.getMemberId()).isNotNull();
    }


    public void setMemberRepository(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
}