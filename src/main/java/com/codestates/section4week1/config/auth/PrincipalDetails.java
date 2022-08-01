package com.codestates.section4week1.config.auth;

import com.codestates.section4week1.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class PrincipalDetails implements UserDetails {

    private Member member;

    public PrincipalDetails(Member member) {
        this.member = member;
    }

    // Authentication 타입 객체이며 안에 Member 정보가 있어야

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { //getAuthorities : User의 권한을 리턴
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return member.getRole();
            }
        });
        return collection;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

/*
시큐리티는 /login 주소에 요청이 오면 대신 로그인을 진행.
Authentication 타입 객체이며 안에 Member 정보가 있어야 한다.
로그인 진행이 완료되면 security session을 만든다.(Security ContextHolder)
Security Session ⇒ Authentication ⇒ UserDetails

(현재 따로 규칙이 없기 때문에 isAccountNonExpired, isAccountNonLocked, isCredentialsNonExpired, isEnabled 메서드들을 모두 return true로 설정.

 */