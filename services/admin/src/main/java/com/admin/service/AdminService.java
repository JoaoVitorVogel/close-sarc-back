package com.admin.service;


import com.admin.dto.ResourceCreateDto;
import com.admin.dto.GroupCreateDto;

import com.admin.dto.UserCreateDto;
import com.admin.entity.Group;
import com.admin.entity.Resource;
import com.admin.entity.User;

import com.admin.repository.GroupRepository;
import com.admin.repository.ResourceRepository;
import com.admin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Transactional
    public User addUser(UserCreateDto dto) {
        // TODO: Adicionar lógica para verificar se email já existe
        // TODO: Criptografar a senha antes de salvar, se precisar mas nn sei se precisa ainda

        User user = new User();
        user.setNome(dto.getNome());
        user.setEmail(dto.getEmail());
        user.setSenha(dto.getSenha());

        return userRepository.save(user);
    }

    @Transactional
    public Resource cadastrarRecurso(ResourceCreateDto dto) {
        Resource resource = new Resource();
        resource.setNome(dto.getNome());
        resource.setDescricao(dto.getDescricao());

        // Se o DTO não informar 'disponivel', assume o padrão 'true'
        if (dto.getDisponivel() != null) {
            resource.setDisponivel(dto.getDisponivel());
        } else {
            resource.setDisponivel(true);
        }

        return resourceRepository.save(resource);
    }

    public Group cadastrarGroup(GroupCreateDto dto) {
        Group group = new Group();
        group.setNome(dto.getNome());
        group.setCurso(dto.getCurso());
        group.setHorario(dto.getHorario());
        group.setProfessor_id(dto.getId_professor());


        return groupRepository.save(group);
    }
}