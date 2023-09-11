package com.cstapin.member.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;

public class MemberSteps {

    private static final String PATH_PREFIX = "/api/v1/member";

    public static ExtractableResponse<Response> 프로필_조회(String accessToken) {
        return 프로필_조회(RestAssured.given().log().all(), accessToken);
    }

    public static ExtractableResponse<Response> 프로필_조회(RequestSpecification requestSpecification, String accessToken) {
        return requestSpecification
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(PATH_PREFIX + "/profile")
                .then().log().all().extract();
    }

}
