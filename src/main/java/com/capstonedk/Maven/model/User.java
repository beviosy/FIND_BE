package com.capstonedk.Maven.model;

import jakarta.persistence.*;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String loginId;
    private String password;
    private String nickname;
    private String email;

    @OneToMany(mappedBy = "user")
    private List<Review> reviews;
}
