package com.example.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CandidateUpdateRequest {
    private String name;
    private Integer experience;
    private String skills;
    private String location;
}
