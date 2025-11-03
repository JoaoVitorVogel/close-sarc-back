package com.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResourceCreateDto {

    @NotBlank(message = "Resource name is required")
    private String name;

    private String description;

    private Boolean available;
}