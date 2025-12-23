package com.example.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobRequest {
    private String title;
    private String description;
    private String requiredSkills;
    private Integer experienceRequired;
    private String location;
}
