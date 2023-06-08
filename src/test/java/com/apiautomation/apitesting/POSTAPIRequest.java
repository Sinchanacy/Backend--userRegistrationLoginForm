package com.apiautomation.apitesting;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

public class POSTAPIRequest  {
    @Test
    public void postRequestForRegistrationWithValidEmail() throws JSONException {
        JSONObject object=new JSONObject();
        object.put("firstname","testuser3");
        object.put("email","testuser5@gmail.com");
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

    @Test
    public void postRequestForUserAuthenticationForValidEmailPassword() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("email","testuser@gmail.com");
        object.put("password","testuser");
        Response responset=
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(object.toString())
                .baseUri("http://localhost:8080/welcome/authenticate")
                .when()
                .post()
                .then()
                .assertThat().statusCode(200)
                .extract().response();
        System.out.println("Response=>" + responset.getBody().asString());
    }
    @Test
    public void postRequestForUserAuthenticationForNotVerifiedEmailPassword() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("email","testuser3@gmail.com");
        object.put("password","testuser");
       RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(object.toString())
                        .baseUri("http://localhost:8080/welcome/authenticate")
                        .when()
                        .post()
                        .then()
                         .assertThat().statusCode(403);


    }
    @Test
    public void postRequestForUserAuthenticationForValidEmailInvalidPassword() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("email","testuser@gmail.com");
        object.put("password","testuser1");
                RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(object.toString())
                        .baseUri("http://localhost:8080/welcome/authenticate")
                        .when()
                        .post()
                        .then()
                        .assertThat().statusCode(403);
    }
    @Test
    public void postRequestForUserAuthenticationForInvalidEmailPassword() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("email","testuser8gmail.com");
        object.put("password","testuser");
        Response response=
                RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(object.toString())
                        .baseUri("http://localhost:8080/welcome/authenticate")
                        .when()
                        .post()
                        .then()
                        .assertThat().statusCode(200)
                        .extract().response();
        System.out.println("Response=>" + response.getBody().asString());
    }

    @Test
    public void postRequestForForgotPasswordForValidEmail() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("email","testuser@gmail.com");
        Response response=
                RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(object.toString())
                        .baseUri("http://localhost:8080/resetpassword")
                        .when()
                        .post()
                        .then()
                        .assertThat().statusCode(200)
                        .extract().response();
        System.out.println("Response=>" + response.getBody().asString());
    }
    @Test
    public void postRequestForForgotPasswordForInvalidEmail() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("email","testuser9@gmail.com");
        RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(object.toString())
                        .baseUri("http://localhost:8080/resetpassword")
                        .when()
                        .post()
                        .then()
                        .assertThat().statusCode(403);
    }
    @Test
    public void postRequestForSavePasswordWithValidPasswordResetToken() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("newPassword","testusers");
        Response response=
                RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(object.toString())
                        .queryParam("token","9eea027d-3a27-4686-97a1-8de414987a09")
                        .baseUri("http://localhost:8080/savePassword")
                        .when()
                        .post()
                        .then()
                        .assertThat().statusCode(200)
                        .extract().response();
        System.out.println("Response => " + response.getBody().asString());
    }
    @Test
    public void postRequestForSavePasswordWithInValidPasswordResetToken() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("newPassword","testusers");
        Response response=
                RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(object.toString())
                        .queryParam("token","9ea027d-3a27-4686-97a1-8de414987a09")
                        .baseUri("http://localhost:8080/savePassword")
                        .when()
                        .post()
                        .then()
                        .assertThat().statusCode(200)
                        .extract().response();
        System.out.println("Response => " + response.getBody().asString());
    }

}
 