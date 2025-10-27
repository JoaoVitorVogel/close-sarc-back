package com.auth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "administrador")
public class Admin {
    @Id
    private Long id;
    private String email;
    private String senha;
    private String nome;
}