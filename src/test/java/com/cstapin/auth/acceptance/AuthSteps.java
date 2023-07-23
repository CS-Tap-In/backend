package com.cstapin.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class AuthSteps {

    private static final String PATH_PREFIX = "/api/v1/auth";

    public static ExtractableResponse<Response> 관리자_회원가입_요청(String username, String password, String nickname, String secretKey) {
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        params.put("nickname", nickname);
        params.put("secretKey", secretKey);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post(PATH_PREFIX + "/join/admin")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 로그인_요청(String username, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post(PATH_PREFIX + "/login")
                .then().log().all().extract();
    }

}
