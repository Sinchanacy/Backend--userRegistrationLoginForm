package com.example.JWTSpringBoot.Controller;
import com.example.JWTSpringBoot.Entity.User;
import com.example.JWTSpringBoot.Event.RegisterEvent;
import com.example.JWTSpringBoot.Model.Authenticationrequest;
import com.example.JWTSpringBoot.Model.Authenticationresponse;
import com.example.JWTSpringBoot.Model.PasswordModel;
import com.example.JWTSpringBoot.Model.RegisterRequest;
import com.example.JWTSpringBoot.Repository.UserRepository;
import com.example.JWTSpringBoot.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:3000")
@org.springframework.web.bind.annotation.RestController
@RequiredArgsConstructor

public class RestController {
    private final UserService service;
    private final UserRepository repository;
    private final ApplicationEventPublisher publisher;

    @PostMapping("welcome/register")
    public ResponseEntity<User> registering(@RequestBody RegisterRequest req, final HttpServletRequest request)
    {
        User user=new User();
        user= ResponseEntity.ok(service.register(user,req)).getBody();
        publisher.publishEvent(new RegisterEvent(user,service.applicationUrl(request)));
        return ResponseEntity.ok(user);
    }
    @PostMapping("welcome/authenticate")
    public ResponseEntity<Authenticationresponse> authenticating(@RequestBody Authenticationrequest request)
    {
        return  ResponseEntity.ok(service.authenticate(request));
    }
    @GetMapping("/verifyReg")
    public String verifyRegistration(@RequestParam("token") String token)
    {
        String result= service.validateToken(token);
        System.out.println(result);
        if(result.equalsIgnoreCase("valid"))
        {
            System.out.println(result);
            return "User Verified Successfully";
        }
        return "Bad User";
    }
    @GetMapping("/ResendVerify")
    public String resendVerifyToken(@RequestParam("email") String email, HttpServletRequest request)
    {
        String status=service.resendVerificationToken(email,request);
        return status;
    }
    @PostMapping("/resetpassword")
    public String resetPassword(@RequestBody PasswordModel passwordModel,HttpServletRequest request)
    {
        Optional<User> user1=repository.findByEmail(passwordModel.getEmail());
        User user=user1.get();
        String url="";
        if(user!=null)
        {
             String token= UUID.randomUUID().toString();
             service.passwordResetLink(user,token);
             url=service.passwordResetUrl(user,service.applicationUrl(request),token);
        }
        return url;
    }
    @PostMapping("/savePassword")
    public String savePassword(@RequestParam("token") String token,@RequestBody PasswordModel passwordModel)
    {
        String result=service.verifyPasswordResetToken(token);
        if(!result.equalsIgnoreCase("valid"))
        {
            return "invalid token";
        }
        Optional<User>user=service.getUserByPasswordResetToken(token);
        if(user.isPresent())
        {
            service.changePassword(user.get(),passwordModel.getNewPassword());
            return "Password Reset Successfully";

        }
        else {
             return "invalid token";
        }
    }
    @PostMapping("/changePassword")
    public String changePassword(@RequestBody PasswordModel model)
    {
        String result=service.changeUserPassword(model.getEmail(),model.getOldPassword(), model.getNewPassword());
        switch(result)
        {
            case "NULL": return "Email Not Found";
            case "NoMatch":return "Password does not Match";
            case "Success":return "Password Changed Successfully";
            default:return "unknown error";
        }
    }

}