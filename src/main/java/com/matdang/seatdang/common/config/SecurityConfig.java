package com.matdang.seatdang.common.config;

import com.matdang.seatdang.auth.dto.CustomOAuth2User;
import com.matdang.seatdang.auth.service.CustomOAuth2UserService;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

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


    @Value("${openai.api.key}")
    private String apiKey;


    @Bean
    public OpenAiService getOpenAiService() {
        return new OpenAiService(apiKey, Duration.ofSeconds(30));
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{





        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/","/login","/signup", "/customer","/customer/search","/customer/store/detail/*","/signupProc","/check-nickname","/check-email", "/storeRegist", "/storeUpdate").permitAll() // 누구나 허용
//                        .requestMatchers("/","/login","/signup","/signupProc","/check-nickname","/check-email").permitAll() // 누구나 허용
                        .requestMatchers("/payment/**").permitAll() // /payment 하위 경로는 인증 없이 허용
                        .requestMatchers("/admin/**").hasRole("ADMIN") // ROLE_ADMIN 권한이 있는 사용자만 허용
                        .requestMatchers("/customer/mypage/**").hasRole("CUSTOMER") // ROLE_Customer
                        .requestMatchers("/store-owner/**").hasRole("STORE_OWNER") // ROLE_STORE_OWNER 권한이 있는 사용자만 허용
                        .anyRequest().authenticated() // 나머지 로그인 사용자만 이용가능
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login") // OAuth2 로그인 페이지 설정
                        .defaultSuccessUrl("/customer", true) // 로그인 성공 후 리다이렉트될 URL
                        .failureUrl("/login?error=true") // 로그인 실패 시 리다이렉트될 URL
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService) // 커스텀 OAuth2UserService 설정 가능
                ));

        http
                .formLogin((auth) -> auth.loginPage("/login") // 폼 로그인이라
                        .loginProcessingUrl("/loginProc")
                        .usernameParameter("memberEmail") // 현재 name = "memberEmail" // 기본인 username이 아니라서 써줌
                        .passwordParameter("memberPassword") // 현재 name = "memberPassword" // 기본인 password가 아니라서 써줌
//                        .successHandler((request, response, authentication) -> {
//                            // 사용자가 로그인하기 전에 가려고 했던 URL을 가져옴
//                            SavedRequest savedRequest = (SavedRequest) request.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST");
//
//                            // 만약 savedRequest가 있으면 그 URL로 리다이렉트, 없으면 기본 URL로 리다이렉트
//                            if (savedRequest != null) {
//                                String targetUrl = savedRequest.getRedirectUrl();
//                                response.sendRedirect(targetUrl);
//                            } else {
//                                response.sendRedirect("/customer");
//                            }
//                        })
                        .successHandler(customAuthenticationSuccessHandler())
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

//                        System.out.println("Logout Redirect URL: " + logoutRedirectUrl);

//                        response.sendRedirect(logoutUrl);

                        // 네이버 로그아웃 URL 설정
                        String redirectUri = URLEncoder.encode("http://localhost:8080/login", StandardCharsets.UTF_8);
                        String logoutUrl = "https://nid.naver.com/nidlogin.logout?returl=" + redirectUri;

                        // 네이버 로그아웃 요청을 백엔드에서 직접 호출
                        RestTemplate restTemplate = new RestTemplate();
                        ResponseEntity<String> logoutResponse = restTemplate.getForEntity(logoutUrl, String.class);

                        // 로그아웃 요청이 성공적으로 처리되었는지 확인
                        if (logoutResponse.getStatusCode() == HttpStatus.OK) {
                            // 로그아웃 성공 - 로그인 페이지로 리다이렉트
                            response.sendRedirect("/login");
                        } else {
                            // 로그아웃 실패 처리
                            // 예: 로그아웃 실패 메시지 전달 또는 다른 조치
                            System.out.println("네이버 로그아웃 실패: 상태 코드 - " + logoutResponse.getStatusCode());
                            response.sendRedirect("/login?logoutError=true");
                        }


//

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

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            String targetUrl;

            // 사용자의 역할에 따라 리다이렉트할 URL 결정
            if (authentication.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
                targetUrl = "/admin/main";
            } else if (authentication.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_STORE_OWNER"))) {
                targetUrl = "/store-owner/main";
            } else if (authentication.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_CUSTOMER"))) {
                targetUrl = "/customer";
            } else {
                targetUrl = "/customer"; // 기본 URL
            }

            // 사용자가 로그인하기 전에 가려고 했던 URL이 있는지 확인
            SavedRequest savedRequest = (SavedRequest) request.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST");
            if (savedRequest != null) {
                targetUrl = savedRequest.getRedirectUrl();
            }

            response.sendRedirect(targetUrl);
        };
    }


}
