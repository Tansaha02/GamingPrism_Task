package com.example.backend.repository;

import com.example.backend.entity.Candidate;
import com.example.backend.entity.Job;
import com.example.backend.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    boolean existsByJobAndCandidate(Job job, Candidate candidate);

    List<JobApplication> findByJob(Job job);
}
