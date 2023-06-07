package com.example.JWTSpringBoot.Repository;

import com.example.JWTSpringBoot.Entity.Tokens;
import com.example.JWTSpringBoot.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Tokens,Integer> {
    Tokens findByToken(String token);
    Optional<Tokens> findById(Integer id);
    Tokens findByUser(User user);
}
