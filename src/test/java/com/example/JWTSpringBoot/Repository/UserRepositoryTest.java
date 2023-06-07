package com.example.JWTSpringBoot.Repository;

import com.example.JWTSpringBoot.Entity.Role;
import com.example.JWTSpringBoot.Entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository repository;

    @Test
    void itShouldFindUserByEmail() {
        String email="user1@gmail.com";
        User user=new User("user1",
                "user1@gmail.com",
                "user1",
                Role.USER);
        repository.save(user);
        User user1=repository.findByEmail(email).get();
        assertEquals(email,user1.getEmail());
    }
}