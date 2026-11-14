# Week 15 — Game Library: Fullstack Integration and Deployment

## Problem and solution
Game Library lets users maintain their own video game collection, track progress, and manage games securely. The Week 15 delivery extends the Week 14 secure fullstack project with a reproducible Docker Compose deployment.

## Reused technology and rationale
- **React + Vite:** responsive browser interface with fast development builds.
- **Java 17 + Spring Boot:** REST API and application validation.
- **Spring Security + JWT:** stateless login, password hashing, and protected per-user game endpoints.
- **Spring Data JPA + H2 file database:** simple persistent local database without an external server.
- **Docker Compose + Nginx:** reproducible two-service deployment; Nginx serves the frontend and forwards `/api/` to the backend, avoiding browser cross-origin issues.

## Architecture
Browser → `localhost:8085` → Nginx frontend → `/api/*` proxy → Spring Boot backend → H2 persistent Docker volume.

The frontend requests relative `/api` URLs in containers. For development, set `VITE_API_URL=http://localhost:8080/api` in `frontend/.env`.

## Prerequisites
Docker Desktop with Docker Compose enabled. Internet access on first build to download dependencies.

## Deployment — Windows PowerShell
From `15-week/02-optional-activity/game-library-deployment`:

```powershell
Copy-Item .env.example .env
$bytes = New-Object byte[] 32
[System.Security.Cryptography.RandomNumberGenerator]::Fill($bytes)
$secret = [Convert]::ToBase64String($bytes)
(Get-Content .env) -replace '^JWT_SECRET=.*', "JWT_SECRET=$secret" | Set-Content .env

docker compose up --build -d
docker compose ps
```

Open **http://localhost:8085**. Register an account, sign in, create a game, change its progress, and delete it. Data persists in the named `game_library_data` volume across normal restarts.

Check logs with `docker compose logs -f backend`. Stop with `docker compose down` (keeps data). **Do not use `docker compose down -v` unless you intentionally want to delete the H2 database.**

## End-to-end verification checklist
1. Open the web application and register a new account.
2. Log in and confirm a JWT is returned and the game list loads.
3. Create a game, update its status and progress, then refresh and verify persistence.
4. Delete a game and confirm it disappears.
5. Check an unauthenticated `GET /api/juegos` returns 401:

```powershell
curl.exe -i http://localhost:8085/api/juegos
```

6. Restart with `docker compose restart` and verify the account and games remain.

## Local development (without Docker)
Backend (PowerShell): set `JWT_SECRET` to a secure Base64 key and run `./mvnw.cmd spring-boot:run` in `backend/`. Frontend: copy `frontend/.env.example` to `frontend/.env`, then run `npm ci` and `npm run dev` in `frontend/`. Visit `http://localhost:5173`.

## Tests
Backend: `./mvnw.cmd test` in `backend/` with a valid `JWT_SECRET` configured. Frontend: `npm ci`, `npm test`, and `npm run build` in `frontend/`.

## Security and configuration
The `.env` file is intentionally excluded from Git. Never commit JWT secrets or credentials. `JWT_SECRET` is required and must decode to at least 32 bytes. The backend is not published on a host port in Docker; Nginx proxies API requests internally. For internet-facing production deployments, add HTTPS, secret management, backups, monitoring, and a production-grade database.

## Evidence (add real screenshots after testing)
Save screenshots in `evidence/` showing:
- `01-compose-running.png`: `docker compose ps` with both containers running.
- `02-login.png`: working login.
- `03-game-list.png`: game CRUD and progress.
- `04-api-protected.png`: HTTP 401 without a JWT.

**Verification status:** Docker deployment and tests must be run on the student's machine. Do not report them as passed until they have been executed.

## GitHub delivery
Place this directory directly under `15-week/02-optional-activity/`, without repeating the `15-week` directory inside it.
