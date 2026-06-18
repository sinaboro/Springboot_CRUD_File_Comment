package com.example.jpasecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.AbstractPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                // ★ PART 8에서 이 부분만 수정 (게시판 권한 추가)
                .authorizeHttpRequests(auth -> auth
                        // 회원가입·로그인·정적 리소스는 로그인 없이 허용
                        .requestMatchers("/auth/**", "/css/**", "/js/**", "/images/**")
                        .permitAll()
                        // 나머지 요청은 로그인 필요
                        .anyRequest().authenticated()
                )

                // 로그인 설정
                .formLogin(form -> form
                        .loginPage("/auth/login") // 커스텀 로그인 페이지
                        .loginProcessingUrl("/auth/login") // 로그인 폼이 제출되는 URL
                        .defaultSuccessUrl("/board/list", true) // 로그인 성공 후 이동
                        .permitAll()
                )

                // 로그아웃 설정
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/auth/login")
                        .permitAll()
                );

        return http.build();
    }

}
