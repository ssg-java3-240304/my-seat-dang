package com.matdang.seatdang.member.service;

import com.matdang.seatdang.member.dto.CustomerDto;
import com.matdang.seatdang.member.entity.Customer;
import com.matdang.seatdang.member.entity.Gender;
import com.matdang.seatdang.member.repository.CustomerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;


@Service
public class CustomerService {

    private final PasswordEncoder passwordEncoder;
    private final CustomerRepository customerRepository;

    public CustomerService(PasswordEncoder passwordEncoder, CustomerRepository customerRepository) {
        this.passwordEncoder = passwordEncoder;
        this.customerRepository = customerRepository;
    }


    public CustomerDto convertToDto(Customer customer) {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setMemberName(customer.getMemberName());
        customerDto.setCustomerNickName(customer.getCustomerNickName());
        customerDto.setCustomerProfileImage(customer.getCustomerProfileImage());
        customerDto.setMemberEmail(customer.getMemberEmail());
        customerDto.setMemberPhone(customer.getMemberPhone());
        customerDto.setCustomerGender(customer.getCustomerGender() != null ? customer.getCustomerGender().name() : null);
        customerDto.setCustomerBirthday(customer.getCustomerBirthday() != null ? customer.getCustomerBirthday().toString() : null);
        customerDto.setImageGenLeft(customer.getImageGenLeft());

        return customerDto;
    }

    @Transactional
    public Customer findById(Long memberId) {
        // DB에서 최신 Customer 정보를 조회
        return customerRepository.findById(memberId).orElseThrow(() -> new RuntimeException("Customer not found"));
    }



    @Transactional
    public void updateCustomerDetails(Customer customer, String customerNickName, String customerGender, String customerBirthday, String customerProfileImageUrl) {
        // 기존 값이 null이 아니고 새로운 값이 null이 아닌 경우에만 업데이트
        String updatedNickName = (customerNickName != null && !customerNickName.isEmpty()) ? customerNickName : customer.getCustomerNickName();
        Gender updatedGender = (customerGender != null && !customerGender.isEmpty()) ? Gender.valueOf(customerGender) : customer.getCustomerGender();

        LocalDate updatedBirthday = customer.getCustomerBirthday();
        if (customerBirthday != null && !customerBirthday.isEmpty()) {
            try {
                updatedBirthday = LocalDate.parse(customerBirthday);
            } catch (DateTimeParseException e) {
                // 생일 값이 잘못된 경우 로그 처리 및 기존 생일 값 유지
                System.err.println("Invalid date format for birthday: " + customerBirthday);
            }
        }

        // 새로운 프로필 이미지가 없을 경우 기존 프로필 이미지 유지
        String updatedProfileImageUrl = (customerProfileImageUrl != null && !customerProfileImageUrl.isEmpty()) ? customerProfileImageUrl : customer.getCustomerProfileImage();



        customer = customer.toBuilder()
                .customerNickName(updatedNickName)
                .customerGender(updatedGender)
                .customerBirthday(updatedBirthday)
                .customerProfileImage(updatedProfileImageUrl)
                .build();

        customerRepository.save(customer);
    }


    @Transactional
    public void updateCustomerPassword(Customer customer, String newPassword) {
        // 빌더 패턴을 사용하여 새로운 인스턴스로 업데이트
        Customer updatedCustomer = customer.toBuilder()
                .memberPassword(passwordEncoder.encode(newPassword))
                .build();

        customerRepository.save(updatedCustomer);
    }
}
