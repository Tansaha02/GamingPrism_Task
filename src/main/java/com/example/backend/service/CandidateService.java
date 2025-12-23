package com.example.backend.service;

import com.example.backend.dto.CandidateUpdateRequest;
import com.example.backend.exception.*;
import com.example.backend.entity.Candidate;
import com.example.backend.repository.CandidateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CandidateService {

    private final CandidateRepository candidateRepository;

    public CandidateService(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    // GET PROFILE
    public Candidate getProfile(String email) {
        return candidateRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));
    }

    // UPDATE PROFILE
    @Transactional
    public Candidate updateProfile(String email, CandidateUpdateRequest request) {

        Candidate candidate = candidateRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));

        // ✅ ONLY update allowed fields
        if (request.getExperience() != null) {
            candidate.setExperience(request.getExperience());
        }

        if (request.getSkills() != null) {
            candidate.setSkills(request.getSkills());
        }

        if (request.getLocation() != null) {
            candidate.setLocation(request.getLocation());
        }

        // ❌ DO NOT touch name/email/password/role

        return candidate; // ✅ Hibernate auto-flushes managed entity
    }
}
