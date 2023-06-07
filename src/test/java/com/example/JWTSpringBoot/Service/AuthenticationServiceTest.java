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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)

class AuthenticationServiceTest {
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private  ApplicationEventPublisher publisher;
    @Mock
    private UserRepository repository;
    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JWTService jwtService;
    @Mock
    private PasswordResetTokenRepository forgetPasswordTokenRepository;
    @InjectMocks
    private AuthenticationService service;

    @BeforeEach
    void setUp() {
    }

    @Test
    public void ForEmailPresent_verifyFindUserByEmail()
    {
        User user=User.builder().email("newuser1@gmail.com")
                .id(1)
                .firstname("newuser1")
                .password("newuser1")
                .role(Role.USER)
                .build();
        Mockito.when(repository.findByEmail("newuser1@gmail.com")).thenReturn(Optional.ofNullable(user));
        String email="newuser1@gmail.com";
        User user1=service.findUserByEmail(email).get();
        assertEquals(email,user1.getEmail());
    }
    @Test
    public void ForEmailNotPresent_ReturnNotFound()
    {
        String email="newuser@gmail.com";
        boolean a=service.findUserByEmail(email).isPresent();
        assertFalse(a);
    }
    @Test
    public void CreateUserSuccess()
    {
        String password="newuser1";
        User user=User.builder()
                .build();
        RegisterRequest request=RegisterRequest.builder()
                .email("newuser1@gmail.com")
            .firstname("newuser1")
            .password("newuser1").build();
        User user1= User.builder()
                .email(request.getEmail())
                .firstname(request.getPassword())
                .password(request.getPassword())
                .isenabled(false)
                .role(Role.USER).build();
        Mockito.when(passwordEncoder.encode(password)).thenReturn(password);
        user=service.register(user,request);
        System.out.println(user);
        assertEquals(user1,user);

    }
    @Test
    public void AuthenticateNotExistingUser_ThrowException()
    {
        String email="newuser@gmail.com";
        boolean a=service.findUserByEmail(email).isPresent();
        User user= User.builder().build();
        Authenticationrequest authenticationrequest= Authenticationrequest.builder().build();
        Authenticationresponse ab=service.authenticate(authenticationrequest);
        System.out.println(ab);
    }
    @Test
    public void AuthenticateInvalidUsernamePassword_ThrowException()
    {
        String email="newuser@gmail.com";
        User user= User.builder()
                .email("newuser1@gmail.com")
                .id(1)
                .firstname("newuser1")
                .password("newuser1")
                .role(Role.USER)
                .isenabled(true)
                .build();
        Mockito.when(repository.findByEmail("newuser1@gmail.com")).thenReturn(Optional.ofNullable(user));
        Authenticationrequest authenticationrequest=Authenticationrequest.builder()
                .email("newuser1@gmail.com")
                .password("newuser").build();
        Mockito.when(jwtService.generateToken(user)).thenReturn(null);
        Authentication authentication=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationrequest.getEmail(),
                        authenticationrequest.getPassword())
        );

        Authenticationresponse au=service.authenticate(authenticationrequest);
        System.out.println(au);
       // assertNull(au);
    }
    @Test
    public void AuthenticateValidUsernamePassword_ThrowException() {
        String email = "newuser@gmail.com";
        User user = User.builder()
                .email("newuser1@gmail.com")
                .id(1)
                .firstname("newuser1")
                .password("newuser1")
                .role(Role.USER)
                .isenabled(true)
                .build();
        Authenticationrequest authenticationrequest = Authenticationrequest.builder()
                .email("newuser1@gmail.com")
                .password("newuser1").build();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationrequest.getEmail(),
                        authenticationrequest.getPassword())
        );
        Authentication au = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationrequest.getEmail(),
                        authenticationrequest.getPassword())
        );
        System.out.println(authentication);

    }
    @Test
    public void ForValidRegistrationVerificationToken_ReturnRegistrationTokenFound()
    {
        User user=User.builder().email("newuser1@gmail.com")
                .id(1)
                .firstname("newuser1")
                .password("newuser1")
                .role(Role.USER)
                .build();
        String VerifyToken="user1@token";
        Tokens token=Tokens.builder()
                .token("user1@token")
                .userid(1)
                .exp_time(new Date(System.currentTimeMillis() + (24 * 60 * 60)))
                .user(user)
                .build();
        Mockito.when(tokenRepository.findByToken("user1@token")).thenReturn(token);
        user.setIsenabled(true);
        Mockito.when(repository.save(user)).thenReturn(user);
        String returnedEmailVerificationTokenStatus =service.validateToken(VerifyToken);
        assertEquals("valid" , returnedEmailVerificationTokenStatus);
    }

    @Test
    public void ForInvalidRegistrationVerificationToken_ReturnRegistrationTokenInvalid()
    {
        String token="user2@token";
        Tokens tokens=new Tokens();
        Mockito.when(tokenRepository.findByToken("user2@token"))
                .thenReturn(null);
        String verificationStatus=service.validateToken(token);
        System.out.println(verificationStatus);
        assertEquals(verificationStatus,"invalid");

    }
    @Test
    public void ForValidRegistrationVerificationTokenExpired_ReturnRegistrationTokenExpired()
    {
        String VerifyToken="user1@token";
        Tokens token=Tokens.builder()
                .token("user1@token")
                .userid(1)
                .exp_time(new Date())
                .user(User.builder().email("newuser1@gmail.com")
                        .id(1)
                        .firstname("newuser1")
                        .password("newuser1")
                        .role(Role.USER)
                        .isenabled(false)
                        .build())
                .build();
        Mockito.when(tokenRepository.findByToken("user1@token")).thenReturn(token);
        String VerificationStatus=service.validateToken(VerifyToken);
        assertEquals("expired",VerificationStatus);
    }
    /*
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

     */
    @Test
    public void IfTokenNotFound_ResendVerificationTokenEmailNotRegistered() {
        HttpServletRequest  request = Mockito.mock(HttpServletRequest.class);
        //Optional<User> user= Optional.of(new User());
        String emailToBeVerified="user@gmail.com";
        Mockito.when(service.findUserByEmail(emailToBeVerified)).thenReturn(Optional.empty());
        //System.out.println(user.isEmpty());
        String status=service.resendVerificationToken("user@gmail.com",request);
        assertEquals("emailNotRegistered",status);

    }

    @Test
    @Disabled
    public void IfTokenNotFound_ResendVerificationTokenSuccess()
    {
      /*  HttpServletRequest  request = Mockito.mock(HttpServletRequest.class);
        String emailToBeVerified="user@gmail.com";
        User user = User
                .builder()
                .id(1)
                .firstname("newuser")
                .email("user@gmail.com")
                .password("pass1")
                .role(Role.USER)
                .isenabled(false)
                .build();
        Mockito.when(service.findUserByEmail(emailToBeVerified)).thenReturn(Optional.ofNullable(user));
        //Mockito.when(applicationEventPublisher.publishEvent(new RegisterEvent(user,service.applicationUrl(request))).thenReturn(Optional.ofNullable(user));
        String status=service.resendVerificationToken( email,
                httpServletRequest);
        System.out.println(status);
       */
        String email = "hi@gmail.com";
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        User user = User
                .builder()
                .id(1)
                .firstname("Oskar")
                .email("hi@gmail.com")
                .password("123")
                .role(Role.USER)
                .isenabled(true)
                .build();


        Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(user));
        String status=service.resendVerificationToken( email,
                httpServletRequest);
        // assertions
        ArgumentCaptor<RegisterEvent> captor = ArgumentCaptor.forClass(RegisterEvent.class);
        verify(publisher).publishEvent(captor.capture());
        verify(repository).findByEmail(email);
        //assertEquals(user, captor.getValue().getUser());
       // assertEquals("http://null:0null", captor.getValue().getApplicationUrl());


    }
    @Test
    public void forInvalidEmail_ReturnNULL()
    {
        Mockito.when(repository.findByEmail("user@gmail.com")).thenReturn(Optional.empty());
        String status = service.changeUserPassword(
                "user@gmail.com",
                "pass2",
                "pass1"
        );
        assertEquals("NULL",status);
    }
    @Test
    public void forInValidOldPassword_ReturnPasswordNoMatch()
    {
        User user = User
                .builder()
                .id(1)
                .firstname("newuser")
                .email("user1@gmail.com")
                .password("pass1")
                .role(Role.USER)
                .isenabled(true)
                .build();
        Mockito.when(repository.findByEmail("user1@gmail.com")).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches("pass2", "pass1")).thenReturn(false);

        String status = service.changeUserPassword(
                "user1@gmail.com",
                "pass2",
                "pass1"
        );
        assertEquals("NoMatch",status);
    }
    @Test
    public void forValidEmailAndOldPassword_ReturnPasswordChangeSuccess()
    {
        User user = User
                .builder()
                .id(1)
                .firstname("newuser")
                .email("user1@gmail.com")
                .password("pass1")
                .role(Role.USER)
                .isenabled(true)
                .build();
        Mockito.when(repository.findByEmail("user1@gmail.com")).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches("pass1", "pass1")).thenReturn(true);

        String status = service.changeUserPassword(
                "user1@gmail.com",
                "pass1",
                "pass2"
        );
        assertEquals("Success",status);
    }
    @Test
    public void whenUserNull_throwNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            service.passwordResetLink(null, "testToken");
        });
    }
    @Test
    public void whenTokenNull_throwNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            service.passwordResetLink(mock(User.class), null);
        });
    }
    @Test
    public void verifyPasswordResetTokenForInvalidToken_ReturnTokenInvalid()
    {
        String passwordToken="user2@token";
        PasswordResetToken tokens=new PasswordResetToken();
        Mockito.when(forgetPasswordTokenRepository.findByToken("user2@token"))
                .thenReturn(null);
        String verificationStatus=service.verifyPasswordResetToken(passwordToken);
        System.out.println(verificationStatus);
        assertEquals(verificationStatus,"invalid");
    }
    @Test
    public void verifyPasswordResetTokenForTokenExpired_ReturnTokenExpired()
    {
        String VerifyToken="user1@token";
        PasswordResetToken token=PasswordResetToken.builder()
                .token("user1@token")
                .userid(1)
                .exp_time(new Date())
                .user(User.builder().email("user1@gmail.com")
                        .id(1)
                        .firstname("newuser1")
                        .password("newuser1")
                        .role(Role.USER)
                        .isenabled(true)
                        .build())
                .build();
        Mockito.when(forgetPasswordTokenRepository.findByToken("user1@token"))
                .thenReturn(token);
        String verificationStatus=service.verifyPasswordResetToken(VerifyToken);
        System.out.println(verificationStatus);
        assertEquals(verificationStatus,"expired");
    }
    @Test
    public void verifyPasswordResetTokenForValidToken_ReturnTokenValid()
    {
        String VerifyToken="user1@token";
        PasswordResetToken token=PasswordResetToken.builder()
                .token("user1@token")
                .userid(1)
                .exp_time(new Date(System.currentTimeMillis() + (24 * 60 * 60)))
                .user(User.builder().email("user1@gmail.com")
                        .id(1)
                        .firstname("newuser1")
                        .password("newuser1")
                        .role(Role.USER)
                        .isenabled(true)
                        .build())
                .build();
        Mockito.when(forgetPasswordTokenRepository.findByToken("user1@token"))
                .thenReturn(token);
        String verificationStatus=service.verifyPasswordResetToken(VerifyToken);
        System.out.println(verificationStatus);
        assertEquals(verificationStatus,"valid");
    }
    @Test
    @Disabled
    public void IfUserValid_PasswordResetUrlSentSuccess()
    {
        HttpServletRequest  request = Mockito.mock(HttpServletRequest.class);
        String resetToken="user1@token";
        User user=User.builder()
                .email("user1@gmail.com")
                .id(1)
                .firstname("newuser1")
                .password("newuser1")
                .role(Role.USER)
                .isenabled(true)
                .build();
        PasswordResetToken token=PasswordResetToken.builder()
                .token("user1@token")
                .userid(1)
                .exp_time(new Date(System.currentTimeMillis() + (24 * 60 * 60)))
                .user(user)
                .build();
        String applicationUrl=service.applicationUrl(request);
        System.out.println(user);
        String url=service.passwordResetUrl(user,service.applicationUrl(request),resetToken);
        assertEquals(applicationUrl+"/savePassword?token="+resetToken,url);
    }
    @Test
    public void ForValidUserAndToken_returnPasswordTokenSavedSuccess(){
        String resetToken = "user1@token";
        User user = User.builder()
                .email("user1@gmail.com")
                .id(1)
                .firstname("newuser1")
                .password("newuser1")
                .role(Role.USER)
                .isenabled(true)
                .build();
        PasswordResetToken token = PasswordResetToken.builder()
                .token("user1@token")
                .userid(1)
                .exp_time(new Date(System.currentTimeMillis() + (24 * 60 * 60)))
                .user(user)
                .build();
        PasswordResetToken passwordResetToken=new PasswordResetToken(user,resetToken);
        service.passwordResetLink(user,resetToken);
    }
    @Test
    public void ForValidPasswordToken_ReturnUser() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        String resetToken = "user1@token";
        User user = User.builder()
                .email("user1@gmail.com")
                .id(1)
                .firstname("newuser1")
                .password("newuser1")
                .role(Role.USER)
                .isenabled(true)
                .build();
        PasswordResetToken token = PasswordResetToken.builder()
                .token("user1@token")
                .userid(1)
                .exp_time(new Date(System.currentTimeMillis() + (24 * 60 * 60)))
                .user(user)
                .build();
        Mockito.when(forgetPasswordTokenRepository.findByToken("user1@token"))
                .thenReturn(token);
        System.out.println(token.getUser());
        Optional<User> user1=service.getUserByPasswordResetToken(resetToken);
        User user2=user1.get();
        System.out.println(user2);
        assertEquals(user2,token.getUser());
    }
    @Test
    public void ForValidResetPasswordToken_ReturnPasswordUpdateSuccess()
    {
        User user = User.builder()
                .email("user1@gmail.com")
                .id(1)
                .firstname("newuser1")
                .password("newuser1")
                .role(Role.USER)
                .isenabled(true)
                .build();
        String newPassword="newUser2";
        Mockito.when(passwordEncoder.encode(newPassword)).thenReturn(newPassword);
        user.setPassword(newPassword);
        User user1=new User();
        service.changePassword(user1,newPassword);
        System.out.println(user1.getPassword());
        assertEquals(newPassword,user1.getPassword());
    }

    @Test
    public void forUserRegistered_SaveRegistrationVerificationTokenSuccess()
    {
        User user = User.builder()
                .email("user1@gmail.com")
                .id(1)
                .firstname("newuser1")
                .password("newuser1")
                .role(Role.USER)
                .isenabled(true)
                .build();
        String tokens="newUser@token";
        Tokens tokens1=new Tokens(tokens,user);
        String token2=service.saveToken(tokens,user);
        assertEquals(tokens1.getToken(),token2);
    }
    /*
     public void saveToken(String token , User user) {
        Tokens tokens=tokenRepository.findByUser(user);
        if(tokens==null) {
            Tokens t = new Tokens(token, user);
            tokenRepository.save(t);
            return;
        }
        tokens.setToken(token);
        tokens.setExp_time();
        tokenRepository.save(tokens);
    }
     */
    @Test
    public void ForUserRegisteredAndRegistrationTokenExpiredOrNotFound_SaveResendVerificationTokenSuccess()
    {
        String VerifyToken="user2@token";
        User user=User.builder().email("newuser1@gmail.com")
                .id(1)
                .firstname("newuser1")
                .password("newuser1")
                .role(Role.USER)
                .isenabled(false)
                .build();
        Tokens token=Tokens.builder()
                .token("user1@token")
                .userid(1)
                .exp_time(new Date())
                .user(user)
                .build();
        Mockito.when(tokenRepository.findByUser(user)).thenReturn(token);
        System.out.println(tokenRepository.findByUser(user));
        token.setToken(VerifyToken);
        token.setExp_time();
        String token2=service.saveToken(VerifyToken,user);
        assertEquals(token.getToken(),token2);

    }
}