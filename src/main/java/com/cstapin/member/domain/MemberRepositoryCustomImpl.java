package com.cstapin.member.domain;

import com.cstapin.member.service.dto.MembersRequest;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.cstapin.member.domain.QMember.member;

@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Member> findMembers(MembersRequest request) {
        List<Member> members = queryFactory
                .selectFrom(member)
                .where(hasUsername(request.getUsername()))
                .offset(request.getPageable().getOffset())
                .limit(request.getPageable().getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(member.count())
                .from(member)
                .where(hasUsername(request.getUsername()));

        System.out.println();

        return PageableExecutionUtils.getPage(members, request.getPageable(), countQuery::fetchOne);
    }

    private BooleanExpression hasUsername(String username) {
        if (StringUtils.hasText(username)) {
            return member.username.contains(username);
        }
        return null;
    }
}
