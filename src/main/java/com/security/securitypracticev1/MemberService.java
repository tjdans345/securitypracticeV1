package com.security.securitypracticev1;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public MemberEntity save(MemberEntity memberEntity) {
        String encPassword = passwordEncoder.encode(memberEntity.getPassword());
        memberEntity.setPassword(encPassword);
        return memberRepository.save(memberEntity);
    }

}
