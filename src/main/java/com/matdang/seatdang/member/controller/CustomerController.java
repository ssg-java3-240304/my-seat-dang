package com.matdang.seatdang.member.controller;

import com.matdang.seatdang.member.dto.CustomerDto;
import com.matdang.seatdang.auth.service.AuthService;
import com.matdang.seatdang.member.service.CustomerService;
import com.matdang.seatdang.member.entity.Customer;
import com.matdang.seatdang.member.entity.Member;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


/**
 *
 * 인증된 사용자의 데이터를 조회하고 수정관련 컨트롤러
 *
 */



@Controller
public class CustomerController {

    private final AuthService authService;
    private final CustomerService customerService;

    public CustomerController(AuthService authService, CustomerService customerService) {
        this.authService = authService;
        this.customerService = customerService;
    }

    @GetMapping("/mainmypage")
    public String mainMyPage(Model model) {
        Member member = authService.getAuthenticatedMember();

        if (member != null) {
            Customer customer = (Customer) member;
            CustomerDto customerDto = customerService.convertToDto(customer);
            model.addAttribute("customer", customerDto);
        } else {
            return "redirect:/login";
        }
        return "customer/mypage/mainmypage";
    }
}
