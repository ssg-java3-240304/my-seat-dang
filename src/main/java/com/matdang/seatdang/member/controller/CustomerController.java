package com.matdang.seatdang.member.controller;

import com.matdang.seatdang.member.dto.CustomerDto;
import com.matdang.seatdang.auth.service.AuthService;
import com.matdang.seatdang.member.dto.CustomerUpdateDto;
import com.matdang.seatdang.member.service.CustomerService;
import com.matdang.seatdang.member.entity.Customer;
import com.matdang.seatdang.member.entity.Member;
import com.matdang.seatdang.object_storage.service.FileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


/**
 *
 * 인증된 사용자의 데이터를 조회하고 수정관련 컨트롤러
 *
 */



@Controller
public class CustomerController {

    private final AuthService authService;
    private final CustomerService customerService;

    private final FileService fileService;

    public CustomerController(AuthService authService, CustomerService customerService ,FileService fileService) {
        this.authService = authService;
        this.customerService = customerService;
        this.fileService = fileService;
    }

    @GetMapping("/mainmypage")
    public String mainMyPage(Model model) {
        Member member = authService.getAuthenticatedMember();

        if (member != null) {
            // 최신 DB 데이터를 가져오기 위해 findById 사용
            Customer customer = customerService.findById(member.getMemberId());
            CustomerDto customerDto = customerService.convertToDto(customer);
            model.addAttribute("customer", customerDto);
        } else {
            return "redirect:/login";
        }
        return "customer/mypage/mainmypage";
    }

    @PostMapping("/mainmypage/update")
    public String updateCustomer(@ModelAttribute CustomerUpdateDto customerUpdateDto, Model model) {
        Member member = authService.getAuthenticatedMember();

        if (member != null) {
            Customer customer = (Customer) member;

            // 프로필 이미지 업로드 처리
            String uploadedFileUrl = null;
            if (customerUpdateDto.getCustomerProfileImage() != null && !customerUpdateDto.getCustomerProfileImage().isEmpty()) {
                uploadedFileUrl = fileService.uploadSingleFile(customerUpdateDto.getCustomerProfileImage(), "profile-images/" + customer.getMemberId());
            }

//            // 값 잘 왔는지 확인
//            System.out.println("값 왔어?");
//            System.out.println("customerNickName: " + customerUpdateDto.getCustomerNickName());
//            System.out.println("customerBirthday: " + customerUpdateDto.getCustomerBirthday());
//            System.out.println("customerProfileImage: " + customerUpdateDto.getCustomerProfileImage());


            // 서비스 계층으로 업데이트할 필드 전달
            customerService.updateCustomerDetails(
                    customer,
                    customerUpdateDto.getCustomerNickName(),
                    customerUpdateDto.getCustomerGender(),
                    customerUpdateDto.getCustomerBirthday(),
                    uploadedFileUrl // 업로드된 이미지 URL 전달
            );
//            System.out.println("필드는 무슨 필드 전달했어?");
//            System.out.println("Updating customer details: " + customerUpdateDto.getCustomerNickName() + ", " + customerUpdateDto.getCustomerBirthday() + ", " + uploadedFileUrl);



            return "redirect:/mainmypage"; // 업데이트 후 메인 마이페이지로 리다이렉트
        } else {
            return "redirect:/login";
        }
    }



}
