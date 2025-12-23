package com.example.backend.service;

import com.example.backend.dto.JobRequest;
import com.example.backend.entity.Job;
import com.example.backend.entity.Recruiter;
import com.example.backend.repository.CandidateRepository;
import com.example.backend.repository.JobApplicationRepository;
import com.example.backend.repository.JobRepository;
import com.example.backend.repository.RecruiterRepository;
import com.example.backend.service.JobService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobServiceTest {

    @Mock
    private JobRepository jobRepository;

    @Mock
    private RecruiterRepository recruiterRepository;

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private JobApplicationRepository jobApplicationRepository;

    @InjectMocks
    private JobService jobService;

    @Test
    void postJob_success() {

        JobRequest request = new JobRequest();
        request.setTitle("Java Dev");
        request.setDescription("Backend role");
        request.setRequiredSkills("Java");
        request.setExperienceRequired(2);
        request.setLocation("Kolkata");

        Recruiter recruiter = new Recruiter();
        recruiter.setEmail("recruiter@test.com");

        when(recruiterRepository.findByEmail("recruiter@test.com"))
                .thenReturn(Optional.of(recruiter));

        jobService.postJob("recruiter@test.com", request);

        verify(jobRepository, times(1)).save(any(Job.class));
    }
}
