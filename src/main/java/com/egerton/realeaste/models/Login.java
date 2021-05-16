package com.egerton.realeaste.models;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table( name = "logins")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Login {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private long id;
    private String email;
    private String token;
    private boolean is_active;
    private Date expiry_date;
    @CreationTimestamp
    private LocalDateTime login_at;
    @UpdateTimestamp
    private LocalDateTime logout_at;

    public Boolean comparePassword(String encryptedPassword, String plainPassword){
        boolean equal = BCrypt.checkpw(this.encryptPassword(plainPassword), encryptedPassword);
        return equal;
    }

    public String encryptPassword(String plainPassword){
        String password = BCrypt.hashpw(plainPassword, "nQ1zoi78n93");
        return password;
    }
}
