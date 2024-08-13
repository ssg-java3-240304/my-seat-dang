package com.matdang.seatdang.member.repository;


import com.matdang.seatdang.member.entitiy.Customer;
import com.matdang.seatdang.member.entitiy.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByMemberEmail(String memberEmail);

    // OAuth 가입 유무 찾기
    @Query("SELECT c FROM Customer c JOIN c.oauthIdentifiers oi WHERE oi = :oauthIdentifier")
    Customer findByOauthIdentifier(@Param("oauthIdentifier") String oauthIdentifier);

}