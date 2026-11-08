# Semana 12 — Game Library: Integración React + Spring Boot JWT

Actividad práctica de Desarrollo Fullstack, Corte 3, Semana 12. Continuación de la Biblioteca de Videojuegos y del backend protegido con JWT de Semana 11.

## Requisitos cubiertos

- **Login y token (40 pts):** la interfaz registra o autentica por email y contraseña usando `POST /api/auth/register` y `POST /api/auth/login`. El token JWT obtenido en login se conserva en `sessionStorage` durante la sesión de la pestaña; «Cerrar sesión» lo elimina.
- **Consumo protegido (35 pts):** `GET /api/juegos` se consulta con `Authorization: Bearer <token>`; se muestran juegos y sus estados. También permite crear, editar y eliminar juegos autenticado.
- **CORS + carga/error (25 pts):** Spring Security habilita CORS para el origen `http://localhost:5173`; la UI muestra estados de «Cargando», mensajes de error de conexión/401 y estado sin juegos.

## Requisitos locales

- Java 17 y acceso a Maven Wrapper (`mvnw.cmd` en Windows)
- Node.js y npm
- Puertos **8080** (API) y **5173** (React) disponibles

## 1. Iniciar backend (PowerShell, terminal 1)

```powershell
cd backend
$bytes = New-Object byte[] 32
$rng = [System.Security.Cryptography.RandomNumberGenerator]::Create()
$rng.GetBytes($bytes)
$rng.Dispose()
$env:JWT_SECRET = [Convert]::ToBase64String($bytes)
.\mvnw.cmd spring-boot:run
```

Para Linux/macOS, exportar `JWT_SECRET` como un secreto Base64 aleatorio de 32 bytes y ejecutar `./mvnw spring-boot:run`.

**No subir nunca la clave JWT al repositorio.** Las pruebas Java incluyen una clave exclusivamente para el perfil de test.

## 2. Iniciar frontend (terminal 2)

Desde `frontend`:

```powershell
npm ci
npm run dev -- --port 5173 --strictPort
```

Abrir `http://localhost:5173`. Si el puerto está ocupado, libéralo: la configuración CORS local espera el puerto **5173**.

## 3. Verificación manual

1. En la pestaña **Registrarse**, crea una cuenta de prueba con nombre, correo y contraseña (mínimo 8 caracteres).
2. Abre **Iniciar sesión**, introduce el mismo correo y contraseña.
3. Verifica que se vea tu biblioteca de videojuegos; si está vacía, agrega un juego de ejemplo y confirma que aparezca.
4. En DevTools → Network, revisa `POST /api/auth/login` (HTTP 200) y `GET /api/juegos` (HTTP 200 con encabezado `Authorization: Bearer ...`). No compartas capturas con el token visible.
5. Simula error deteniendo el backend y pulsando actualizar biblioteca: comprueba el mensaje de conexión.
6. Para probar un 401, llama `/api/juegos` sin token; debe devolver 401. Desde sesión iniciada, debe devolver 200.
7. Cierra sesión: debe desaparecer el acceso a la biblioteca.

## 4. Pruebas

```powershell
# Dentro de backend
.\mvnw.cmd test

# Dentro de frontend
npm ci
npm run test
npm run build
npm run lint
```

## Notas de seguridad

- JWT en `sessionStorage` es una solución formativa para esta actividad. En producción conviene analizar cookies HttpOnly + SameSite y protección CSRF según arquitectura, además de HTTPS.
- `backend/data/`, `backend/target/`, `frontend/node_modules/` y `frontend/dist/` son artefactos locales y no deben versionarse.
- Esta entrega no modifica las Semanas 11 ni 14.
