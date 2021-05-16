package com.egerton.realeaste.models;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "subscribers")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Subscriber {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO)
    private long id;
    private String username;
    private String email;
    private boolean is_active = true;
    @CreationTimestamp
    private LocalDateTime created_at;
    @UpdateTimestamp
    private LocalDateTime updated_at;
}
