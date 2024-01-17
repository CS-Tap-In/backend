package com.cstapin.member.unit;

import com.cstapin.member.persistence.MemberEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberEntityTest {

    @Test
    void 회원탈퇴하면_username_앞에_withdrawal_이_붙는다() {
        //given
        MemberEntity memberEntity = MemberEntity.builder().username("username").build();

        //when
        memberEntity.withdraw();

        //then
        assertThat(memberEntity.getUsername()).isEqualTo("withdrawal_username");
    }
}
