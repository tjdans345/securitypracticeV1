package com.security.securitypracticev1.security.auth;

import com.security.securitypracticev1.MemberEntity;
import com.security.securitypracticev1.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SecurityService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Autowired
    public SecurityService (MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 스프링이 로그인 요청을 가로챌 때, username, password 변수 2개를 가로채는데
    // password 부분 처리는 알아서 함.
    // username이 DB에 있는지만 확인해주면 됨. loadUserByUsername() 이 함수에서 확인을 해줌

    /**
     * 입력한 account를 통해 회원을 조회 -> 회원 정보와 권한 정보가 담긴 User Class 반환
     * @param account
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        Optional<MemberEntity> memberOptional = memberRepository.findAllByAccount(account);
        MemberEntity memberEntity = memberOptional.orElse(null);

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_MEMBER"));

        if(memberEntity.getAccount() == null) {
            memberEntity.setAccount("guest");
        }

        // 세션에 유저 정보 저장
        return new User(memberEntity.getAccount(), memberEntity.getPassword(), authorities);
    }


}
