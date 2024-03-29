package com.cstapin.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class AuthSteps {

    private static final String PATH_PREFIX = "/api/v1/auth";
    private static final String PATH_PREFIX_WEB = "/api/v1/web/auth";

    public static ExtractableResponse<Response> 관리자_회원가입_요청(String username, String password, String nickname, String secretKey) {
        return 관리자_회원가입_요청(RestAssured.given().log().all(), username, password, nickname, secretKey);
    }

    public static ExtractableResponse<Response> 관리자_회원가입_요청(RequestSpecification requestSpecification,
                                                            String username, String password, String nickname, String secretKey) {
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        params.put("nickname", nickname);
        params.put("secretKey", secretKey);

        return requestSpecification
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post(PATH_PREFIX + "/join/admin")
                .then().log().all().extract();
    }


    public static ExtractableResponse<Response> 로그인_요청(String username, String password) {
        return 로그인_요청(RestAssured.given().log().all(), username, password);
    }

    public static ExtractableResponse<Response> 로그인_요청(RequestSpecification requestSpecification,
                                                       String username, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);

        return requestSpecification
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post(PATH_PREFIX + "/login")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 로그인_요청_깃허브(RequestSpecification requestSpecification, String code) {
        Map<String, String> params = new HashMap<>();
        params.put("code", code);

        return requestSpecification
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post(PATH_PREFIX + "/login/github")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 일반_회원가입_요청(String username, String password, String nickname) {
        return 일반_회원가입_요청(RestAssured.given().log().all(), username, password, nickname);
    }

    public static ExtractableResponse<Response> 일반_회원가입_요청(RequestSpecification requestSpecification,
                                                            String username, String password, String nickname) {
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        params.put("nickname", nickname);

        return requestSpecification
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post(PATH_PREFIX + "/join/user")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 토큰_재발급_요청(RequestSpecification requestSpecification, String refreshToken) {
        return requestSpecification
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(Map.of("refreshToken", refreshToken))
                .when().post(PATH_PREFIX + "/reissue")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 웹토큰_발행() {
        return 웹토큰_발행(RestAssured.given().log().all());
    }

    public static ExtractableResponse<Response> 웹토큰_발행(RequestSpecification requestSpecification) {
        return requestSpecification
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(PATH_PREFIX_WEB + "/token")
                .then().log().all().extract();
    }

}
