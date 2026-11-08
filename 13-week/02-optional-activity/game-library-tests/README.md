# Semana 13 — Pruebas de la aplicación (Game Library)

Actividad formativa de Desarrollo Fullstack, Corte 3, Semana 13.
Se reutiliza la aplicación Game Library (React + Spring Boot + JWT) y se agregan
pruebas automáticas de **backend** y **frontend**, incluyendo **casos de error y límite**.

## Qué se probó

### Backend (JUnit 5 + Mockito + MockMvc)

| Archivo | Tipo | Camino feliz | Error / límite |
|---|---|---|---|
| `JuegoServiceTest` | Unitaria (service) | Crear asigna el dueño del JWT e ignora el `id` del cliente; listar; actualizar | Juego de otro usuario → `ResourceNotFoundException` y no se borra nada; usuario inexistente |
| `AuthServiceTest` | Unitaria (service) | Registro normaliza el correo y guarda la contraseña **codificada**; login devuelve token | Correo duplicado → 409; contraseña incorrecta → 401; correo no registrado → 401 |
| `JuegoValidationTest` | Unitaria (validación) | Progreso 0, 50 y 100; título de 120 caracteres | **Límites**: progreso -1 y 101; título vacío; título de 121 caracteres |
| `JuegoEndpointTests` | Integración (endpoint) | `POST /api/juegos` → 201 | Progreso 101 → 400 con el campo inválido; leer el juego de otro usuario → 404 |

Las pruebas de `JuegoServiceTest`, `AuthServiceTest` y `JuegoValidationTest` no levantan
Spring ni la base de datos, por eso son rápidas y no necesitan variables de entorno.

### Frontend (Vitest + React Testing Library)

Archivo: `frontend/src/test/Biblioteca.test.jsx` (el `App.test.jsx` de login ya existía).

- Camino feliz: estadísticas (total / jugando / completados) y cierre de sesión.
- Error / límite: biblioteca vacía, servidor caído (error de conexión), token expirado (401 cierra la sesión),
  cancelar la confirmación de eliminar (no se envía `DELETE`) y correo duplicado (409) al registrarse.

## Cómo ejecutar

### Backend

Desde `backend/` (Java 17). Solo `JuegoSecurityTests`, `JuegoEndpointTests` y `GameLibraryApiApplicationTests`
cargan Spring completo y necesitan `JWT_SECRET_TEST`:

```powershell
$rng = [System.Security.Cryptography.RandomNumberGenerator]::Create()
$bytes = New-Object byte[] 32
$rng.GetBytes($bytes)
$rng.Dispose()
$env:JWT_SECRET_TEST = [Convert]::ToBase64String($bytes)
.\mvnw.cmd clean test
```

Para correr solo las pruebas unitarias (sin variable de entorno):

```powershell
.\mvnw.cmd test -Dtest="JuegoServiceTest,AuthServiceTest,JuegoValidationTest"
```

### Frontend

Desde `frontend/`:

```powershell
npm ci
npm test
npm run lint
npm run build
```

## Resultado verificado

- Frontend: `npm test` → **10 pruebas pasan** (3 existentes + 7 nuevas), lint y build sin errores.
- Backend: ver la salida de `mvnw clean test` en tu máquina.
