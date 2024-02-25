package com.cstapin.auth.service;

import com.cstapin.auth.domain.*;
import com.cstapin.auth.jwt.JwtProvider;
import com.cstapin.auth.oauth2.github.GithubClient;
import com.cstapin.auth.oauth2.github.GithubCodeRequest;
import com.cstapin.auth.oauth2.github.GithubProfileResponse;
import com.cstapin.auth.service.dto.*;
import com.cstapin.auth.service.query.TokenQueryService;
import com.cstapin.exception.notfound.MemberNotFoundException;
import com.cstapin.member.domain.Member;
import com.cstapin.member.domain.MemberRole;
import com.cstapin.member.service.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    @Value("${props.github.username.prefix}")
    private String githubUsernamePrefix;

    @Value("${props.web-token.alg}")
    private String webTokenAlgorithm;

    @Value("${props.web-token.private-key}")
    private String webTokenPrivateKey;

    private final GithubClient githubClient;
    private final JoinValidator joinValidator;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final TokenQueryService tokenQueryService;
    private final TokenRepository tokenRepository;
    private final JwtReissueValidator jwtReissueValidator;
    private final MemberRepository memberRepository;
    private final AuthenticationRepository authenticationRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.getByUsername(username);
        return new PrincipalDetails(member.getCredentials());
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        try {
            Member member = memberRepository.getByUsername(request.getUsername());
            member.validatePassword(passwordEncoder, request.getPassword());
            Token token = tokenRepository.save(
                    new Token(jwtProvider.createAccessToken(member.getCredentials()), jwtProvider.createRefreshToken())
            );
            Member savedMember = memberRepository.save(member.updateToken(token.getId()));

            return new LoginResponse(token, savedMember.getRole());
        } catch (MemberNotFoundException e) {
            throw new IllegalArgumentException("잘못된 아이디 입니다.");
        }
    }

    @Transactional
    public LoginResponse loginFromGithub(GithubCodeRequest request) {

        String accessTokenFromGithub = githubClient.getAccessTokenFromGithub(request.getCode());
        GithubProfileResponse githubProfile = githubClient.getGithubProfileFromGithub(accessTokenFromGithub);

        try {
            Member member = memberRepository.getByUsername(githubUsernamePrefix + githubProfile.getId());

            Token token = tokenRepository.save(
                    new Token(jwtProvider.createAccessToken(member.getCredentials()), jwtProvider.createRefreshToken())
            );
            Member savedMember = memberRepository.save(member.updateToken(token.getId()));

            return new LoginResponse(token, savedMember.getRole());
        } catch (MemberNotFoundException e) {
            Member member = Member.githubMember(githubUsernamePrefix, githubProfile);

            Token token = tokenRepository.save(
                    new Token(jwtProvider.createAccessToken(member.getCredentials()), jwtProvider.createRefreshToken())
            );

            Member savedMember = memberRepository.save(member.updateToken(token.getId()));

            return new LoginResponse(token, savedMember.getRole());
        }
    }

    @Transactional
    public void join(JoinRequest request, MemberRole memberRole) {
        joinValidator.validate(request, memberRole);
        memberRepository.save(request.toMember(passwordEncoder, memberRole));
    }

    @Transactional
    public TokenResponse reissueToken(ReissueTokenRequest request) {

        Token token = tokenQueryService.findByRefreshToken(request.getRefreshToken());
        Member member = memberRepository.getByTokenId(token.getId());
        token.update(
                jwtReissueValidator,
                jwtProvider.createAccessToken(member.getCredentials()),
                jwtProvider.createRefreshToken(),
                LocalDateTime.now()
        );

        return new TokenResponse(token);
    }

    @Transactional
    public WebTokenResponse issueWebToken() {
        Authentication authentication = authenticationRepository.save(new Authentication(UUID.randomUUID().toString()));

        return new WebTokenResponse(authentication.getWebToken());
    }

    @Transactional
    public void validateWebTokenAuthentication(String encryptedWebToken) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(webTokenAlgorithm);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(webTokenPrivateKey));
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

            Cipher cipher = Cipher.getInstance(webTokenAlgorithm);

            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            // 암호문을 복호화
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedWebToken));

            // 평문으로 변환하여 출력
            String webToken = new String(decryptedBytes);

            Authentication authentication = authenticationRepository.findByWebToken(webToken)
                    .orElseThrow(() -> new AccessDeniedException("유효하지 않은 토큰입니다."));

            authenticationRepository.delete(authentication);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            throw new AccessDeniedException("잘못된 접근입니다.");
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }
}
