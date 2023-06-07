package com.example.JWTSpringBoot.Service;

import com.example.JWTSpringBoot.Configuration.JWTService;
import com.example.JWTSpringBoot.Model.Authenticationrequest;
import com.example.JWTSpringBoot.Model.Authenticationresponse;
import com.example.JWTSpringBoot.Model.RegisterRequest;
import com.example.JWTSpringBoot.Entity.PasswordResetToken;
import com.example.JWTSpringBoot.Entity.Role;
import com.example.JWTSpringBoot.Entity.Tokens;
import com.example.JWTSpringBoot.Entity.User;
import com.example.JWTSpringBoot.Event.RegisterEvent;
import com.example.JWTSpringBoot.Repository.PasswordResetTokenRepository;
import com.example.JWTSpringBoot.Repository.TokenRepository;
import com.example.JWTSpringBoot.Repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService implements UserService{
    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    private final AuthenticationManager authenticationManager;
    @Override
    public User register(User user,RegisterRequest request) {
         user= User.builder()
                .firstname(request.getFirstname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .isenabled(false)
                .build();
        repository.save(user);
        return user;
    }
    @Override
    public Authenticationresponse authenticate(Authenticationrequest request) {
        var user=repository.findByEmail(request.getEmail()).orElse(null);
        if(user==null)
        {
            return Authenticationresponse.builder().build();
        }
        Authentication authentication=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword())
        );
        System.out.println(authentication);
        var jwtToken=jwtService.generateToken(user);
        return Authenticationresponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public String saveToken(String token, User user) {
        Tokens tokens=tokenRepository.findByUser(user);
        if(tokens==null) {
            Tokens t = new Tokens(token, user);
            tokenRepository.save(t);
            return t.getToken();
        }
        System.out.println(tokens);
        tokens.setToken(token);
        tokens.setExp_time();
        tokenRepository.save(tokens);
        return tokens.getToken();
    }
    @Override
    public String validateToken(String token) {
        Tokens verToken=tokenRepository.findByToken(token);
        if(verToken==null)
        {
            return "invalid";
        }
        User user=verToken.getUser();
        Calendar calendar=Calendar.getInstance();
        if(verToken.getExp_time().getTime()-calendar.getTime().getTime()<=0)
        {
            tokenRepository.delete(verToken);
            return "expired";
        }
        user.setIsenabled(true);
        repository.save(user);
        return "valid";
    }
    @Override
    public  String applicationUrl(HttpServletRequest request) {
        return "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
    }
    @Override
    public String resendVerificationToken(String email,final HttpServletRequest request) {
        Optional<User> user=this.findUserByEmail(email);
        if(user.isEmpty()){
            return "emailNotRegistered";
        }
        applicationEventPublisher.publishEvent(new RegisterEvent(
                        user.get(),
                        applicationUrl(request)
                )
        );
        return "Verification Link Sent Successfully";
    }
    @Override
    public Optional<User> findUserByEmail(String email) {
        return repository.findByEmail(email); }
    @Override
    public void passwordResetLink(User user, String token)throws NullPointerException {
        if(user==null||token==null)
        {
            throw new NullPointerException("null");
        }
        PasswordResetToken passwordResetToken=new PasswordResetToken(user,token);
        passwordResetTokenRepository.save(passwordResetToken);
    }
    @Override
    public String passwordResetUrl(User user, String applicationUrl, String token) {
        String url=applicationUrl+"/savePassword?token="+ token;
        log.info("click the link to reset ur password:{}", url);
        return url;
    }
    @Override
    public String verifyPasswordResetToken(String token) {
        PasswordResetToken resetToken=passwordResetTokenRepository.findByToken(token);
        if(resetToken==null)
        {
            return "invalid";
        }
        User user= resetToken.getUser();
        Calendar calendar=Calendar.getInstance();
        if(resetToken.getExp_time().getTime()-calendar.getTime().getTime()<=0)
        {
            passwordResetTokenRepository.delete(resetToken);
            return "expired";
        }
        return "valid";
    }
    @Override
    public Optional<User> getUserByPasswordResetToken(String token) {
        return Optional.ofNullable(passwordResetTokenRepository.findByToken(token).getUser());}
    @Override
    public String changeUserPassword(String email ,String oldPassword, String newPassword) {
        Optional<User> user =repository.findByEmail(email);
        if(user.isEmpty())
          return "NULL";
        if (!passwordEncoder.matches(oldPassword, user.get().getPassword())) {
            return "NoMatch";
        }
        user.get().setPassword(passwordEncoder.encode(newPassword));
        repository.save(user.get());
        return "Success"; }
    @Override
    public void changePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        repository.save(user);}
}