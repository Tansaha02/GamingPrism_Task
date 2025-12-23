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
class RecruiterFlowTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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

        JsonNode json = objectMapper.readTree(response);
        return json.get("token").asText();
    }

    @Test
    void recruiter_can_post_job() throws Exception {
        String token = getRecruiterToken();

        String jobBody = """
        {
          "title": "Java Dev",
          "description": "Backend role",
          "requiredSkills": "Java",
          "experienceRequired": 2,
          "location": "Kolkata"
        }
        """;

        mockMvc.perform(post("/api/jobs")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jobBody))
                .andExpect(status().isOk());
    }

    @Test
    void recruiter_can_view_own_jobs() throws Exception {
        String token = getRecruiterToken();

        mockMvc.perform(get("/api/jobs/mine")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
}
