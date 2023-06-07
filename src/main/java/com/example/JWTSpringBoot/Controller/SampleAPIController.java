package com.example.JWTSpringBoot.Controller;

import com.example.JWTSpringBoot.Entity.User;
import com.example.JWTSpringBoot.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@org.springframework.web.bind.annotation.RestController
@RequiredArgsConstructor

public class SampleAPIController {
    private final UserRepository userRepository;
    @GetMapping("/sampleAPIRequest")
    public User userDetails(@RequestParam("email") String email)
    {
       Optional<User> user=userRepository.findByEmail(email);
       return user.get();
    }
    @GetMapping("/sampleAPIRequestAll")
    public List allUserDetails()
    {
        List user=userRepository.findAll();
        return user;
    }
}
