package com.example.backend.controller;


import com.example.backend.dto.*;
import com.example.backend.service.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // ================= REGISTER CANDIDATE =================
    @PostMapping("/register/candidate")
    public ResponseEntity<String> registerCandidate(
            @RequestBody @Valid CandidateRegisterRequest request) {

        authService.registerCandidate(request);
        return ResponseEntity.ok("Candidate registered successfully");
    }

    // ================= REGISTER RECRUITER =================
    @PostMapping("/register/recruiter")
    public ResponseEntity<String> registerRecruiter(
            @RequestBody @Valid RecruiterRegisterRequest request) {

        authService.registerRecruiter(request);
        return ResponseEntity.ok("Recruiter registered successfully");
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody @Valid LoginRequest request) {

        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
