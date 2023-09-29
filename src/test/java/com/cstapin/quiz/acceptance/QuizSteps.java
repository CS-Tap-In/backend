package com.cstapin.quiz.acceptance;

import com.cstapin.quiz.domain.LearningStatus;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizSteps {

    private static final String PATH_PREFIX_ADMIN = "/api/v1/admin/quizzes";
    private static final String PATH_PREFIX_USER = "/api/v1/user/quizzes";

    public static ExtractableResponse<Response> 문제_생성(String accessToken, Map<String, Object> params) {
        return 문제_생성(RestAssured.given().log().all().auth().oauth2(accessToken), params);
    }

    public static ExtractableResponse<Response> 문제_생성(RequestSpecification requestSpecification, Map<String, Object> params) {
        return requestSpecification
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post(PATH_PREFIX_ADMIN)
                .then().log().all().extract();
    }

    public static Map<String, Object> 문제_생성_요청값(Long categoryId, String title, String problem, List<String> answers) {
        Map<String, Object> params = new HashMap<>();
        params.put("categoryId", categoryId + "");
        params.put("title", title);
        params.put("problem", problem);
        params.put("answer", answers);
        params.put("status", "PRIVATE");
        return params;
    }

    public static ExtractableResponse<Response> 문제_목록_조회(String accessToken, Map<String, String> params) {
        return 문제_목록_조회(RestAssured.given().log().all().auth().oauth2(accessToken), params);
    }

    public static ExtractableResponse<Response> 문제_목록_조회(RequestSpecification requestSpecification, Map<String, String> params) {
        return requestSpecification
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParams(params)
                .when().get(PATH_PREFIX_ADMIN)
                .then().log().all().extract();
    }

    public static Map<String, String> 문제_목록_조회_요청값(String searchType, String keyword, Long categoryId) {
        Map<String, String> params = new HashMap<>();
        params.put("st", searchType);
        params.put("keyword", keyword);
        params.put("category", categoryId + "");
        params.put("page", 1 + "");
        params.put("size", 10 + "");
        params.put("status", "PRIVATE");
        params.put("rejected", "N");
        return params;
    }

    public static ExtractableResponse<Response> 문제_상세_조회(String accessToken, Long id) {
        return 문제_상세_조회(RestAssured.given().log().all().auth().oauth2(accessToken), id);
    }

    public static ExtractableResponse<Response> 문제_상세_조회(RequestSpecification requestSpecification, Long id) {
        return requestSpecification
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", id)
                .when().get(PATH_PREFIX_ADMIN + "/{id}")
                .then().log().all().extract();
     }

     public static ExtractableResponse<Response> 문제_카테고리_생성(String accessToken, Map<String, String> params) {
         return 문제_카테고리_생성(RestAssured.given().log().all().auth().oauth2(accessToken), params);
     }

    public static ExtractableResponse<Response> 문제_카테고리_생성(RequestSpecification requestSpecification, Map<String, String> params) {
        return requestSpecification
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post(PATH_PREFIX_ADMIN + "/categories")
                .then().log().all().extract();
    }

    public static Map<String, String> 문제_카테고리_요청값(String title) {
        Map<String, String> params = new HashMap<>();
        params.put("title", title);
        return params;
    }

    public static ExtractableResponse<Response> 문제_카테고리_수정(String accessToken, Long id, Map<String, String> params) {
        return 문제_카테고리_수정(RestAssured.given().log().all().auth().oauth2(accessToken), id, params);
    }

    public static ExtractableResponse<Response> 문제_카테고리_수정(RequestSpecification requestSpecification, Long id, Map<String, String> params) {
        return requestSpecification
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", id)
                .body(params)
                .when().put(PATH_PREFIX_ADMIN + "/categories/{id}")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 문제_카테고리_삭제(String accessToken, Long id) {
        return 문제_카테고리_삭제(RestAssured.given().log().all().auth().oauth2(accessToken), id);
    }

    public static ExtractableResponse<Response> 문제_카테고리_삭제(RequestSpecification requestSpecification, Long id) {
        return requestSpecification
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", id)
                .when().delete(PATH_PREFIX_ADMIN + "/categories/{id}")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 문제_카테고리_목록_조회(String accessToken) {
        return 문제_카테고리_목록_조회(RestAssured.given().log().all().auth().oauth2(accessToken));
    }

    public static ExtractableResponse<Response> 문제_카테고리_목록_조회(RequestSpecification requestSpecification) {
        return requestSpecification
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(PATH_PREFIX_ADMIN + "/categories")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 문제_수정(String accessToken, Map<String, Object> params, Long quizId) {
        return 문제_수정(RestAssured.given().log().all().auth().oauth2(accessToken), params, quizId);
    }

    public static ExtractableResponse<Response> 문제_수정(RequestSpecification requestSpecification, Map<String, Object> params, Long quizId) {
        return requestSpecification
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .pathParam("quizId", quizId)
                .when().put(PATH_PREFIX_ADMIN + "/{quizId}")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 문제_삭제(String accessToken, Long quizId) {
        return 문제_삭제(RestAssured.given().log().all().auth().oauth2(accessToken), quizId);
    }

    public static ExtractableResponse<Response> 문제_삭제(RequestSpecification requestSpecification, Long quizId) {
        return requestSpecification
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("quizId", quizId)
                .when().delete(PATH_PREFIX_ADMIN + "/{quizId}")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 문제_상태_변경(String accessToken, Long quizId, String status) {
        return 문제_상태_변경(RestAssured.given().log().all().auth().oauth2(accessToken), quizId, status);
    }

    public static ExtractableResponse<Response> 문제_상태_변경(RequestSpecification requestSpecification, Long quizId, String status) {
        return requestSpecification
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("quizId", quizId)
                .body(Map.of("status", status))
                .when().patch(PATH_PREFIX_ADMIN + "/{quizId}/status")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 문제들_상태_변경(String accessToken, List<Long> quizIds, String status) {
        return 문제들_상태_변경(RestAssured.given().log().all().auth().oauth2(accessToken), quizIds, status);
    }

    public static ExtractableResponse<Response> 문제들_상태_변경(RequestSpecification requestSpecification, List<Long> quizIds, String status) {
        return requestSpecification
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(Map.of("status", status, "quizIds", quizIds))
                .when().patch(PATH_PREFIX_ADMIN + "/status")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 유저가_문제_생성(String accessToken, Map<String, Object> params) {
        return 유저가_문제_생성(RestAssured.given().log().all().auth().oauth2(accessToken), params);
    }

    public static ExtractableResponse<Response> 유저가_문제_생성(RequestSpecification requestSpecification, Map<String, Object> params) {
        return requestSpecification
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post(PATH_PREFIX_USER)
                .then().log().all().extract();
    }

    public static Map<String, Object> 유저가_문제_생성_요청값(Long categoryId, String title, String problem, List<String> answers) {
        Map<String, Object> params = new HashMap<>();
        params.put("categoryId", categoryId + "");
        params.put("title", title);
        params.put("problem", problem);
        params.put("answer", answers);
        return params;
    }

    public static ExtractableResponse<Response> 내가_만든_문제_목록_조회(String accessToken) {
        return 내가_만든_문제_목록_조회(RestAssured.given().log().all().auth().oauth2(accessToken));
    }

    public static ExtractableResponse<Response> 내가_만든_문제_목록_조회(RequestSpecification requestSpecification) {
        return requestSpecification
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(PATH_PREFIX_USER + "/my/making")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 오늘의_문제_선정(String accessToken) {
        return 오늘의_문제_선정(RestAssured.given().log().all().auth().oauth2(accessToken));
    }

    public static ExtractableResponse<Response> 오늘의_문제_선정(RequestSpecification requestSpecification) {
        return requestSpecification
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(PATH_PREFIX_USER + "/daily")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 오늘의_문제_목록_조회(String accessToken) {
        return 오늘의_문제_목록_조회(RestAssured.given().log().all().auth().oauth2(accessToken));
    }

    public static ExtractableResponse<Response> 오늘의_문제_목록_조회(RequestSpecification requestSpecification) {
        return requestSpecification
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(PATH_PREFIX_USER + "/daily")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 문제_풀이_기록_등록(String accessToken, Long learningRecordId, LearningStatus status) {
        return 문제_풀이_기록_등록(RestAssured.given().log().all().auth().oauth2(accessToken), learningRecordId, status);
    }

    public static ExtractableResponse<Response> 문제_풀이_기록_등록(RequestSpecification requestSpecification,
                                                            Long learningRecordId, LearningStatus status) {
        return requestSpecification
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("learningRecordId", learningRecordId)
                .body(Map.of("learningStatus", status.name()))
                .when().post(PATH_PREFIX_USER + "/daily/learningRecords/{learningRecordId}")
                .then().log().all().extract();
    }
}
