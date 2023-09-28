package com.cstapin.member.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;

import java.util.Map;

public class MemberSteps {

    private static final String PATH_PREFIX_ADMIN = "/api/v1/admin/members";
    private static final String PATH_PREFIX_USER = "/api/v1/user/members";

    public static ExtractableResponse<Response> 프로필_조회(String accessToken) {
        return 프로필_조회(RestAssured.given().log().all().auth().oauth2(accessToken));
    }

    public static ExtractableResponse<Response> 프로필_조회(RequestSpecification requestSpecification) {
        return requestSpecification
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(PATH_PREFIX_USER + "/profiles")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 하루_퀴즈_목표치_변경(String accessToken, int dailyGoal) {
        return 하루_퀴즈_목표치_변경(RestAssured.given().log().all().auth().oauth2(accessToken), dailyGoal);
    }

    public static ExtractableResponse<Response> 하루_퀴즈_목표치_변경(RequestSpecification requestSpecification, int dailyGoal) {
        return requestSpecification
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(Map.of("dailyGoal", dailyGoal))
                .when().patch(PATH_PREFIX_USER + "/daily-goal")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 회원_목록_조회(RequestSpecification requestSpecification, String username) {
        return requestSpecification
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .params(Map.of("username", username, "page", 1, "size", 10))
                .when().get(PATH_PREFIX_ADMIN)
                .then().log().all().extract();
    }

}
