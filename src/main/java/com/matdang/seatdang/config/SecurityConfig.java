package com.matdang.seatdang.config;

import com.matdang.seatdang.auth.dto.CustomOAuth2User;
import com.matdang.seatdang.auth.service.CustomOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.net.URLEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    // 순환 참조 때문에 Lazy 걸음 (CustomOAuth2UserService)
    @Lazy
    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{





        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/**","/login","/signup","/signupProc").permitAll() // 누구나 허용
                        .requestMatchers("/admin/**").hasRole("ADMIN") // ROLE_ADMIN 권한이 있는 사용자만 허용
                        .requestMatchers("/storeowner/**").hasRole("STORE_OWNER") // ROLE_STORE_OWNER 권한이 있는 사용자만 허용
                        .anyRequest().authenticated() // 나머지 로그인 사용자만 이용가능
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login") // OAuth2 로그인 페이지 설정
                        .defaultSuccessUrl("/", true) // 로그인 성공 후 리다이렉트될 URL
                        .failureUrl("/login?error=true") // 로그인 실패 시 리다이렉트될 URL
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService) // 커스텀 OAuth2UserService 설정 가능
                ));

        http
                .formLogin((auth) -> auth.loginPage("/login") // 폼 로그인이라
                        .loginProcessingUrl("/loginProc")
                        .usernameParameter("memberEmail") // 현재 name = "memberEmail" // 기본인 username이 아니라서 써줌
                        .passwordParameter("memberPassword") // 현재 name = "memberPassword" // 기본인 password가 아니라서 써줌
                        .permitAll() // 아무나 가능
                );
        http
                .csrf((auth) -> auth.disable());
        // csrf라는 사이트 위변조 방지 설정 자동설정임 그래서 post요청 보낼 때 csrf 토큰도 같이 보내주어야 로그인됨
        // 이거 켜두면 개발환경에서는 토큰 보내지 않으면 로그인 진행 안되서 지금 disable 해둔거임

        // 로그아웃
//        http.logout(auth -> {auth.logoutUrl("/logout")
//                .logoutSuccessHandler((request, response, authentication) -> {
//                    if (authentication != null && authentication.getPrincipal() instanceof CustomOAuth2User) {
//                        // 세션 무효화
//                        request.getSession().invalidate();
//                        // 로그아웃 URL이 제공되는 경우 사용
//                        String logoutUrl = "https://nid.naver.com/nidlogin.logout"; // 네이버의 로그아웃 URL
//                        response.sendRedirect(logoutUrl);
//                    } else {
//                        // 기본 로그아웃 성공 처리
//                        request.getSession().invalidate();
//                        response.sendRedirect("/login");
//                    }
//                });
//        });
        http.logout(auth -> auth
                .logoutUrl("/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                    // 사용자가 인증된 상태인지 사용자가 OAuth 인증을 통해 로그인 했는지?
                    if (authentication != null && authentication.getPrincipal() instanceof CustomOAuth2User) {
                        // 애플리케이션 세션 무효화 (인증되지 않은 상태)
                        request.getSession().invalidate();
                        // 네이버 로그아웃은 되는데 naver로 가짐..
                        // 네이버 로그아웃 URL에 리다이렉트할 URI 포함
                        String logoutUrl = "https://nid.naver.com/nidlogin.logout?returl=" + URLEncoder.encode("http://223.130.155.102:8080/login", "UTF-8");
                        System.out.println("Encoded URL: " + logoutUrl);
//                        System.out.println("Logout Redirect URL: " + logoutRedirectUrl);


                        // 네이버 로그아웃으로 리다이렉트
                        //현재 지금 네이버 Oauth 로그아웃하면 네이버창으로 감.......ㅠㅠㅠ
                        response.sendRedirect(logoutUrl);
//                        response.sendRedirect("/login");


                    } else {
                        // 기본 세션 로그아웃 처리
                        request.getSession().invalidate(); // 세션 무효화
                        response.sendRedirect("/login");
                    } //원래는 authentication != null && authentication.getPrincipal() 이건데
                    //authentication.getPrincipal()가 CustomOAuth2User 이면 UserDetails 구현체라서 구별 가능
                })
        );



        // frameOption : deny | sameOrigin | allow-from
        http.headers(httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
        return http.build();
    }

    //정적파일 무시
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers("/assets/**","/css/**","/img/**","/js/**","/sass/**","/scss/**","/vendor/**","/video/**");
    }

//    // OAuth2UserService 빈 설정 (필요에 따라 커스텀 서비스 추가 가능)
//    @Bean
//    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
//        return new DefaultOAuth2UserService(); // 기본 서비스 사용
//    }






}
