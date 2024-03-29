package com.cstapin.auth.domain;

import com.cstapin.auth.service.dto.JoinRequest;
import com.cstapin.member.domain.MemberRole;
import com.cstapin.member.service.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JoinValidator {

    private static final String DUPLICATE_USERNAME = "다른 아이디를 사용해주세요.";
    private static final String NOT_EQUAL_JOIN_ADMIN_SECRET_KEY = "관리자 비밀 키를 정확히 입력해주세요.";
    private final MemberRepository memberRepository;

    @Value("${props.join.admin}")
    private String joinAdminSecretKey;

    public void validate(JoinRequest request, MemberRole memberRole) {
        if (memberRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException(DUPLICATE_USERNAME);
        }
        if (MemberRole.ADMIN.equals(memberRole) && !Objects.equals(joinAdminSecretKey, request.getSecretKey())) {
            throw new IllegalArgumentException(NOT_EQUAL_JOIN_ADMIN_SECRET_KEY);
        }
    }
}
