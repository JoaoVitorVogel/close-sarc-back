package com.admin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class GroupCreateDto {

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "O curso é obrigatório")
    private String curso;

    @NotBlank(message = "O horario é obrigatório")
    private String horario;

    @NotBlank(message = "O prof é obrigatório")
    private long id_professor;

}