package com.cstapin.member.domain;

import com.cstapin.member.service.dto.MembersRequest;
import org.springframework.data.domain.Page;

public interface MemberRepositoryCustom {
    Page<Member> findMembers(MembersRequest request);
}
