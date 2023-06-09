package com.apiautomation.apitesting;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class APIRequestTestsUsingPojos {
    @Test
    public void  postRequestForRegistrationWithValidEmail() throws JsonProcessingException {
        UserDetails user=new UserDetails("USER","t@gmail.com","USER");
        ObjectMapper objectMapper=new ObjectMapper();
        String s=objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(user);
        System.out.println(s); //serialisation of java class object into json object
        UserDetails users= objectMapper.readValue(s, UserDetails.class);
        System.out.println(users); //deserialization of json object into java class object
        Response response=
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(s)
                .baseUri("http://localhost:8080/welcome/register").
                log().all().
                when()
                .post().
                then()
                .assertThat().statusCode(200)
                .extract().response();
        System.out.println(response);

        }
    }


