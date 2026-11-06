# Game Library — Secure Fullstack Application

## Project Overview

Game Library is a fullstack application developed to help users manage their personal video game collections.

Users can register, log in, add video games, update their status and progress, and delete games from their libraries.

The application protects user information through JWT authentication and ownership-based authorization. Each user can only access and manage their own video games.

This project was developed as part of the Fullstack Development course.

## Technology Stack

**Backend**
- Java 17
- Spring Boot 4.0.8
- Spring Security
- JWT authentication
- Spring Data JPA
- H2 Database
- Maven
- Swagger / OpenAPI

**Frontend**
- React 19
- Vite 8
- JavaScript
- HTML and CSS
- Vitest
- React Testing Library

**Containerization**
- Docker
- Multi-stage Dockerfile

## Main Features

- User registration and login.
- JWT-based authentication.
- Password hashing with BCrypt.
- Personal video game collections.
- Create, read, update, and delete video games.
- Track game status and completion progress.
- Ownership-based authorization.
- Validation and error handling.
- Responsive user interface.

## Project Structure

```text
game-library-secure/
├── backend/
│   ├── src/
│   ├── Dockerfile
│   ├── .dockerignore
│   └── pom.xml
├── frontend/
│   ├── src/
│   ├── package.json
│   └── vitest.config.js
└── README.md
```

## Security & run

Game Library uses Spring Security and JSON Web Tokens (JWT) to authenticate users and protect private resources.

Users must register and log in before accessing their personal video game collections.

Passwords are hashed with BCrypt before being stored in the database.

Every protected request must include a valid JWT in the `Authorization: Bearer TOKEN` header.

The backend verifies game ownership before allowing users to read, update, or delete video games.

Requests without authentication are rejected with HTTP 401, while attempts to access another user's games return HTTP 404.

The JWT signing secret is provided through an environment variable and must never be committed to the repository.

CORS is configured to allow requests from the local React development application.

### Backend — Local execution

Requirements: Java 17 and a working Java build environment.

Navigate to the backend directory:

```powershell
cd backend
```

Generate a secure JWT secret in Windows PowerShell:

```powershell
$rng = [System.Security.Cryptography.RandomNumberGenerator]::Create()
$bytes = New-Object byte[] 32
$rng.GetBytes($bytes)
$rng.Dispose()
$env:JWT_SECRET = [Convert]::ToBase64String($bytes)
```

Start Spring Boot:

```powershell
.\mvnw.cmd spring-boot:run
```

The API runs at:

`http://localhost:8080`

### Frontend — Local execution

Open a separate terminal and navigate to the frontend directory:

```powershell
cd frontend
```

Install dependencies:

```powershell
npm ci
```

Start the development server:

```powershell
npm run dev
```

Open the application at:

`http://localhost:5173`

The local frontend connects to the backend on port 8080.

### Run the backend with Docker

Docker Desktop must be running.

Navigate to the backend directory and build the Docker image:

```powershell
cd backend
docker build -t game-library-api:1.0 .
```

Generate a secure JWT secret using the PowerShell commands shown above.

Start the container:

```powershell
docker run -d --name game-library-container -p 8081:8080 -e JWT_SECRET="$env:JWT_SECRET" game-library-api:1.0
```

The containerized backend runs at:

`http://localhost:8081`

Verify the container:

```powershell
docker ps --filter "name=game-library-container"
```

A request without authentication should return HTTP 401:

```powershell
Invoke-WebRequest http://localhost:8081/api/juegos
```

This Docker command runs the backend only. The frontend is configured for the local backend on port 8080 and requires an adjusted API URL to connect directly to the container on port 8081.

The container uses its own H2 database. Without a persistent Docker volume, database information may be lost when the container is removed.

## API Endpoints

| Method | Endpoint | Description | Authentication |
|---|---|---|---|
| POST | `/api/auth/register` | Register a user | Public |
| POST | `/api/auth/login` | Authenticate and receive a JWT | Public |
| GET | `/api/juegos` | List personal games | Required |
| GET | `/api/juegos/{id}` | Get an owned game | Required |
| POST | `/api/juegos` | Create a personal game | Required |
| PUT | `/api/juegos/{id}` | Update an owned game | Required |
| DELETE | `/api/juegos/{id}` | Delete an owned game | Required |

## Automated Tests

### Backend tests

The backend includes automated tests using JUnit, Spring Boot Test, MockMvc, and Spring Security Test.

The tests verify application startup, rejection of unauthenticated requests, and access with a valid JWT.

A separate in-memory H2 database is used during testing to avoid interfering with development data.

From the backend directory, generate a test JWT secret:

```powershell
$rng = [System.Security.Cryptography.RandomNumberGenerator]::Create()
$bytes = New-Object byte[] 32
$rng.GetBytes($bytes)
$rng.Dispose()
$env:JWT_SECRET_TEST = [Convert]::ToBase64String($bytes)
```

Run the backend tests:

```powershell
.\mvnw.cmd clean test
```

Verified result: **3 tests passed, 0 failures, 0 errors.**

### Frontend tests

The frontend uses Vitest and React Testing Library.

The automated tests verify that the login form is displayed, invalid credentials generate an error message, and a user's library loads after successful authentication.

From the frontend directory, run:

```powershell
npm test
```

Verified result: **3 tests passed, 0 failures.**

The frontend can also be built using:

```powershell
npm run build
```

## Security Verification

The following behaviors were verified manually:

- User registration and JWT login were successful.
- Authenticated users could create their own games.
- A second user could not read another user's game (HTTP 404).
- A second user could not modify another user's game (HTTP 404).
- A second user could not delete another user's game (HTTP 404).
- Requests without JWT authentication returned HTTP 401.
- The React frontend successfully performed create, update, and delete operations through the backend.
- The Docker container started successfully and returned HTTP 401 for an unauthenticated request.

## Notes

This project is intended for academic demonstration and local development.

The H2 database is configured for local storage. Production deployment would require additional measures such as database migrations, persistent storage, HTTPS, secure secret management, and stricter deployment configuration.
