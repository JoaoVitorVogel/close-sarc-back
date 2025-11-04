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
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Transactional
    public User addUser(UserCreateDto dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        return userRepository.save(user);
    }

    @Transactional
    public Resource cadastrarRecurso(ResourceCreateDto dto) {
        Resource resource = new Resource();
        resource.setName(dto.getName());
        resource.setDescription(dto.getDescription());

        // If DTO doesn't specify 'available', defaults to 'true'
        if (dto.getAvailable() != null) {
            resource.setAvailable(dto.getAvailable());
        } else {
            resource.setAvailable(true);
        }

        return resourceRepository.save(resource);
    }

    @Transactional
    public Group cadastrarGroup(GroupCreateDto dto) {
        Group group = new Group();
        group.setName(dto.getName());
        group.setCourse(dto.getCourse());
        group.setSchedule(dto.getSchedule());
        group.setProfessorId(dto.getProfessorId());

        return groupRepository.save(group);
    }
}