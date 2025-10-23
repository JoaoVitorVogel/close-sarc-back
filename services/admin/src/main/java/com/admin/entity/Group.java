package com.admin.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "turma")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String curso;

    @Column(nullable = false)
    private String horario;

    @Column(nullable = false)
    private Long professor_id;

}