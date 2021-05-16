package com.egerton.realeaste.models;

import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "houses")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class House {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String description;
    private float price;
    private float deposit;
    private String type;
    private int stock;
    private int slots;
    private String capacity_unit;
    private int capacity;
    private String image;
    @ManyToOne(targetEntity = Agent.class, cascade = CascadeType.REMOVE)
    private Agent agent;
    @CreationTimestamp
    private LocalDateTime created_at;
    @UpdateTimestamp
    private LocalDateTime updated_at;

}
