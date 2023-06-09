package com.apiautomation.apitesting;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

public class APIRequestTestsUsingPojos {
    @Test
    public void  postRequestForRegistrationWithValidEmail() throws JsonProcessingException {
        UserDetails user=new UserDetails("USER","USER@gmail.com","USER");
        ObjectMapper objectMapper=new ObjectMapper();
        String s=objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(user);
        System.out.println(s); //serialisation of java class object into json object
        UserDetails users= objectMapper.readValue(s, UserDetails.class);
        System.out.println(users); //deserialization of json object into java class object

        }


    }


