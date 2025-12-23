package com.example.backend.config;

import com.example.backend.entity.Role;
import com.example.backend.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void initRoles() {
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role(null, "ROLE_CANDIDATE"));
            roleRepository.save(new Role(null, "ROLE_RECRUITER"));
        }
    }
}

