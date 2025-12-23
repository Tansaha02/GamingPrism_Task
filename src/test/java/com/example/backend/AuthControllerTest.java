package com.example.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerRecruiter_success() throws Exception {
        String body = """
        {
          "name": "HR",
          "company": "ABC Corp",
          "email": "test_recruiter@test.com",
          "password": "pass123"
        }
        """;

        mockMvc.perform(post("/api/auth/register/recruiter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk());
    }

    @Test
    void registerCandidate_success() throws Exception {
        String body = """
        {
          "name": "Candidate",
          "email": "test_candidate@test.com",
          "password": "pass123",
          "experience": 2,
          "skills": "Java",
          "location": "Kolkata"
        }
        """;

        mockMvc.perform(post("/api/auth/register/candidate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk());
    }

    @Test
    void login_success() throws Exception {
        String body = """
        {
          "email": "test_candidate@test.com",
          "password": "pass123"
        }
        """;

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk());
    }
}
