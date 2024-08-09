package com.matdang.seatdang.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{


        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/**","/login","/signup").permitAll() // 누구나 허용 // 지금 다 허용함
//                        .requestMatchers("/admin/**").hasRole("ADMIN") // ROLE_ADMIN 권한이 있는 사용자만 허용
//                        .requestMatchers("/storeowner/**").hasRole("STOREOWNER") // ROLE_STOREOWNER 권한이 있는 사용자만 허용
                        .anyRequest().authenticated()
                );
        http
                .formLogin((auth) -> auth.loginPage("/login")
                        .loginProcessingUrl("/loginProc")
                        .permitAll() // 아무나 가능
                );
        http
                .csrf((auth) -> auth.disable());
        // csrf라는 사이트 위변조 방지 설정 자동설정임 그래서 post요청 보낼 때 csrf 토큰도 같이 보내주어야 로그인됨
        // 이거 켜두면 개발환경에서는 토큰 보내지 않으면 로그인 진행 안되서 지금 disable 해둔거임

        return http.build();
    }

    //정적파일 무시
    // 지금은 동ㄱ적 파일 경로 admin , customer 통과로 만듦 수정하기
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring()
//                .requestMatchers("/admin/**","/customer/**","/");
//    }


}
