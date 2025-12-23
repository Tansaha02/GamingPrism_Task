JOB PORTAL BACKEND
SETUP & TESTING GUIDE
====================

This document provides a clear and simple guide to understand, run, and test
the Job Portal Backend application. It is written for developers, reviewers,
and interviewers to easily follow the complete API flow.

--------------------------------------------------
1. PROJECT OVERVIEW
--------------------------------------------------
This project is a Spring Boot–based backend application for a Job Portal system.

The application supports two types of users:
1. Candidate
2. Recruiter

Key functionalities include:
- Candidate and Recruiter registration
- Secure login using JWT authentication
- Role-based access control
- Recruiters can post and manage jobs
- Candidates can search and apply for jobs
- Job search supports pagination and skill-based filtering
- H2 in-memory database for quick testing and demos

--------------------------------------------------
2. TECHNOLOGY STACK
--------------------------------------------------
- Java 17
- Spring Boot 3
- Spring Security with JWT
- Spring Data JPA
- H2 In-Memory Database
- Maven

--------------------------------------------------
3. HOW TO RUN THE APPLICATION
--------------------------------------------------

Step 1:
Open the project in IntelliJ IDEA or Eclipse.

Step 2:
Ensure Java 17 is installed and configured.

Step 3:
Run the main application class:
JobPortalBackendApplication.java

Step 4:
Once started successfully, the application will be available at:
http://localhost:8080

--------------------------------------------------
4. DATABASE ACCESS (H2 CONSOLE)
--------------------------------------------------

H2 Console URL:
http://localhost:8080/h2-console

JDBC URL:
jdbc:h2:mem:testdb

Username:
sa

Password:
(leave empty)

Note:
The database is in-memory and resets every time the application restarts.

--------------------------------------------------
5. SAMPLE LOGIN CREDENTIALS
--------------------------------------------------

Candidate Account:
Email    : candidate1@test.com
Password : Candidate@123

Recruiter Account:
Email    : recruiter1@test.com
Password : Recruiter@123

--------------------------------------------------
6. API TESTING FLOW (IMPORTANT)
--------------------------------------------------

Always test APIs in the sequence mentioned below.

---------------------------------
A. AUTHENTICATION APIs
---------------------------------

1. Register Candidate
POST /api/auth/register/candidate

2. Register Recruiter
POST /api/auth/register/recruiter

3. Login (Candidate or Recruiter)
POST /api/auth/login

Response:
A JWT token will be returned.

Important:
Use this token for all secured APIs.

Header format:
Authorization: Bearer <JWT_TOKEN>

---------------------------------
B. RECRUITER APIs (JWT REQUIRED)
---------------------------------

1. Post a Job
POST /api/jobs
(Role: RECRUITER)

2. View Jobs Posted by Recruiter
GET /api/jobs/mine

3. View Applicants for a Job
GET /api/jobs/applications/{jobId}

---------------------------------
C. CANDIDATE APIs (JWT REQUIRED)
---------------------------------

1. View Candidate Profile
GET /api/candidates/profile

2. Update Candidate Profile
PUT /api/candidates/profile

3. Search Jobs (Pagination + Skills)
GET /api/jobs/search?skills=Java,Spring&location=Kolkata&page=0&size=5

4. Apply for a Job
POST /api/jobs/{jobId}/apply

---------------------------------
D. PUBLIC APIs (NO AUTH REQUIRED)
---------------------------------

1. Get All Jobs
GET /api/jobs

2. Get Job Details by ID
GET /api/jobs/{id}

--------------------------------------------------
7. PAGINATION DETAILS
--------------------------------------------------

Pagination parameters:
- page : Page number (starts from 0)
- size : Number of records per page

Example:
GET /api/jobs/search?skills=Java&location=Bangalore&page=0&size=3

--------------------------------------------------
8. COMMON ERRORS & SOLUTIONS
--------------------------------------------------

403 Forbidden:
- JWT token is missing or invalid
- Role mismatch (candidate accessing recruiter APIs)

400 Bad Request:
- Required request parameters are missing

401 Unauthorized:
- Invalid email or password during login

--------------------------------------------------
9. IMPORTANT NOTES
--------------------------------------------------
- Do not re-register demo users (email already exists)
- Always login again after restarting the application
- H2 database data will be cleared on restart

--------------------------------------------------
10. FINAL REMARKS
--------------------------------------------------
This project is ideal for:
- Learning Spring Boot backend development
- Understanding JWT-based security
- Interview demonstrations
- Academic or mini-project submissions

--------------------------------------------------
END OF DOCUMENT
--------------------------------------------------
