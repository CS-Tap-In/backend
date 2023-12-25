package com.cstapin.auth.unit;

import com.cstapin.auth.domain.JwtReissueValidator;
import com.cstapin.auth.domain.Token;
import com.cstapin.auth.jwt.JwtProvider;
import com.cstapin.auth.jwt.properties.JwtProperties;
import com.cstapin.member.domain.Credentials;
import com.cstapin.member.domain.Member;
import com.cstapin.member.domain.MemberRole;
import com.cstapin.member.domain.Profiles;
import com.cstapin.member.persistence.MemberEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TokenTest {

    private static final String SECRET_KEY = "secret_key";
    @Mock
    private JwtProperties jwtProperties;
    private JwtReissueValidator jwtReissueValidator;
    private JwtProvider jwtProvider;
    private Member member;

    @BeforeEach
    void setUpFixture() {
        jwtReissueValidator = new JwtReissueValidator(jwtProperties);
        jwtProvider = new JwtProvider(jwtProperties);
        member = Member.builder().credentials(new Credentials("username", "password", MemberRole.ADMIN))
                .profiles(new Profiles("nickname", "avatarurl")).build();
    }

    @Test
    @DisplayName("토큰 정보 업데이트")
    void update() {
        //given
        when(jwtProperties.getAccessTokenExpirationPeriod()).thenReturn(0L);
        when(jwtProperties.getAccessTokenSecretKey()).thenReturn(SECRET_KEY);
        String accessToken = jwtProvider.createAccessToken(member.getCredentials());
        Token token = new Token(accessToken, "", LocalDateTime.now().minusDays(30), LocalDateTime.now().minusDays(10));

        //when
        String newAccessToken = jwtProvider.createAccessToken(member.getCredentials());
        String newRefreshToken = jwtProvider.createRefreshToken();
        token.update(jwtReissueValidator, newAccessToken, newRefreshToken, LocalDateTime.now());

        //then
        assertThat(token.getAccessToken()).isEqualTo(newAccessToken);
        assertThat(token.getRefreshToken()).isEqualTo(newRefreshToken);
    }

    @Test
    @DisplayName("accessToken 이 만료된 토큰이 아닌 경우")
    void updateWithNotExpiredAccessToken() {
        //given
        when(jwtProperties.getAccessTokenExpirationPeriod()).thenReturn(10_000_000L);
        when(jwtProperties.getAccessTokenSecretKey()).thenReturn(SECRET_KEY);
        String accessToken = jwtProvider.createAccessToken(member.getCredentials());
        Token token = new Token(accessToken, "", LocalDateTime.now().minusDays(30), LocalDateTime.now().minusDays(10));

        //when
        String newAccessToken = jwtProvider.createAccessToken(member.getCredentials());
        String newRefreshToken = jwtProvider.createRefreshToken();

        //then
        assertThatThrownBy(() -> token.update(jwtReissueValidator, newAccessToken, newRefreshToken, LocalDateTime.now()))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("가장 최근 토큰 재발급 요청이 30일이 넘은 경우")
    void updateWithRequestAfterThirtyDays() {
        //given
        when(jwtProperties.getAccessTokenExpirationPeriod()).thenReturn(0L);
        when(jwtProperties.getAccessTokenSecretKey()).thenReturn(SECRET_KEY);
        String accessToken = jwtProvider.createAccessToken(member.getCredentials());
        Token token = new Token(accessToken, "", LocalDateTime.now().minusDays(100), LocalDateTime.now().minusDays(31));

        //when
        String newAccessToken = jwtProvider.createAccessToken(member.getCredentials());
        String newRefreshToken = jwtProvider.createRefreshToken();

        //then
        assertThatThrownBy(() -> token.update(jwtReissueValidator, newAccessToken, newRefreshToken, LocalDateTime.now()))
                .isInstanceOf(IllegalStateException.class);
    }
}
