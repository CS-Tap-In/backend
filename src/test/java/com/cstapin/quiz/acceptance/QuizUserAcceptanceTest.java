package com.cstapin.quiz.acceptance;

import com.cstapin.auth.acceptance.AuthSteps;
import com.cstapin.member.acceptance.MemberSteps;
import com.cstapin.quiz.domain.LearningStatus;
import com.cstapin.quiz.domain.QuizStatus;
import com.cstapin.utils.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import static com.cstapin.auth.acceptance.AuthSteps.웹토큰_발행;
import static com.cstapin.quiz.acceptance.QuizSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

public class QuizUserAcceptanceTest extends AcceptanceTest {


    @Value("${props.web-token.alg}")
    private String webTokenAlgorithm;

    @Value("${props.web-token.public-key}")
    private String webTokenPublicKey;

    private String accessToken;
    private String adminAccessToken;

    @BeforeEach
    void setUpFixture() {
        //로그인
        ExtractableResponse<Response> 로그인 = AuthSteps.로그인_요청("user", "password123@");
        accessToken = 로그인.jsonPath().getString("accessToken");

        //관리자 로그인
        ExtractableResponse<Response> 관리자_로그인 = AuthSteps.로그인_요청("admin", "password123@");
        adminAccessToken = 관리자_로그인.jsonPath().getString("accessToken");

        //카테고리
        문제_카테고리_생성(adminAccessToken, 문제_카테고리_요청값("데이터베이스"));

        //문제 생성
        Map<String, Object> 문제_생성_요청값 = 문제_생성_요청값(1L, "트랜잭션", "***연산은 한개의 논리적 단위(트랜잭션)에 대한 작업이 성공적으로 끝났고...",
                List.of("Commit", "커밋"));
        long 문제_id = 문제_생성(adminAccessToken, 문제_생성_요청값).jsonPath().getLong("id");
        문제_상태_변경(adminAccessToken, 문제_id, QuizStatus.PUBLIC.name());
    }

    /**
     * Given: 문제를 등록한다.
     * When: 내가 만든 문제들을 조회한다.
     * Then: 문제가 조회된다.
     * Then: 문제는 미승인(UNAPPROVED) 상태 이다.
     */
    @Test
    void createQuiz() {
        //given
        Map<String, Object> 문제_생성_요청값 = 유저가_문제_생성_요청값(1L, "인덱스", "+++은 기본 인덱스이다.", List.of("pk", "기본키", "기본 키"));
        유저가_문제_생성(accessToken, 문제_생성_요청값);

        //when
        ExtractableResponse<Response> 내가_만든_문제_목록_조회 = 내가_만든_문제_목록_조회(accessToken);

        //then
        assertThat(내가_만든_문제_목록_조회.jsonPath().getList("content.title")).containsExactly("인덱스");
        assertThat(내가_만든_문제_목록_조회.jsonPath().getList("content.status")).containsExactly("UNAPPROVED");
    }

    /**
     * Given: 문제를 등록한다.
     * Given: 로그인한다.
     * When: 하루 목표치를 1개로 변경한다.
     * When: 오늘의 문제를 선정한다.
     * Then: 새로운 문제 개수가 목표치 개수와 일치한다.
     */
    @Test
    void selectDailyQuizzes() {
        //when
        MemberSteps.하루_퀴즈_목표치_변경(accessToken, 1);
        ExtractableResponse<Response> 오늘의_문제_선정 = 오늘의_문제_선정(accessToken);

        //then
        assertThat(오늘의_문제_선정.jsonPath().getInt("newQuizCount")).isEqualTo(1);
    }

    /**
     * Given: 문제를 등록한다.
     * Given: 로그인한다.
     * When: 오늘의 문제를 선정한다.
     * Then: 오늘의 문제 목록의 사이즈와 문제 선정 시 반환 된 문제 개수의 합이 같다.
     * When: 문제 풀이 기록을 FAILURE로 등록한다.
     * Then: 오늘의 문제 목록을 조회하면 FAILURE 상태이다.
     * When: 문제 풀이 기록을 RECOVERY로 등록한다.
     * Then: 오늘의 문제 목록을 조회하면 조회되지 않는다.
     */
    // TODO 오늘의 문제 선정이 23:59:59에 되고 오늘의 문제 목록 조회가 00:00:01이 되면 에러가 발생한다.
    @Test
    void findDailyQuizzes() {
        //when
        ExtractableResponse<Response> 오늘의_문제_선정 = 오늘의_문제_선정(accessToken);
        int newQuizCount = 오늘의_문제_선정.jsonPath().getInt("newQuizCount");
        int reviewQuizCount = 오늘의_문제_선정.jsonPath().getInt("reviewQuizCount");

        //then
        ExtractableResponse<Response> 오늘의_문제_목록_조회 = 오늘의_문제_목록_조회(accessToken);
        assertThat(오늘의_문제_목록_조회.jsonPath().getList(".").size())
                .isEqualTo(newQuizCount + reviewQuizCount);

        //when
        long 학습_기록_id = 오늘의_문제_목록_조회.jsonPath().getLong("[0].learningRecordId");
        문제_풀이_기록_등록(accessToken, 학습_기록_id, LearningStatus.FAILURE);

        //then
        ExtractableResponse<Response> 오늘의_문제_목록_다시_조회 = 오늘의_문제_목록_조회(accessToken);
        assertThat(오늘의_문제_목록_다시_조회.jsonPath().getString("[0].learningStatus"))
                .isEqualTo(LearningStatus.FAILURE.name());

        //when
        문제_풀이_기록_등록(accessToken, 학습_기록_id, LearningStatus.RECOVERY);

        //then
        ExtractableResponse<Response> 오늘의_문제_목록_또_다시_조회 = 오늘의_문제_목록_조회(accessToken);
        assertThat(오늘의_문제_목록_또_다시_조회.jsonPath().getList(".").size())
                .isEqualTo(newQuizCount + reviewQuizCount - 1);

    }

    /**
     * GIVEN: 문제를 등록한다.
     * WHEN: 카테고리를 3~5개 사이로 정하고 문제를 선정한다.
     * THEN: 선택한 카테고리에 속하는 문제가 출제된다.
     */
    @Test
    void selectRandomQuizzesByCategory() {
        //given
        문제_카테고리_생성(adminAccessToken, 문제_카테고리_요청값("네트워크"));
        문제_카테고리_생성(adminAccessToken, 문제_카테고리_요청값("운영체제"));

        //when
        ExtractableResponse<Response> 랜덤_문제_선정 = 랜덤_문제_선정(List.of(1L, 2L, 3L));

        //then
        assertThat(랜덤_문제_선정.jsonPath().getString("[0].quizCategoryTitle"))
                .containsAnyOf("데이터베이스", "네트워크", "운영체제");
    }

    /**
     * Given: 문제를 풀고 맞춘 개수를 받는다.
     * When: 자신의 이름, 핸드폰 번호와 함께 맞춘 문제의 개수를 등록한다.
     * Then: 유저 순위 목록을 조회하면 조회된다.
     */
    @Test
    void submitRandomQuizResult() {
        //given
        int correctCount = 50;

        //when
        String webToken = 웹토큰_발행().jsonPath().getString("webToken");

        랜덤_문제_결과_등록(웹토큰_암호화(webToken), correctCount, "01012341234", "유기훈");

        //then
        assertThat(랜덤_문제_유저_순위_목록_조회(YearMonth.now().format(DateTimeFormatter.ofPattern("yyyy-MM")))
                .jsonPath().getString("content[0].phoneNumber"))
                .isEqualTo("010-****-1234");
        assertThat(랜덤_문제_유저_순위_목록_조회(YearMonth.now().format(DateTimeFormatter.ofPattern("yyyy-MM")))
                .jsonPath().getString("content[0].correctCount"))
                .isEqualTo("50");
    }

    /**
     * When: 핸드폰 번호가 순수한 숫자가 아니다.
     * Then: 400 에러가 발생한다.
     * When: 이름이 한글이 아니다.
     * Then: 400 에러가 발생한다.
     */
    @Test
    void validateInputSubmitRandomQuizResult() {
        //when
        String 웹토큰1 = 웹토큰_발행().jsonPath().getString("webToken");

        ExtractableResponse<Response> 순수숫자가아닌핸드폰번호 =
                랜덤_문제_결과_등록(웹토큰_암호화(웹토큰1), 50, "010-1234-1234", "유기훈");

        //then
        assertThat(순수숫자가아닌핸드폰번호.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        //when
        String 웹토큰2 = 웹토큰_발행().jsonPath().getString("webToken");

        ExtractableResponse<Response> 한글이아닌이름 =
                랜덤_문제_결과_등록(웹토큰_암호화(웹토큰2), 50, "01012341234", "youkihoon");

        //then
        assertThat(한글이아닌이름.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * When: 50문제를 푼 유저가 이름, 핸드폰 번호와 함께 맞춘 문제의 개수를 등록한다.
     * Then: 반환된 순위 값은 1이다.
     * When: 49문제를 푼 유저가 이름, 핸드폰 번호와 함께 맞춘 문제의 개수를 등록한다.
     * Then: 반환된 순위 값은 2이다.
     * When: 50문제를 푼 유저가 이름, 핸드폰 번호와 함께 맞춘 문제의 개수를 등록한다.
     * Then: 반환된 순위 값은 2이다.
     * Then: 순위를 조회하면 유저의 등수에 맞게 순서대로 반환된다.
     */
    @Test
    void submitRandomQuizResults() {
        //when
        String 웹토큰1 = 웹토큰_발행().jsonPath().getString("webToken");

        ExtractableResponse<Response> 첫번째_유저 = 랜덤_문제_결과_등록(웹토큰_암호화(웹토큰1), 50, "01012341234", "첫사람");

        //then
        assertThat(첫번째_유저.jsonPath().getLong("rank")).isEqualTo(1L);

        //when
        String 웹토큰2 = 웹토큰_발행().jsonPath().getString("webToken");

        ExtractableResponse<Response> 두번째_유저 = 랜덤_문제_결과_등록(웹토큰_암호화(웹토큰2), 49, "01012344321", "두사람");

        //then
        assertThat(두번째_유저.jsonPath().getLong("rank")).isEqualTo(2L);

        //when
        String 웹토큰3 = 웹토큰_발행().jsonPath().getString("webToken");

        ExtractableResponse<Response> 세번째_유저 = 랜덤_문제_결과_등록(웹토큰_암호화(웹토큰3), 50, "01012345678", "세사람");

        //then
        assertThat(세번째_유저.jsonPath().getLong("rank")).isEqualTo(2L);

        //then
        ExtractableResponse<Response> 순위_목록 = 랜덤_문제_유저_순위_목록_조회(YearMonth.now().format(DateTimeFormatter.ofPattern("yyyy-MM")));

        assertThat(순위_목록.jsonPath().getString("content[0].username")).isEqualTo("첫*람");
        assertThat(순위_목록.jsonPath().getString("content[1].username")).isEqualTo("세*람");
        assertThat(순위_목록.jsonPath().getString("content[2].username")).isEqualTo("두*람");
    }

    /**
     * Given: 웹 토큰을 발급한다.
     * When: 동일한 웹 웹토큰으로 결과 등록을 두 번 하면
     * Then: 두번째 결과는 등록되지 않는다.
     */
    @Test
    void 웹토큰_재사용() {
        //given
        String 웹토큰 = 웹토큰_발행().jsonPath().getString("webToken");

        //when
        랜덤_문제_결과_등록(웹토큰_암호화(웹토큰), 25, "01012341234", "유기훈");
        랜덤_문제_결과_등록(웹토큰_암호화(웹토큰), 30, "01012341234", "유기훈");

        //then
        ExtractableResponse<Response> 순위_목록 = 랜덤_문제_유저_순위_목록_조회(YearMonth.now().format(DateTimeFormatter.ofPattern("yyyy-MM")));

        assertThat(순위_목록.jsonPath().getInt("content[0].correctCount")).isEqualTo(25);
    }


    public String 웹토큰_암호화(String webToken) {
        try {
            Cipher cipher = Cipher.getInstance(webTokenAlgorithm);

            KeyFactory keyFactory = KeyFactory.getInstance(webTokenAlgorithm);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(webTokenPublicKey));
            PublicKey publicKey = keyFactory.generatePublic(keySpec);

            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            // 평문 암호화
            byte[] encryptedBytes = cipher.doFinal(webToken.getBytes());

            // Base64로 인코딩하여 암호화된 데이터 출력
            return Base64.getEncoder().encodeToString(encryptedBytes);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            throw new AccessDeniedException("잘못된 접근입니다.");
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }

    }

}
