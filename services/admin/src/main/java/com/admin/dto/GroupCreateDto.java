package com.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GroupCreateDto {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Course is required")
    private String course;

    @NotBlank(message = "Schedule is required")
    private String schedule;

    @NotNull(message = "Professor ID is required")
    private Long professorId;

    // Getters explícitos para garantir compatibilidade
    public String getName() {
        return name;
    }

    public String getCourse() {
        return course;
    }

    public String getSchedule() {
        return schedule;
    }

    public Long getProfessorId() {
        return professorId;
    }
}