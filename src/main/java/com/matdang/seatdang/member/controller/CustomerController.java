package com.matdang.seatdang.member.controller;

import com.matdang.seatdang.member.dto.CustomerDto;
import com.matdang.seatdang.auth.service.AuthService;
import com.matdang.seatdang.member.dto.CustomerUpdateDto;
import com.matdang.seatdang.member.service.CustomerService;
import com.matdang.seatdang.member.entity.Customer;
import com.matdang.seatdang.member.entity.Member;
import com.matdang.seatdang.object_storage.service.FileService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


/**
 *
 * 인증된 사용자의 데이터를 조회하고 수정관련 컨트롤러
 *
 */



@Controller
@RequestMapping("my-seat-dang")
public class CustomerController {

    private final AuthService authService;
    private final CustomerService customerService;

    private final FileService fileService;

    public CustomerController(AuthService authService, CustomerService customerService ,FileService fileService) {
        this.authService = authService;
        this.customerService = customerService;
        this.fileService = fileService;
    }

    // 회원수정 페이지 Edit Profile로 변경
    @GetMapping("/mypage/edit-profile")
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
        return "customer/mypage/edit-profile";
    }

    @PostMapping("/mypage/update")
    public String updateCustomer(@ModelAttribute CustomerUpdateDto customerUpdateDto) {
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



            return "redirect:/my-seat-dang/mypage/edit-profile"; // 업데이트 후 메인 마이페이지로 리다이렉트 // @이름변경 edit-profile로 이름
        } else {
            return "redirect:/login";
        }
    }


    @PostMapping("/mypage/edit-profile/change-password")
    public String changePassword(@RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 Model model, HttpServletRequest request) {
        Member member = authService.getAuthenticatedMember();

        if (member != null) {
            // 새 비밀번호와 확인 비밀번호가 일치하는지 확인
            if (!newPassword.equals(confirmPassword)) {
                model.addAttribute("error", "새 비밀번호와 확인 비밀번호가 일치하지 않습니다.");
                return "customer/mypage/edit-profile"; // 비밀번호가 일치하지 않을 경우 오류 메시지 표시
            }

            // 비밀번호 변경
            customerService.updateCustomerPassword((Customer) member, newPassword);

            // 세션 무효화 및 로그아웃 처리
            request.getSession().invalidate(); // 세션 무효화
            SecurityContextHolder.clearContext(); // Spring Security 컨텍스트 초기화

            model.addAttribute("success", "비밀번호가 성공적으로 변경되었습니다.");
            return "redirect:/login?passwordChanged=true"; // 비밀번호 변경 후 성공모달창으로 리다이렉트
        } else {
            return "redirect:/my-seat-dang/mypage/edit-profile";
        }
    }




}
