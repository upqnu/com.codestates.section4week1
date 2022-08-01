package com.codestates.section4week1.config.auth;

import com.codestates.section4week1.model.Member;
import com.codestates.section4week1.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member memberEntity = memberRepository.findByUsername(username);
        System.out.println("username : " + username);
        if(memberEntity != null) {
            return new PrincipalDetails(memberEntity);
        }
        return null;
    }
}

/*
Security 설정에서 loginProcessingUrl(”/login”);으로 요청이 오면 --> 자동으로 UserDetailsService 타입으로 IoC되어 있는 loadUserByUsername 함수가 실행.
메서드 파라미터에 username이라고 되어있으면 form을 통해 username을 가져올 때 name이 반드시 매치되어야 한다.
이름을 똑같이 변경해주거나 SecurityConfig에 .loginPage() 아래에 .usernameParameter(”다른 이름")으로 추가해줘야 한다.
loadUserByUsername 함수가 Authentication으로 값이 return 된다.

MemberRepository에 findByUsername 메서드를 추가
 */