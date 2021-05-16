package com.egerton.realeaste.models;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "subscriptions")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private int year;
    private int month;
    private int date;
    private float amount;
    private String mpesa_code;
    private String mpesa_phone;
    private LocalDateTime start_date;
    private LocalDateTime end_date;
    private boolean is_active;
    @ManyToOne(targetEntity = Agent.class, cascade = CascadeType.REMOVE)
    private Agent agent;
    @CreationTimestamp
    private LocalDateTime created_at;
    @UpdateTimestamp
    private LocalDateTime updated_at;
}
