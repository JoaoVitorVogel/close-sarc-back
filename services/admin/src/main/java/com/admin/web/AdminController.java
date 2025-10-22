package com.admin.web;


import com.admin.dto.ResourceCreateDto;
import com.admin.dto.UserCreateDto;

import com.admin.entity.Resource;
import com.admin.entity.User;
import com.admin.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin") // Prefixo para todas as rotas de admin
public class AdminController {

    @Autowired
    private AdminService adminService;


    @PostMapping("/professores")
    public ResponseEntity<User> cadastrarProfessor(@Valid @RequestBody UserCreateDto dto) {
        User newUser = adminService.addUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }


    @PostMapping("/recursos")
    public ResponseEntity<Resource> cadastrarRecurso(@Valid @RequestBody ResourceCreateDto dto) {
        Resource novoRecurso = adminService.cadastrarRecurso(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoRecurso);
    }
}