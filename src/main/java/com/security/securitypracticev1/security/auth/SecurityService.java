package com.security.securitypracticev1.security.auth;

import com.security.securitypracticev1.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SecurityService implements UserDetailsService {

    private MemberRepository memberRepository;

    @Autowired
    public SecurityService (MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 스프링이 로그인 요청을 가로챌 때, username, password 변수 2개를 가로채는데
    // password 부분 처리는 알아서 함.
    // username이 DB에 있는지만 확인해주면 됨. loadUserByUsername() 이 함수에서 확인을 해줌
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
