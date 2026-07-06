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
