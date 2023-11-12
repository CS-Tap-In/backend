package com.cstapin.member.service;


import com.cstapin.member.domain.Member;
import com.cstapin.member.service.dto.DailyGoalRequest;
import com.cstapin.member.service.dto.MembersRequest;
import com.cstapin.member.service.dto.MembersResponse;
import com.cstapin.member.service.dto.ProfileResponse;
import com.cstapin.member.service.query.MemberQueryService;
import com.cstapin.quiz.service.query.LearningRecordQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final LearningRecordQueryService learningRecordQueryService;
    private final MemberQueryService memberQueryService;

    public ProfileResponse findProfile(String username) {
        Member member = memberQueryService.findByUsername(username);

        int completeQuizCount = learningRecordQueryService.findCompleteQuizCount(member.getId(), LocalDate.now());
        boolean isDailyLearningComplete = learningRecordQueryService.isDailyLearningComplete(member.getId(), LocalDate.now());

        return ProfileResponse.of(memberQueryService.findByUsername(username), completeQuizCount, isDailyLearningComplete);
    }

    @Transactional
    public void changeDailyGoal(String username, DailyGoalRequest request) {
        memberQueryService.findByUsername(username).changeDailyGoal(request.getDailyGoal());
    }

    public Page<MembersResponse> findMembers(MembersRequest request) {
        return memberQueryService.findMembers(request);
    }

    @Transactional
    public void withdrawMember(String username) {
        memberQueryService.findByUsername(username).withdraw();
    }
}
