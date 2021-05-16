package com.egerton.realeaste.models;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "bookings")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Booking {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO)
    private long id;
    private String first_name;
    private String last_name;
    private String email;
    private String phone;
    @ManyToOne( targetEntity = House.class)
    private House house;
    private int slots;
    @CreatedDate
    private Date booking_date;
    @OneToOne( targetEntity = Deposit.class)
    private Deposit deposit;
}
