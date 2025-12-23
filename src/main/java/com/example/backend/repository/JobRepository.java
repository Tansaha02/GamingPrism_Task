package com.example.backend.repository;

import com.example.backend.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JobRepository extends JpaRepository<Job, Long> {

    @Query("""
        SELECT j FROM Job j
        WHERE LOWER(j.location) = LOWER(:location)
        AND (
            :skills IS NULL OR
            LOWER(j.requiredSkills) LIKE %:skills%
        )
    """)
    Page<Job> searchJobs(
            @Param("skills") String skills,
            @Param("location") String location,
            Pageable pageable
    );
}
