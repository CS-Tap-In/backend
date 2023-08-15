package com.cstapin.auth.acceptance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public static ExtractableResponse<Response> 문제_생성(String accessToken, Map<String, String> params) {
        return 문제_생성(RestAssured.given().log().all().auth().oauth2(accessToken), params);
    }

    public static ExtractableResponse<Response> 문제_생성(RequestSpecification requestSpecification, Map<String, String> params) {
        return requestSpecification
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post(PATH_PREFIX_ADMIN)
                .then().log().all().extract();
    }

    public static Map<String, String> 문제_생성_요청값(Long categoryId, String title, String problem, List<String> answers) {
        Map<String, String> params = new HashMap<>();
        params.put("categoryId", categoryId + "");
        params.put("title", title);
        params.put("problem", problem);
        params.put("answer", listToJsonString(answers));
        return params;
    }

    public static ExtractableResponse<Response> 문제_목록_조회(String accessToken) {
        return 문제_목록_조회(RestAssured.given().log().all().auth().oauth2(accessToken));
    }

    public static ExtractableResponse<Response> 문제_목록_조회(RequestSpecification requestSpecification) {
        return requestSpecification
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(PATH_PREFIX_ADMIN)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 문제_상세_조회(String accessToken, Long id) {
        return 문제_상세_조회(RestAssured.given().log().all().auth().oauth2(accessToken), id);
    }

    public static ExtractableResponse<Response> 문제_상세_조회(RequestSpecification requestSpecification, Long id) {
        return requestSpecification
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", id)
                .when().get(PATH_PREFIX_ADMIN)
                .then().log().all().extract();
    }

    private static String listToJsonString(List<String> answers) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(answers);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Json 문자열 파싱이 실패하였습니다.");
        }
    }
}
