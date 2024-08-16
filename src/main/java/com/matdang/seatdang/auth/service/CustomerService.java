package com.matdang.seatdang.auth.service;

import com.matdang.seatdang.auth.dto.CustomerDto;
import com.matdang.seatdang.member.entitiy.Customer;
import com.matdang.seatdang.member.entitiy.Member;
import org.springframework.stereotype.Service;



@Service
public class CustomerService {

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
}
