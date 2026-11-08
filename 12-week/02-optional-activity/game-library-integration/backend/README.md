# Semana 11 — Game Library JWT

Actividad formativa de Desarrollo Fullstack: proteger la API de Biblioteca de Videojuegos con JWT.

## Funcionalidades
- `POST /api/auth/register`: registrar usuario.
- `POST /api/auth/login`: autenticar y obtener token JWT.
- `GET /api/juegos`: endpoint protegido con `Authorization: Bearer <token>`.
- Contraseñas cifradas con BCrypt, JWT HS256 con expiración de 2 horas.
- Pruebas automatizadas de HTTP 401 (sin token) y 200 (token válido) en `JuegoSecurityTests`.

## Requisitos
Java 17 y Maven Wrapper. Definir variable `JWT_SECRET` en entorno (base64 de 32 o más bytes aleatorios). No subirla a GitHub.

### Ejecución (PowerShell)
```powershell
$bytes = New-Object byte[] 32
[System.Security.Cryptography.RandomNumberGenerator]::Fill($bytes)
$env:JWT_SECRET = [Convert]::ToBase64String($bytes)
.\mvnw.cmd spring-boot:run
```

### Pruebas automáticas (otra terminal en esta carpeta)
```powershell
.\mvnw.cmd test
```
Las pruebas usan una clave fija **solo para tests**, definida en `src/test/resources/application.properties`; nunca se usa como secreto de producción.

### Demostración manual (PowerShell, servidor ejecutándose)
```powershell
$base = 'http://localhost:8080'
$correo = 'alumno' + (Get-Random) + '@example.com'
$pass = 'Prueba12345!'
$body = @{nombre='Alumno';email=$correo;password=$pass} | ConvertTo-Json
Invoke-RestMethod -Method Post -Uri "$base/api/auth/register" -ContentType 'application/json' -Body $body
# Sin token: debe responder HTTP 401
try { Invoke-WebRequest "$base/api/juegos" -UseBasicParsing } catch { [int]$_.Exception.Response.StatusCode }
$login = @{email=$correo;password=$pass} | ConvertTo-Json
$res = Invoke-RestMethod -Method Post -Uri "$base/api/auth/login" -ContentType 'application/json' -Body $login
# Con token: debe responder HTTP 200
(Invoke-WebRequest "$base/api/juegos" -Headers @{Authorization="Bearer $($res.token)"} -UseBasicParsing).StatusCode
```

## Procedencia
Adaptación del backend `14-week/game-library-secure/backend` existente en el repositorio del estudiante para demostrar los requisitos concretos de Semana 11. No incluye `node_modules`, archivos de build ni bases de datos locales.
