package com.easypay;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class EasyPayTest {

    String BaseURI="https://code-api-staging.easypayfinance.com";
    public static String token;
    String contentType="application/json" ;

    @Before
    public void authentication() {

        RequestSpecification tokenRequest=given().header("Content-type","application/json")
                .body("{\n" +
                        "  \"username\": \"user\",\n" +
                        "  \"password\": \"pass\"\n" +
                        "}");

        Response generateTokenResponse = tokenRequest.when().post(BaseURI+"/api/Authentication/login");
        generateTokenResponse.prettyPrint();

        token="Bearer "+generateTokenResponse.jsonPath().getString("token");
        System.out.println(token);

        generateTokenResponse.then().assertThat().statusCode(200);
        System.out.println(generateTokenResponse.getStatusCode());
    }

    @Test
    public void getAllApps() {

        Response response= RestAssured.given().contentType(contentType)
                .header("Authorization",token)
                .when().get(BaseURI+"/api/Application/all");

        response.prettyPrint();
        int actualResponseCode=response.statusCode();
        System.out.println(actualResponseCode);

        response.then().assertThat().statusCode(200);

    }

    @Test
    public void postAnApp(){

        RequestSpecification postApp=given()
                .header("Authorization",token)
                .header("Content-Type","application/json")
                .body("{\n" +
                        "  \"applicationId\": 56,\n" +
                        "  \"name\": \"Emine\",\n" +
                        "  \"age\": \"25\",\n" +
                        "  \"amount\": 4\n" +
                        "}");
        Response response =postApp.when().post(BaseURI+"/api/Application");
        response.prettyPrint();
        int actualResponseCode=response.statusCode();
        System.out.println(actualResponseCode);
        response.then().assertThat().statusCode(200);

    }
}
