package com.example.JWTSpringBoot.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;



@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetToken {
    private static final int time = 60;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userid;
    private String token;
    private Date exp_time;
    @OneToOne(fetch = FetchType.EAGER, cascade
            = CascadeType.ALL)
    @JoinColumn(name = "id",
            nullable = false, foreignKey = @ForeignKey(name = "Password_Reset_Token"))
    private User user;


    public PasswordResetToken(String token) {
        this.token = token;
        this.exp_time = Calc(time);

    }

    public PasswordResetToken(User user, String token) {
        this.token = token;
        this.user = user;
        this.exp_time = Calc(time);
    }


    private Date Calc(int time) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(new Date().getTime());
        c.add(Calendar.MINUTE, time);
        return new Date(c.getTime().getTime());
    }
}
