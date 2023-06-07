package com.example.JWTSpringBoot.Event;

import com.example.JWTSpringBoot.Entity.User;
import com.example.JWTSpringBoot.Service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class RegListener implements ApplicationListener<RegisterEvent> {
    @Autowired
    private AuthenticationService service;
    @Override
    public void onApplicationEvent(RegisterEvent event) {
        //create verification token
        User user=event.getUser();
        String token= UUID.randomUUID().toString();
        service.saveToken(token,user);
        //send mail to user
        String url=event.getApplicationUrl()+ "/verifyReg?token=" + token;
        //send ver email
        log.info("click the link to verify ur account :{}", url);
    }
}
