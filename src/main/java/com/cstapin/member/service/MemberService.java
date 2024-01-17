package com.cstapin.member.service;


import com.cstapin.member.domain.Member;
import com.cstapin.member.persistence.MemberEntity;
import com.cstapin.member.service.dto.DailyGoalRequest;
import com.cstapin.member.service.dto.MembersRequest;
import com.cstapin.member.service.dto.MembersResponse;
import com.cstapin.member.service.dto.ProfileResponse;
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
    private final MemberRepository memberRepository;

    public ProfileResponse findProfile(String username) {
        Member member = memberRepository.getByUsername(username);

        int completeQuizCount = learningRecordQueryService.findCompleteQuizCount(member.getId(), LocalDate.now());
        boolean isDailyLearningComplete = learningRecordQueryService.isDailyLearningComplete(member.getId(), LocalDate.now());

        return ProfileResponse.of(member.getId(), member.getProfiles(), completeQuizCount, isDailyLearningComplete);
    }

    @Transactional
    public void changeDailyGoal(String username, DailyGoalRequest request) {
        memberRepository.save(memberRepository.getByUsername(username).changeDailyGoal(request.getDailyGoal()));
    }

    public Page<MembersResponse> findMembers(MembersRequest request) {
        return memberRepository.findMembers(request);
    }

    @Transactional
    public void withdrawMember(String username) {
        Member withdrawnMember = memberRepository.getByUsername(username).withdraw();
        memberRepository.save(withdrawnMember);
    }
}
