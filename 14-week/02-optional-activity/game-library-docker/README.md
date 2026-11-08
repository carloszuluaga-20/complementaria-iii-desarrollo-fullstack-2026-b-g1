# Semana 14 — Contenerizar con Docker (actividad opcional)

Backend de **Game Library** (Spring Boot + JWT) empaquetado en una imagen Docker.

## Archivos clave

- `Dockerfile`: construcción en dos etapas.
  1. **build**: `maven:3.9-eclipse-temurin-17` compila el proyecto y genera el `.jar` (`-DskipTests`).
  2. **runtime**: `eclipse-temurin:17-jre` copia solo el `.jar` y lo ejecuta, con lo que la imagen final es más liviana.
- `.dockerignore`: evita enviar `target/`, `data/`, `.git/` y archivos `.env` al contexto de construcción.
- `pom.xml` y `src/`: código del backend que se compila dentro de la imagen.

El contenedor expone el puerto **8080** y la clave del JWT se recibe por la variable de entorno `JWT_SECRET` (nunca se guarda en el repositorio).

## Cómo construir y correr

Requisito: Docker Desktop abierto. Desde esta carpeta (`game-library-docker/`), en PowerShell:

```powershell
# 1. Construir la imagen
docker build -t game-library-api:1.0 .

# 2. Generar una clave JWT aleatoria de 32 bytes (Base64)
$rng = [System.Security.Cryptography.RandomNumberGenerator]::Create()
$bytes = New-Object byte[] 32
$rng.GetBytes($bytes)
$rng.Dispose()
$env:JWT_SECRET = [Convert]::ToBase64String($bytes)

# 3. Correr el contenedor (puerto 8081 de tu PC -> 8080 del contenedor)
docker run -d --name game-library-container -p 8081:8080 -e JWT_SECRET="$env:JWT_SECRET" game-library-api:1.0
```

## Verificación (la app responde desde el contenedor)

```powershell
# El contenedor está corriendo
docker ps --filter "name=game-library-container"

# Sin token debe responder 401: la API está viva y protegida
Invoke-WebRequest http://localhost:8081/api/juegos
```

Resultado esperado: `docker ps` muestra el contenedor en estado `Up` con `0.0.0.0:8081->8080/tcp`,
y la petición sin token devuelve **HTTP 401 Unauthorized**.

### Evidencia

**1. Contenedor corriendo (`docker ps`):**

![docker ps](evidencia/docker-ps.png)

**2. Respuesta 401 sin token:**

![401 sin token](evidencia/respuesta-401.png)

**3. Registro, login y consulta con token (200):**

![login y consulta](evidencia/login-y-consulta.png)

## Detener y limpiar

```powershell
docker stop game-library-container
docker rm game-library-container
```

## Notas

- La base de datos H2 vive dentro del contenedor. Sin un volumen de Docker, los datos se pierden al eliminar el contenedor.
- Esta carpeta es solo el backend. El proyecto completo (con el frontend) está en `14-week/game-library-secure/`.
