package com.cstapin.utils;

import com.cstapin.member.domain.Member;
import com.cstapin.member.domain.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Profile("test")
@Component
public class DataLoader {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void loadData() {
        memberRepository.save(new Member("admin", passwordEncoder.encode("password123@"), "admin", Member.MemberRole.ADMIN));
    }
}
