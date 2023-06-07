package com.example.JWTSpringBoot.Event;
import com.example.JWTSpringBoot.Entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class RegisterEvent extends ApplicationEvent {
    private User user;
    private String applicationUrl;
    public RegisterEvent(User user, String applicationUrl)  {
        super(user);
        this.user=user;
        this.applicationUrl=applicationUrl;
    }
}