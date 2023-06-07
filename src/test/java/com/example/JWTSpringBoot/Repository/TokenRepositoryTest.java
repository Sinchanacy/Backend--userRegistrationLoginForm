package com.example.JWTSpringBoot.Repository;

import com.example.JWTSpringBoot.Entity.Role;
import com.example.JWTSpringBoot.Entity.Tokens;
import com.example.JWTSpringBoot.Entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class TokenRepositoryTest {
    @Autowired
    private TokenRepository tokenRepository;
    @Test
    void FindUserByToken_sucess() {
        String VerifyToken="user1@token";
        Tokens token=new Tokens(1,"user1@token",new Date(), User.builder().build());
        tokenRepository.save(token);
        Tokens token1 =tokenRepository.findByToken(VerifyToken);
        assertEquals(VerifyToken,token1.getToken());
    }
}