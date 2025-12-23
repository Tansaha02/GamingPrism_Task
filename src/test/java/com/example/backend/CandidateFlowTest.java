package com.example.backend;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CandidateFlowTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String getCandidateToken() throws Exception {
        String loginBody = """
        {
          "email": "test_candidate@test.com",
          "password": "pass123"
        }
        """;

        String response = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginBody))
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readTree(response).get("token").asText();
    }

    private String getRecruiterToken() throws Exception {
        String loginBody = """
        {
          "email": "test_recruiter@test.com",
          "password": "pass123"
        }
        """;

        String response = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginBody))
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readTree(response).get("token").asText();
    }

    private Long createJobAndReturnId() throws Exception {

        String recruiterToken = getRecruiterToken();

        String jobBody = """
        {
          "title": "Test Java Dev",
          "description": "Backend role",
          "requiredSkills": "Java",
          "experienceRequired": 2,
          "location": "Kolkata"
        }
        """;

        // Post job
        mockMvc.perform(post("/api/jobs")
                .header("Authorization", "Bearer " + recruiterToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jobBody))
                .andExpect(status().isOk());

        // Fetch recruiter jobs (SAFE)
        String response = mockMvc.perform(get("/api/jobs/mine")
                .header("Authorization", "Bearer " + recruiterToken))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode jobsArray = objectMapper.readTree(response);

        if (!jobsArray.isArray() || jobsArray.isEmpty()) {
            throw new RuntimeException("No jobs found for recruiter");
        }

        return jobsArray.get(0).get("id").asLong();
    }

    @Test
    void candidate_can_apply_job() throws Exception {

        Long jobId = createJobAndReturnId();
        String candidateToken = getCandidateToken();

        mockMvc.perform(post("/api/jobs/" + jobId + "/apply")
                .header("Authorization", "Bearer " + candidateToken))
                .andExpect(status().isOk());
    }
}
