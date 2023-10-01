package com.cstapin.member.unit;

import com.cstapin.member.domain.Member;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberTest {

    @Test
    void 회원탈퇴하면_username_앞에_withdrawal_이_붙는다() {
        //given
        Member member = Member.builder().username("username").build();

        //when
        member.withdraw();

        //then
        assertThat(member.getUsername()).isEqualTo("withdrawal_username");
    }
}
