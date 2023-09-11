package com.cstapin.member.service;


import com.cstapin.member.service.dto.ProfileResponse;
import com.cstapin.member.service.query.MemberQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final MemberQueryService memberQueryService;

    public ProfileResponse findProfile(String username) {
        // 학습 기록 기능 추가시키면서 completeQuizCount 값 추가할 예정
        return ProfileResponse.of(memberQueryService.findByUsername(username), 0);
    }
}
