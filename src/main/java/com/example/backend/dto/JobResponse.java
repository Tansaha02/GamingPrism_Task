package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor   // ✅ Lombok generates the required constructor
public class JobResponse {

    private Long id;
    private String title;
    private String description;
    private String requiredSkills;
    private Integer experienceRequired;
    private String location;
    private String recruiterName;
}
