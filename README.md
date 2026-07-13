# AI Code Review Assistant

Internship project (Java tech stack): Spring Boot + React full-stack app that runs static
analysis (Checkstyle, PMD, SpotBugs) and AI-powered review on submitted code.

## Day 1 — Requirement Analysis, Planning, Spring Boot Setup

### What's included in this scaffold
- `backend/` — Spring Boot 3.3.4 project (Java 17, Maven)
  - Dependencies wired in `pom.xml`: Web, Data JPA, Security, Validation, PostgreSQL driver,
    JJWT, Lombok, OpenPDF
  - Package structure: `controller / service / repository / entity / dto / security / util / config / exception`
  - `application.properties` with placeholders for DB, JWT secret, and OpenAI key
  - A `/api/health` endpoint to verify the app boots correctly
- `frontend/` — empty React folder structure (`pages / components / services / assets`) ready for `npm create vite@latest`

### Prerequisites to install before running
1. **Java 17** (JDK) — `java -version`
2. **Maven** (or use the included wrapper once you generate one) — `mvn -version`
3. **PostgreSQL** running locally, with a database created:
   ```sql
   CREATE DATABASE ai_code_review_db;
   ```
4. **Node.js + npm** (for the frontend, later)

### Running the backend
```bash
cd backend
mvn spring-boot:run
```
Then check: `http://localhost:8080/api/health` → should return `{"status":"UP", ...}`

### Before it will fully start
Update `src/main/resources/application.properties`:
- `spring.datasource.username` / `password` to match your local Postgres
- `app.jwt.secret` to a long random string
- `openai.api.key` to your actual key (only needed once AI review is implemented)

### Day 1 checklist
- [ ] Confirm Java 17, Maven, PostgreSQL are installed
- [ ] Create the `ai_code_review_db` database
- [ ] Update `application.properties` with your local DB credentials
- [ ] Run `mvn spring-boot:run` and confirm `/api/health` responds
- [ ] Initialize git repo and push initial commit
- [ ] (Optional) Scaffold frontend with `npm create vite@latest frontend -- --template react`

### Next (Day 2)
PostgreSQL integration + Spring Security authentication — `User` entity, repository, JWT filter,
`/api/auth/register` and `/api/auth/login` endpoints.

## Day 2 — PostgreSQL Integration & Spring Security

- Connected Spring Boot to PostgreSQL using Spring Data JPA
- Created `User` entity and `UserRepository`
- Implemented BCrypt password hashing
- Set up Spring Security with stateless JWT authentication (JJWT 0.12.6)
- Built `/api/auth/register` and `/api/auth/login` endpoints
- Tested both endpoints in Postman — returned 200 OK with valid JWT tokens

## Day 3 — Frontend Dashboard & Routing

- Scaffolded React frontend using Vite (JavaScript)
- Set up Tailwind CSS for styling
- Installed and configured React Router for page navigation
- Built Login page — connects to backend, stores JWT, redirects to dashboard
- Built Register page — connects to backend, redirects to login on success
- Added `ProtectedRoute` component to guard `/dashboard` from unauthenticated access
- Built basic Dashboard layout with navbar and logout functionality
- Fixed CORS issue by adding `CorsConfig.java` to allow requests from `http://localhost:5173`

## Day 4: File Upload APIs
- Implemented file upload endpoint using Spring `MultipartFile`
- Accepts single Java source file uploads via `multipart/form-data`
- Saves uploaded file temporarily to disk for processing
- Tested successfully in Postman with `.java` file uploads

## Day 5: Checkstyle Integration
- Added Checkstyle 10.20.2 as a library dependency (not Maven plugin) for runtime analysis of uploaded files
- Created `checkstyle-rules.xml` with rules covering naming conventions, unused imports, whitespace, and code size limits
- Built `CheckstyleService` to load rules and run analysis programmatically using Checkstyle's `Checker` API
- Built `POST /api/analysis/checkstyle` endpoint to accept a file upload and return violations as JSON
- Configured Spring Security to permit `/api/analysis/**` for testing
- Verified with Postman: uploading a file with intentional errors correctly returns violations (naming, unused imports, whitespace issues)

**Sample response:**
```json
{
  "fileName": "test.java",
  "violationCount": 5,
  "violations": [
    "Line 1: Using the '.*' form of import should be avoided - java.util.*.",
    "Line 3: Name 'test' must match pattern '^[A-Z][a-zA-Z0-9]*$'.",
    "Line 4: Name 'DoSomething' must match pattern '^[a-z][a-zA-Z0-9]*$'.",
    "Line 5: '=' is not followed by whitespace.",
    "Line 5: '=' is not preceded with whitespace."
  ]
}

## Day 6 - PMD and SpotBugs Integration

- Integrated PMD 7.3.0 as a static analysis tool
  - Added `PmdService` running rule sets: best practices, code style, design, error-prone, performance, security
  - Created `POST /api/pmd/test` endpoint (test-only, permitted in SecurityConfig)
  - Verified against sample code with unused variables, empty catch blocks, and generic exception handling
- Integrated SpotBugs 4.8.6 as a bytecode-level bug detector
  - Added `SpotBugsService` using `FindBugs2`, `BugCollectionBugReporter`, and `Project`
  - Resolved Java 17 module system compatibility by pointing to `jrt-fs.jar` instead of the legacy `rt.jar`
  - Fixed `IllegalStateException: Priority threshold not set` by explicitly setting `Priorities.LOW_PRIORITY`
  - Created `GET /api/spotbugs/test` endpoint (test-only, permitted in SecurityConfig)
  - Verified against a compiled class with a guaranteed null pointer dereference (correctly flagged as `NP_ALWAYS_NULL`, High priority)
- Key learning: SpotBugs requires compiled `.class` files, not `.java` source — compilation must precede analysis

## Day 7: Unified Analysis Report Endpoint

- Created `AnalysisReportDto` to combine results from Checkstyle, PMD, and SpotBugs into a single response
- Created `AnalysisReportService`, which calls all three analysis services and merges their outputs
- Created `AnalysisReportController` with `GET /api/analysis/full` endpoint, accepting `javaFilePath` and `classFilePath` query params
- Endpoint already covered under `/api/analysis/**` permitAll rule in `SecurityConfig`
- Tested successfully in Postman — returns combined JSON with `checkstyleViolations`, `pmdViolations`, and `spotbugsViolations`
- Pushed to GitHub

Day 8: Integrated Gemini API for AI-powered code review.
- Created OpenAiConfig and OpenAiService with WebClient calls to Gemini's generateContent endpoint
- POST /api/ai/review endpoint built and secured
- Verified model availability via Gemini's /models endpoint
- Endpoint correctly reaches Gemini's servers; live response testing pending due to temporary 503 (Service Unavailable) from Gemini's free-tier API

### Day 9: Persist Analysis Results to Database
- Created `Review` and `ReviewFinding` entities to store static analysis results
- Added `ReviewRepository` and `ReviewFindingRepository` for database access
- Built `ReviewService` with `createReview()` and `addFinding()` methods
- Updated `/api/analysis/full` endpoint to accept a `projectId` parameter and automatically save Checkstyle, PMD, and SpotBugs results as a `Review` with linked `ReviewFinding` records
- Verified persistence end-to-end via Postman + pgAdmin: analysis results now stored in `reviews` and `review_findings` tables instead of only being returned in the API response
