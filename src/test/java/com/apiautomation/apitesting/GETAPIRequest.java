package com.apiautomation.apitesting;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import netscape.javascript.JSObject;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GETAPIRequest {
    @Test
    public void GetRequestTestingForAllUserDetails()
    {
        Response response=
        RestAssured.
                given()
                .contentType(ContentType.JSON)
                .baseUri("http://localhost:8080/sampleAPIRequestAll").
                when()
                .get().
                then()
                .assertThat()
                .statusCode(200)
                .statusLine("HTTP/1.1 200 ").
                extract().response();
        assertTrue(response.getBody().asString().contains("newuser11@gmail.com"));
    }
    @Test
    public void GetRequestTestingForUserDetails() throws JSONException {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .queryParam("email","newuser11@gmail.com")
                .baseUri("http://localhost:8080/sampleAPIRequest").
                when().get().
                then()
                .assertThat().statusCode(200);

    }
    @Test
    public void GetRequestTestingForUserDetailsForInvalidUser() throws JSONException {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .queryParam("email","newusers@gmail.com")
                .baseUri("http://localhost:8080/sampleAPIRequest").
                when().get().
                then()
                .assertThat().statusCode(403);
    }
}
