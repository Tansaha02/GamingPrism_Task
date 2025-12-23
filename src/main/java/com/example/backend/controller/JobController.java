package com.example.backend.controller;

import com.example.backend.dto.JobRequest;
import com.example.backend.dto.JobResponse;
import com.example.backend.entity.Candidate;
import com.example.backend.service.JobService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    // POST JOB
    @PreAuthorize("hasRole('RECRUITER')")
    @PostMapping
    public ResponseEntity<String> postJob(
            @RequestBody JobRequest request,
            Authentication authentication) {

        jobService.postJob(authentication.getName(), request);
        return ResponseEntity.ok("Job posted successfully");
    }

    // LIST ALL JOBS
    @GetMapping
    public ResponseEntity<List<JobResponse>> getAllJobs() {
        return ResponseEntity.ok(jobService.getAllJobs());
    }

    // SEARCH JOBS (PAGINATION + MULTI-SKILL)
    @GetMapping("/search")
    public ResponseEntity<Page<JobResponse>> searchJobs(
            @RequestParam(required = false) List<String> skills,
            @RequestParam(required = false) String location,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        return ResponseEntity.ok(
            jobService.searchJobs(skills, location, page, size)
        );
    }


    // JOB DETAILS
    @GetMapping("/{id}")
    public ResponseEntity<JobResponse> getJobById(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.getJobById(id));
    }

    // APPLY JOB
    @PreAuthorize("hasRole('CANDIDATE')")
    @PostMapping("/{id}/apply")
    public ResponseEntity<String> applyJob(
            @PathVariable Long id,
            Authentication authentication) {

        jobService.applyJob(id, authentication.getName());
        return ResponseEntity.ok("Applied successfully");
    }

    // RECRUITER JOBS
    @PreAuthorize("hasRole('RECRUITER')")
    @GetMapping("/mine")
    public ResponseEntity<List<JobResponse>> myJobs(Authentication authentication) {
        return ResponseEntity.ok(
                jobService.getRecruiterJobs(authentication.getName())
        );
    }

    // VIEW APPLICANTS
    @PreAuthorize("hasRole('RECRUITER')")
    @GetMapping("/applications/{jobId}")
    public ResponseEntity<List<Candidate>> getApplicants(
            @PathVariable Long jobId) {

        return ResponseEntity.ok(jobService.getApplicants(jobId));
    }
}
