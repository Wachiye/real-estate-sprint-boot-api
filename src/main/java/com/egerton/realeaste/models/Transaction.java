package com.egerton.realeaste.models;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "transactions")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Transaction{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String MerchantRequestID;
    private String CheckoutRequestID;
    private int ResultCode;
    private String ResultDesc;
    private long TransactionDate;
    private String MpesaReceiptNumber;
    private double Amount;
    private long PhoneNumber;
    @CreationTimestamp
    private LocalDateTime created_at;
    private boolean Accepted;
    private boolean used;
}
