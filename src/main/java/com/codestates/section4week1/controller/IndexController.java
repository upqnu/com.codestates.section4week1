package com.codestates.section4week1.controller;

import com.codestates.section4week1.model.Member;
import com.codestates.section4week1.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/")
    public @ResponseBody String index() {
        return "index";
    }

    @GetMapping("/user")
    public @ResponseBody String user() {
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager() {
        return "manager";
    }

    @GetMapping("/login")
    public String login() {
        return "loginForm";
    }

    @GetMapping("/join")
    public String joinForm() {
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(Member member) {
        member.setRole("ROLE_USER");
        String rawPassword = member.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        member.setPassword(encPassword);

        memberRepository.save(member);

        return "redirect:/login";
    }

    @Secured("ROLE_ADMIN") // 1개의 권한을 주고 싶을 때 사용
    @GetMapping("/info")
    public @ResponseBody String info() {
        return "info";
    } // (1)

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") // 1개 이상의 권한을 주고 싶을 때 사용. 메서드가 실행되고 응답하기 직전에 권한을 검사하는데 사용.
    @GetMapping("/data")
    public @ResponseBody String data() {
        return "data";
    } // (2)
}

/*
(1) 1개의 권한을 주고 싶을 때 사용
SecurityConfig에 .antMatchers("/info/**").access("hasRole('ROLE_ADMIN')") 코드를 추가하는 것과 같은 동작.

(2) 1개 이상의 권한을 주고 싶을 때 사용.
SecurityConfig에 .antMatchers("/data/**").access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") 코드를 추가하는 것과 같은 동작.
#을 사용하면 파라미터에 접근할 수 있다 : (ex) @PreAuthorize("isAuthenticated() and (( #user.name == principal.name ) or hasRole('ROLE_ADMIN'))")
클라이언트에 응답하기 전에 로그인 상태 또는 반환되는 사용자 이름과 현재 사용자 이름에 대한 검사, 현재 사용자가 관리자 권한을 가지고 있는지 등의 권한 후처리를 할 수 있다.
 */