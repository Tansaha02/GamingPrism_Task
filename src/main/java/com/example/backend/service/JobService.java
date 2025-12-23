package com.example.backend.service;

import com.example.backend.dto.JobRequest;
import com.example.backend.dto.JobResponse;
import com.example.backend.entity.*;
import com.example.backend.exception.NoJobsFoundException;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.*;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final RecruiterRepository recruiterRepository;
    private final CandidateRepository candidateRepository;
    private final JobApplicationRepository jobApplicationRepository;

    public JobService(JobRepository jobRepository,
                      RecruiterRepository recruiterRepository,
                      CandidateRepository candidateRepository,
                      JobApplicationRepository jobApplicationRepository) {
        this.jobRepository = jobRepository;
        this.recruiterRepository = recruiterRepository;
        this.candidateRepository = candidateRepository;
        this.jobApplicationRepository = jobApplicationRepository;
    }

    // ================= POST JOB =================
    @CacheEvict(value = {"jobs", "jobSearch"}, allEntries = true)
    public void postJob(String recruiterEmail, JobRequest request) {

        Recruiter recruiter = recruiterRepository.findByEmail(recruiterEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter not found"));

        Job job = Job.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .requiredSkills(request.getRequiredSkills())
                .experienceRequired(request.getExperienceRequired())
                .location(request.getLocation())
                .recruiter(recruiter)
                .build();

        jobRepository.save(job);
    }

    // ================= GET ALL JOBS =================
    @Cacheable("jobs")
    public List<JobResponse> getAllJobs() {

        List<JobResponse> jobs = jobRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();

        if (jobs.isEmpty()) {
            throw new NoJobsFoundException("No jobs found");
        }

        return jobs;
    }

    // ================= SEARCH JOBS (PAGINATION + MULTI-SKILL) =================
    @Cacheable(value = "jobSearch", key = "#skills + '-' + #location + '-' + #page")
    public Page<JobResponse> searchJobs(
            List<String> skills,
            String location,
            int page,
            int size) {

        String skillsString = (skills == null || skills.isEmpty())
                ? null
                : String.join(",", skills).toLowerCase();

        Pageable pageable = PageRequest.of(page, size);

        Page<JobResponse> result = jobRepository
                .searchJobs(skillsString, location, pageable)
                .map(this::toResponse);

        if (result.isEmpty()) {
            throw new NoJobsFoundException("No jobs found for given criteria");
        }

        return result;
    }

    // ================= APPLY JOB =================
    public void applyJob(Long jobId, String candidateEmail) {

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        Candidate candidate = candidateRepository.findByEmail(candidateEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));

        if (jobApplicationRepository.existsByJobAndCandidate(job, candidate)) {
            throw new RuntimeException("Already applied for this job");
        }

        JobApplication application = JobApplication.builder()
                .job(job)
                .candidate(candidate)
                .appliedAt(LocalDateTime.now())
                .build();

        jobApplicationRepository.save(application);
    }

    // ================= RECRUITER JOBS =================
    public List<JobResponse> getRecruiterJobs(String recruiterEmail) {

        Recruiter recruiter = recruiterRepository.findByEmail(recruiterEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter not found"));

        List<JobResponse> jobs = jobRepository.findAll()
                .stream()
                .filter(job -> job.getRecruiter().getId().equals(recruiter.getId()))
                .map(this::toResponse)
                .toList();

        if (jobs.isEmpty()) {
            throw new NoJobsFoundException("No jobs posted by this recruiter");
        }

        return jobs;
    }

    // ================= GET JOB BY ID =================
    public JobResponse getJobById(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));
        return toResponse(job);
    }

    // ================= VIEW APPLICANTS =================
    public List<Candidate> getApplicants(Long jobId) {

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        return jobApplicationRepository.findByJob(job)
                .stream()
                .map(JobApplication::getCandidate)
                .toList();
    }

    // ================= MAPPER =================
    private JobResponse toResponse(Job job) {
        return new JobResponse(
        	    job.getId(),
        	    job.getTitle(),
        	    job.getDescription(),
        	    job.getRequiredSkills(),
        	    job.getExperienceRequired(),
        	    job.getLocation(),
        	    job.getRecruiter().getName()
        	);

    }
}
