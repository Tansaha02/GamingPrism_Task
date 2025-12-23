package com.example.backend.controller;

import com.example.backend.dto.CandidateUpdateRequest;
import com.example.backend.entity.Candidate;
import com.example.backend.service.CandidateService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/candidates")
public class CandidateController {

    private final CandidateService candidateService;

    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    // GET PROFILE
    @PreAuthorize("hasRole('CANDIDATE')")
    @GetMapping("/profile")
    public ResponseEntity<Candidate> getProfile(Authentication authentication) {
        return ResponseEntity.ok(
                candidateService.getProfile(authentication.getName())
        );
    }

    // UPDATE PROFILE
    @PreAuthorize("hasRole('CANDIDATE')")
    @PutMapping("/profile")
    public ResponseEntity<Candidate> updateProfile(
            Authentication authentication,
            @RequestBody CandidateUpdateRequest request) {

        return ResponseEntity.ok(
                candidateService.updateProfile(authentication.getName(), request)
        );
    }
}
