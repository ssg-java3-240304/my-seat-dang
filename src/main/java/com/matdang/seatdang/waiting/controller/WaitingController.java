package com.matdang.seatdang.waiting.controller;

import com.matdang.seatdang.auth.service.AuthService;
import com.matdang.seatdang.common.storeEnum.StoreType;
import com.matdang.seatdang.member.entity.*;
import com.matdang.seatdang.member.repository.MemberRepository;
import com.matdang.seatdang.member.vo.StoreVo;
import com.matdang.seatdang.store.entity.Store;
import com.matdang.seatdang.store.repository.StoreRepository;
import com.matdang.seatdang.store.vo.StoreSetting;
import com.matdang.seatdang.store.vo.WaitingTime;
import com.matdang.seatdang.waiting.controller.dto.WaitingPeople;
import com.matdang.seatdang.waiting.dto.UpdateRequest;
import com.matdang.seatdang.waiting.entity.WaitingStorage;
import com.matdang.seatdang.waiting.repository.WaitingStorageRepository;
import com.matdang.seatdang.waiting.repository.query.dto.WaitingDto;
import com.matdang.seatdang.waiting.entity.CustomerInfo;
import com.matdang.seatdang.waiting.entity.Waiting;
import com.matdang.seatdang.waiting.entity.WaitingStatus;
import com.matdang.seatdang.waiting.repository.WaitingRepository;
import com.matdang.seatdang.waiting.service.WaitingService;
import com.matdang.seatdang.waiting.service.WaitingSettingService;
import com.matdang.seatdang.waiting.service.facade.RedissonLockWaitingFacade;
import jakarta.annotation.PostConstruct;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/store-owner/waiting")
public class WaitingController {
    private final WaitingRepository waitingRepository;
    private final WaitingService waitingService;
    private final WaitingSettingService waitingSettingService;
    private final AuthService authService;
    // test 코드
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final StoreRepository storeRepository;
    private final WaitingStorageRepository waitingStorageRepository;
    private final RedissonLockWaitingFacade redissonLockWaitingFacade;

    @GetMapping
    public String showWaiting(@RequestParam(defaultValue = "0") int status,
                              @RequestParam(defaultValue = "0") int page,
                              Model model) {
        Long storeId = authService.getAuthenticatedStoreId();
        log.debug("storeId = {}", storeId);
        log.info("===  showWaiting  ===");

        Page<WaitingDto> waitings = waitingService.showWaiting(storeId, status, page);
        createEstimatedWaitingTime(status, model, waitings.getContent(), storeId);

        model.addAttribute("waitingStatus", waitingSettingService.findWaitingStatus(storeId));
        model.addAttribute("waitingPeople", WaitingPeople.create(waitings.getContent()));
        model.addAttribute("waitings", waitings);
        model.addAttribute("storeId", storeId);
        model.addAttribute("status", status);
        model.addAttribute("currentPage", waitings.getNumber());
        model.addAttribute("totalPages", waitings.getTotalPages());
        return "storeowner/waiting/main";
    }

    @PostMapping
    public String updateStatus(@ModelAttribute UpdateRequest updateRequest, Model model) {
        int result = redissonLockWaitingFacade.updateStatus(updateRequest);

        if (result == 0) {
            log.error("=== not update ===");
        } else {
            log.info("===  update Waiting Status  ===");
            log.info("storeId = {},  total update = {}", updateRequest.getStoreId(), result);
        }

        return "redirect:/store-owner/waiting";
    }

    private void createEstimatedWaitingTime(int status, Model model, List<WaitingDto> waitings, Long storeId) {
        if (status == 0 && !waitings.isEmpty()) {
            int diff = 0;
            long elapsedTime = Duration.between(waitings.get(0).getCreatedDate(), LocalDateTime.now()).toMinutes();
            int estimatedTime = waitingSettingService.findEstimatedWaitingTime(storeId).getMinute();

            if (estimatedTime > elapsedTime) {
                diff = estimatedTime - (int) elapsedTime;
            }

            int totalMinutes = (waitings.size() - 1) * estimatedTime + diff;
            int hours = totalMinutes / 60;
            int minutes = totalMinutes % 60;

            model.addAttribute("estimatedTime", LocalTime.of(hours, minutes));
        }
    }

    /**
     * test 실행시 주석 필요
     */
    @PostConstruct
    public void initData() throws InterruptedException {
        StoreVo storeVo = new StoreVo(1L, "달콤커피", StoreType.CUSTOM, "서울시강남구");
        storeRepository.save(Store.builder()
                .storeName("마싯당")
                .storeSetting(StoreSetting.builder()
                        .waitingPeopleCount(10)
                        .waitingStatus(com.matdang.seatdang.store.vo.WaitingStatus.CLOSE)
                        .waitingTime(WaitingTime.builder()
                                .waitingOpenTime(LocalTime.of(9, 0))
                                .waitingCloseTime(LocalTime.of(22, 0))
                                .build())
                        .build())
                .build());

        storeRepository.save(Store.builder()
                .storeName("마싯당-스토리지")
                .storeSetting(StoreSetting.builder()
                        .waitingPeopleCount(10)
                        .waitingStatus(com.matdang.seatdang.store.vo.WaitingStatus.OPEN)
                        .waitingTime(WaitingTime.builder()
                                .waitingOpenTime(LocalTime.of(9, 0))
                                .waitingCloseTime(LocalTime.of(22, 0))
                                .build())
                        .build())
                .build());

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
        memberRepository.save(storeOwner);

        Customer customer = Customer.builder()
                .memberEmail("customer@naver.com")
                .joinDate(LocalDate.now())
                .memberName("customer")
                .memberPassword(bCryptPasswordEncoder.encode("1234"))
                .memberPhone("010-1234-5678")
                .memberRole(MemberRole.ROLE_CUSTOMER)
                .memberStatus(MemberStatus.APPROVED)
                .imageGenLeft(5)
                .customerGender(Gender.MALE)
                .customerBirthday(LocalDate.of(1990, 1, 1))
                .customerNickName("미식가")
                .customerProfileImage("profile.jpg")
                .build();
        //when
        memberRepository.save(customer);

//        {
//            long i = 1;
//            for (WaitingStatus value : WaitingStatus.values()) {
//                if (value != WaitingStatus.VISITED) {
//                    for (int j = 0; j < 10; j++, i++) {
//                        waitingRepository.save(Waiting.builder()
//                                .waitingNumber(i)
//                                .waitingOrder(i)
//                                .storeId(1L)
//                                .customerInfo(new CustomerInfo(2L, "010-1111-1111", ((int) (Math.random() * 3 + 1))))
//                                .waitingStatus(value)
//                                .visitedTime(null)
//                                .build());
//                    }
//                }
//            }
//        }
//
//        for (long i = 1; i <= 10; i++) {
//            waitingRepository.save(Waiting.builder()
//                    .waitingNumber(i)
//                    .waitingOrder(i)
//                    .storeId(2L)
//                    .customerInfo(new CustomerInfo(i, "010-1111-1111", ((int) (Math.random() * 3 + 1))))
//                    .waitingStatus(WaitingStatus.WAITING)
//                    .visitedTime(null)
//                    .build());
//        }
//        {
//            long i = 51L;
//            for (WaitingStatus value : WaitingStatus.values()) {
//                if (value != WaitingStatus.WAITING) {
//
//                    for (int j = 0; j < 10; j++, i++) {
//                        waitingStorageRepository.save(WaitingStorage.builder()
//                                .id(i)
//                                .waitingNumber(i)
//                                .waitingOrder(i)
//                                .storeId(2L)
//                                .customerInfo(new CustomerInfo(2L, "010-1111-1111", ((int) (Math.random() * 3 + 1))))
//                                .createdDate(LocalDateTime.now())
//                                .waitingStatus(value)
//                                .visitedTime(null)
//                                .build());
//                    }
//                }
//            }
//        }
    }
}
