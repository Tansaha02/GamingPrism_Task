package com.example.backend.security;

import com.example.backend.entity.*;
import com.example.backend.repository.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final CandidateRepository candidateRepository;
    private final RecruiterRepository recruiterRepository;

    public CustomUserDetailsService(CandidateRepository candidateRepository,
                                    RecruiterRepository recruiterRepository) {
        this.candidateRepository = candidateRepository;
        this.recruiterRepository = recruiterRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Candidate candidate = candidateRepository.findByEmail(email).orElse(null);
        if (candidate != null) {
            return new CustomUserDetails(
                    candidate.getEmail(),
                    candidate.getPassword(),
                    candidate.getRole()
            );
        }

        Recruiter recruiter = recruiterRepository.findByEmail(email).orElse(null);
        if (recruiter != null) {
            return new CustomUserDetails(
                    recruiter.getEmail(),
                    recruiter.getPassword(),
                    recruiter.getRole()
            );
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}
