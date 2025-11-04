package com.admin.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "professor")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Usa BIGSERIAL
    private Long id;

    @Column(length = 100)
    private String name;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column(length = 255, nullable = false)
    private String password; //criptografar talvez nn sei se precisa :P


}