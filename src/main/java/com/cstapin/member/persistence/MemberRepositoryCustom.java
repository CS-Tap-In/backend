package com.cstapin.member.persistence;

import com.cstapin.member.service.dto.MembersRequest;
import org.springframework.data.domain.Page;

public interface MemberRepositoryCustom {
    Page<MemberEntity> findMembers(MembersRequest request);
}
