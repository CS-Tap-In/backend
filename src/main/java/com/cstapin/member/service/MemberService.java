package com.cstapin.member.service;


import com.cstapin.member.service.dto.DailyGoalRequest;
import com.cstapin.member.service.dto.MembersRequest;
import com.cstapin.member.service.dto.MembersResponse;
import com.cstapin.member.service.dto.ProfileResponse;
import com.cstapin.member.service.query.MemberQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberQueryService memberQueryService;

    public ProfileResponse findProfile(String username) {
        // 학습 기록 기능 추가시키면서 completeQuizCount 값 추가할 예정
        return ProfileResponse.of(memberQueryService.findByUsername(username), 0);
    }

    @Transactional
    public void changeDailyGoal(String username, DailyGoalRequest request) {
        memberQueryService.findByUsername(username).changeDailyGoal(request.getDailyGoal());
    }

    public Page<MembersResponse> findMembers(MembersRequest request) {
        return memberQueryService.findMembers(request);
    }
}
