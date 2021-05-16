package com.egerton.realeaste.models;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "deposits")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Deposit {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private long id;
   private long mpesa_till_number;
   private String mpesa_business_name;
   private String mpesa_account_name;
   private String mpesa_phone;
   private String mpesa_code;
   private float amount;
   private String purpose;
   @CreationTimestamp
   private LocalDateTime created_at;
   @UpdateTimestamp
   private LocalDateTime updated_at;
}
