package com.example.JWTSpringBoot.Service;

import com.example.JWTSpringBoot.Model.Authenticationrequest;
import com.example.JWTSpringBoot.Model.Authenticationresponse;
import com.example.JWTSpringBoot.Model.RegisterRequest;
import com.example.JWTSpringBoot.Entity.User;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public interface UserService {
    User register(User user, RegisterRequest req);
    String applicationUrl(HttpServletRequest request);
    Authenticationresponse authenticate(Authenticationrequest request);
    String saveToken(String token, User user);
    String validateToken(String token);
    String resendVerificationToken(String email,HttpServletRequest request);
    void passwordResetLink(User user, String token);
    String passwordResetUrl(User user, String applicationUrl, String token);
    String verifyPasswordResetToken(String token);
    Optional<User> getUserByPasswordResetToken(String token);
    Optional<User> findUserByEmail(String email);
   // boolean validateOldPassword(User user, String oldPassword);
    String changeUserPassword(String email, String oldPassword, String newPassword);

    void changePassword(User user, String newPassword);
}
 /*
    public boolean validateOldPassword(User user, String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }
     */
