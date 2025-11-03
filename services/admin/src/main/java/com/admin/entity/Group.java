package com.admin.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "class")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "course", length = 100)
    private String course;

    @Column(name = "schedule", length = 50, nullable = false)
    private String schedule;

    @Column(name = "professor_id", nullable = false)
    private Long professorId;
}