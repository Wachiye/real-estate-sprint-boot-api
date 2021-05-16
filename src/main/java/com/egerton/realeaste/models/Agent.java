package com.egerton.realeaste.models;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "agents")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Agent {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO)
    private long id;
    private String first_name;
    private String last_name;
    private String email;
    private String phone;
    private String location;
    private String password;
    private Long mpesa_till_number;
    private String mpesa_account_name;
    private String business_name;
    @CreationTimestamp
    private LocalDateTime created_at;
    @UpdateTimestamp
    private LocalDateTime updated_at;
}