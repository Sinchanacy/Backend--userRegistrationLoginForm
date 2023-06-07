package com.apiautomation.apitesting;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

public class POSTAPIRequest  {
    @Test
    public void postRequestForRegistrationWithValidEmail() throws JSONException {
        JSONObject object=new JSONObject();
        object.put("firstname","testuser3");
        object.put("email","testuser3@gmail.com");
        object.put("password","testuser");
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(object.toString())
                .baseUri("http://localhost:8080/welcome/register").
                log().all().
                when()
                .post().
                then()
                .assertThat().statusCode(200);
    }


    @Test
    public void postRequestForRegistrationWithExistingEmail() throws JSONException {
        JSONObject object=new JSONObject();
        object.put("firstname","testuser");
        object.put("email","testuser@gmail.com");
        object.put("password","testuser");
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(object.toString())
                .baseUri("http://localhost:8080/welcome/register").
                when()
                .post().
                then()
                .assertThat().statusCode(403);
    }
}
