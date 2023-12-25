package com.cstapin.utils;

import com.cstapin.member.domain.Credentials;
import com.cstapin.member.domain.Member;
import com.cstapin.member.domain.MemberRole;
import com.cstapin.member.domain.Profiles;
import com.cstapin.member.service.MemberRepository;
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
        memberRepository.save(Member.builder().credentials(
                new Credentials("admin", passwordEncoder.encode("password123@"), MemberRole.ADMIN))
                .profiles(new Profiles("admin")).build());

        memberRepository.save(Member.builder().credentials(
                        new Credentials("user", passwordEncoder.encode("password123@"), MemberRole.USER))
                .profiles(new Profiles("user", "http://avatar.com/1")).build());
    }
}
