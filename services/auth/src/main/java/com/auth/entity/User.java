package com.auth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
// Entidade "read-only" para o serviço de auth
@Data
@Entity
@Table(name = "professor")
public class User {
    @Id
    private Long id;
    private String email;
    private String senha;
    private String nome;
}