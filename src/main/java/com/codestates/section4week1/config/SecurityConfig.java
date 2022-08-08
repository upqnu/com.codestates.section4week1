package com.codestates.section4week1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // (1)
public class SecurityConfig {

//    @Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable(); // csrf를 disable 하지 않으면 form 태그로만 요청이 가능해지고 postman등의 요청이 불가능
        http.headers().frameOptions().disable(); // disable 하지 않으면 h2 연결 불가

        http.authorizeRequests()
                .antMatchers("/user/**").authenticated()
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/login");
        return http.build();
    }

}

/*
(1) SpringSecurity는 Web기반의 Security 외에 Method Security 기능을 제공. 서비스 계층을 직접 호출할때 사용할 수 있는 보안 기능.
@EnableGlobalMethodSecurity는 MethodSecurity용 설정이 따로 할 때 선언하는 애너테이션.

속성에는 securedEnabled, prePostEnabled, jsr250Enabled 3개의 옵션이 존재.
1.securedEnabled : @Secured 애노테이션을 사용하여 인가 처리를 하고 싶을때 사용하는 옵션이다.
기본값은 false
2.prePostEnabled : @PreAuthorize, @PostAuthorize 애노테이션을 사용하여 인가 처리를 하고 싶을때 사용하는 옵션이다.
기본값은 false
3.jsr250Enabled : @RolesAllowed 애노테이션을 사용하여 인가 처리를 하고 싶을때 사용하는 옵션이다.
기본값은 false

@Secured, @RolesAllowed
특정 메서드 호출 이전에 권한을 확인한다.
SpEL 지원하지 않는다.
@Secured 는 스프링에서 지원하는 애노테이션이며, @RolesAllowed는 자바 표준

@PreAuthorize, @PostAuthorize
특정 메서드 호출 전, 후 이전에 권한을 확인한다.
SpEL을 지원한다.
스프링에서 지원하는 애노테이션이다.
PostAuthorize는 해당 메서드의 리턴값을 returnObject 로 참조하여 SpEL을 통해 인가 처리를 할 수 있다.

(1)번에서 securedEnabled = true, prePostEnabled = true로 속성을 설정했으므로
IndexController.java에 새로운 메서드와 @Secured와 @preAuthorize를 추가

 */