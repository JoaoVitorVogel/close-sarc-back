package com.auth.service;

import com.auth.repository.AdminRepository;
import com.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    // O "username" aqui é o email
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. Tenta encontrar como user
        var userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            var prof = userOpt.get();
            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_user"));
            // Passamos email, password HASHED, e roles
            return new User(prof.getEmail(), prof.getPassword(), authorities);
        }


        var adminOpt = adminRepository.findByEmail(email);
        if (adminOpt.isPresent()) {
            var admin = adminOpt.get();
            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
            return new User(admin.getEmail(), admin.getPassword(), authorities);
        }


        throw new UsernameNotFoundException("Usuário não encontrado com o email: " + email);
    }
}