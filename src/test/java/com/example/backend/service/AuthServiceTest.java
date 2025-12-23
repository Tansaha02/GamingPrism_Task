package com.example.backend.service;

import com.example.backend.dto.CandidateRegisterRequest;
import com.example.backend.entity.Candidate;
import com.example.backend.entity.Role;
import com.example.backend.repository.CandidateRepository;
import com.example.backend.repository.RecruiterRepository;
import com.example.backend.repository.RoleRepository;
import com.example.backend.security.JwtUtil;
import com.example.backend.service.AuthService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private RecruiterRepository recruiterRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @Test
    void registerCandidate_success() {

        CandidateRegisterRequest request = new CandidateRegisterRequest();
        request.setName("Test");
        request.setEmail("test@test.com");
        request.setPassword("pass123");
        request.setExperience(2);
        request.setSkills("Java");
        request.setLocation("Kolkata");

        Role role = new Role();
        role.setName("ROLE_CANDIDATE");

        when(candidateRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(roleRepository.findByName("ROLE_CANDIDATE")).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(anyString())).thenReturn("encoded-pass");

        authService.registerCandidate(request);

        verify(candidateRepository, times(1)).save(any(Candidate.class));
    }

    @Test
    void registerCandidate_duplicateEmail_shouldFail() {

        CandidateRegisterRequest request = new CandidateRegisterRequest();
        request.setEmail("test@test.com");

        when(candidateRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(RuntimeException.class,
                () -> authService.registerCandidate(request));
    }
}
