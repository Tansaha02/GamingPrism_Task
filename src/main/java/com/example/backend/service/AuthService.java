package com.example.backend.service;

import com.example.backend.dto.*;
import com.example.backend.entity.*;
import com.example.backend.repository.*;
import com.example.backend.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final CandidateRepository candidateRepository;
    private final RecruiterRepository recruiterRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthService(
            CandidateRepository candidateRepository,
            RecruiterRepository recruiterRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil) {

        this.candidateRepository = candidateRepository;
        this.recruiterRepository = recruiterRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

 // ================= REGISTER CANDIDATE =================
    public void registerCandidate(CandidateRegisterRequest request) {

        if (candidateRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Candidate already exists with this email");
        }

        Role role = roleRepository.findByName("ROLE_CANDIDATE")
                .orElseThrow(() -> new RuntimeException("ROLE_CANDIDATE not found"));

        Candidate candidate = new Candidate();
        candidate.setName(request.getName());
        candidate.setEmail(request.getEmail());
        candidate.setPassword(passwordEncoder.encode(request.getPassword()));
        candidate.setExperience(request.getExperience());
        candidate.setSkills(request.getSkills());
        candidate.setLocation(request.getLocation());
        candidate.setRole(role);

        candidateRepository.save(candidate);
    }


    // ================= REGISTER RECRUITER =================
    public void registerRecruiter(RecruiterRegisterRequest request) {

        if (recruiterRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Recruiter already exists with this email");
        }

        Role role = roleRepository.findByName("ROLE_RECRUITER")
                .orElseThrow(() -> new RuntimeException("ROLE_RECRUITER not found"));

        Recruiter recruiter = Recruiter.builder()
                .name(request.getName())
                .company(request.getCompany())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();

        recruiterRepository.save(recruiter);
    }

    // ================= LOGIN =================
    public LoginResponse login(LoginRequest request) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (AuthenticationException ex) {
            throw new RuntimeException("Invalid email or password");
        }

        String role;

        if (candidateRepository.findByEmail(request.getEmail()).isPresent()) {
            role = "ROLE_CANDIDATE";
        } else if (recruiterRepository.findByEmail(request.getEmail()).isPresent()) {
            role = "ROLE_RECRUITER";
        } else {
            throw new RuntimeException("User role not found");
        }

        String token = jwtUtil.generateToken(request.getEmail(), role);

        return new LoginResponse(token, role);
    }
}
