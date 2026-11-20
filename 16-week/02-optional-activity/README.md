# Week 16 — Fullstack Development Portfolio | Game Library

**Course:** Complementaria III — Desarrollo Fullstack · 2026-B  
**Student:** Complete with the name used in your GitHub profile  
**Period:** C1, C2 and C3 · **Delivery:** Week 16 (optional)

> This portfolio links the work included in the submitted repository. Links are relative to the repository root, so they work when this folder is committed to the same GitHub repository. The evidence checklist distinguishes requested screenshots from screenshots actually available.

## 1. Project overview

**Game Library** is a web application for maintaining a personal video-game collection. Users can register and sign in, create and view games, edit their genre/status/progress, and delete entries. Across the semester, the solution evolved from a React frontend into a Java Spring Boot REST API with authentication, automated tests, and containerized deployment.

### Main technologies

| Area | Technologies |
| --- | --- |
| Frontend | React, Vite, JavaScript, HTML, CSS |
| Backend | Java 17, Spring Boot, REST controllers, validation |
| Persistence | Spring Data JPA, H2 file database (Week 15) |
| Security | Spring Security, JWT, password hashing |
| Quality | Backend and frontend automated tests |
| Deployment | Docker Compose, Nginx, persistent Docker volume |

## 2. Portfolio by academic period

### C1 — Frontend fundamentals and React

| Week | Delivery | Evidence in repository |
| --- | --- | --- |
| 01 | Initial activity and course setup | [Week 01 README](../../01-week/02-optional-activity/README.md) |
| 04 | React application | [React app](../../04-week/02-optional-activity/react-app/) |
| 05 | C1 practical exam | [Practical exam README](../../05-week/02-optional-activity/c1-practical-exam/README.md) |
| 05 | Game Library frontend | [Frontend README](../../05-week/02-optional-activity/game-library-frontend/README.md) |

**Learning:** Component-based interfaces, page structure, frontend state, and client-side interaction.

### C2 — Backend, persistence and API documentation

| Week | Delivery | Evidence in repository |
| --- | --- | --- |
| 06 | Layered Spring Boot API | [API layers README](../../06-week/02-optional-activity/game-library-api-layers/README.md) |
| 07 | JPA persistence | [JPA README](../../07-week/02-optional-activity/game-library-jpa/README.md) |
| 08 | CRUD endpoints | [CRUD README](../../08-week/02-optional-activity/game-library-crud/README.md) |
| 09 | Swagger / API documentation | [Swagger README](../../09-week/02-optional-activity/game-library-swagger/README.md) |

**Learning:** Controllers, services, repositories, entity persistence, HTTP methods and API documentation.

### C3 — Authentication, integration, quality and deployment

| Week | Delivery | Evidence in repository |
| --- | --- | --- |
| 10 | Game Library API | [API README](../../10-week/02-optional-activity/game-library-api/README.md) |
| 11 | JWT authentication | [JWT README](../../11-week/02-optional-activity/game-library-jwt/README.md) |
| 12 | Frontend–backend integration | [Integration README](../../12-week/02-optional-activity/game-library-integration/README.md) |
| 13 | Automated tests | [Tests README](../../13-week/02-optional-activity/game-library-tests/README.md) |
| 14 | Secure fullstack app | [Week 14 source](../../14-week/game-library-secure/) · [Docker README](../../14-week/02-optional-activity/game-library-docker/README.md) |
| 15 | Containerized deployment | [Deployment README](../../15-week/02-optional-activity/game-library-deployment/README.md) |

**Learning:** JWT security, protected API requests, fullstack integration, automated validation, container orchestration and reproducible execution.

## 3. Final project — Game Library

- **Source repository:** [Course fork or repository — replace with your own GitHub URL](../../)
- **Final project folder:** [Week 15 — Game Library Deployment](../../15-week/02-optional-activity/game-library-deployment/)
- **Architecture:** Browser → Nginx frontend on `localhost:8085` → `/api/` reverse proxy → Spring Boot → persistent H2 database volume.
- **Public deployment URL:** Not provided in the submitted ZIP. The documented URL `http://localhost:8085` is a local address, not a publicly hosted deployment.

### Run the final project with Docker Desktop (Windows PowerShell)

From the repository root:

```powershell
cd 15-week/02-optional-activity/game-library-deployment
Copy-Item .env.example .env
$bytes = New-Object byte[] 32
[System.Security.Cryptography.RandomNumberGenerator]::Fill($bytes)
$secret = [Convert]::ToBase64String($bytes)
(Get-Content .env) -replace '^JWT_SECRET=.*', "JWT_SECRET=$secret" | Set-Content .env
docker compose up --build -d
docker compose ps
```

Then visit **http://localhost:8085**. Create an account, log in, register a game, update its status/progress and delete it. To stop the services without deleting the stored data:

```powershell
docker compose down
```

For tests, local development, security configuration and a more detailed verification checklist, see the [Week 15 README](../../15-week/02-optional-activity/game-library-deployment/README.md). The ZIP alone does not establish that the containers or tests were executed successfully on this machine.

## 4. Screenshot evidence

The submitted ZIP contains frontend asset images, but no screenshots documenting actual application execution. **Add your own screenshots** to [`evidence/`](evidence/) using the filenames below; do not use the frontend asset image as proof of a working deployment.

| Screenshot to capture | Suggested filename | What it should show |
| --- | --- | --- |
| C1 frontend | `01-c1-frontend.png` | React/Game Library frontend running |
| C2 API | `02-c2-api-swagger.png` | Swagger endpoints or successful CRUD request |
| C3 login | `03-c3-login.png` | Game Library login interface |
| C3 CRUD | `04-c3-games.png` | Game list and a created/updated game |
| Deployment | `05-c3-docker.png` | `docker compose ps` with running containers |
| Protected API | `06-c3-security.png` | 401 response without authentication |

After capturing, display them here by replacing the checklist below with the corresponding Markdown images (for example `![Game Library login](evidence/03-c3-login.png)`).

## 5. Personal reflection

Throughout this Fullstack Development course, I learned how the frontend and backend work together to deliver a complete web application. At first, my work focused on creating interfaces with React and understanding how components, forms and application state are organized. As the course progressed, I moved into Java and Spring Boot, where I learned to separate responsibilities into controllers, services and repositories, create REST endpoints and persist data with JPA. Building Game Library across multiple weeks helped me understand why this separation is useful when an application grows.

The most important challenge for me was integrating the different parts of the system. A screen is not enough if its requests do not reach the API correctly, and an API is not ready to be used if authentication, error handling and data validation are missing. Adding JWT authentication, automated tests and Docker showed me that a fullstack application involves more than writing features: it also requires thinking about security, reliability and how another person will run the project. I also learned the value of documenting setup steps and keeping configuration secrets outside version control.

My next step is to strengthen my testing and deployment skills, especially by introducing continuous integration, deploying the application to a publicly accessible environment with HTTPS, and exploring a production-oriented database such as PostgreSQL. I want to keep improving Game Library as a practical example of my growth as a fullstack developer.

## 6. Final checklist

- [x] Links to course deliverables across C1, C2 and C3
- [x] Final project included with repository-relative link and setup instructions
- [x] Reflection and next learning step
- [ ] Add genuine execution screenshots to `evidence/`
- [ ] Confirm the GitHub profile's `CONFIG` block (`FULL_NAME` and `GITHUB_USER`)
- [ ] Commit and push the changes to your personal fork
