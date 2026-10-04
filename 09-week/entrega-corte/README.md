# Game Library API

API REST desarrollada con Spring Boot para gestionar una biblioteca de videojuegos.

Este proyecto fue realizado como parte de la materia **Desarrollo Fullstack** e implementa un CRUD completo utilizando una arquitectura por capas.

## Tecnologías

- Java 17
- Spring Boot 4
- Spring Web MVC
- Spring Data JPA
- Bean Validation
- H2 Database
- Maven
- Swagger / OpenAPI
- Postman

## Estructura del proyecto

El proyecto utiliza una arquitectura por capas:

```text
src/main/java/com/fullstack/gamelibrary
│
├── controller
│   └── JuegoController.java
│
├── entity
│   └── Juego.java
│
├── exception
│   ├── GlobalExceptionHandler.java
│   └── ResourceNotFoundException.java
│
├── repository
│   └── JuegoRepository.java
│
├── service
│   └── JuegoService.java
│
└── GameLibraryApiApplication.java
```

### Capas

- `entity`: define la entidad `Juego` y sus reglas de validación.
- `repository`: permite acceder a la base de datos utilizando Spring Data JPA.
- `service`: contiene la lógica de negocio de la aplicación.
- `controller`: expone los endpoints REST.
- `exception`: maneja errores de validación y recursos no encontrados.

## Entidad Juego

El recurso principal de la API es `Juego`.

Sus atributos son:

- `id`
- `titulo`
- `genero`
- `estado`
- `progreso`

### Ejemplo

```json
{
  "id": 1,
  "titulo": "Minecraft",
  "genero": "Sandbox",
  "estado": "Jugando",
  "progreso": 30
}
```

## Base de datos

La aplicación utiliza una base de datos **H2** con persistencia en archivo mediante Spring Data JPA.

URL de la base de datos:

```text
jdbc:h2:file:./data/juegosdb
```

La información queda almacenada localmente, por lo que los registros pueden mantenerse después de reiniciar la aplicación.

La consola de H2 está disponible en:

[http://localhost:8080/h2-console](http://localhost:8080/h2-console)

## Cómo ejecutar el proyecto

### Requisitos

- Java 17
- Maven

Abrir una terminal dentro de la carpeta:

```text
09-week/entrega-corte/game-library-api
```

Ejecutar:

```bash
mvn spring-boot:run
```

La aplicación se ejecutará en:

[http://localhost:8080](http://localhost:8080)

## Swagger

La documentación de Swagger está disponible en:

[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

La documentación OpenAPI está disponible en:

[http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

Swagger permite visualizar y probar los endpoints CRUD de la API.

## API Reference

The API exposes five main REST endpoints under `/api/juegos`.

`GET /api/juegos` returns the complete list of games stored in the database.

`GET /api/juegos/{id}` returns a specific game by its identifier and responds with HTTP `404` when the game does not exist.

`POST /api/juegos` creates a new game after validating the JSON request body and returns HTTP `201` when the resource is created successfully.

`PUT /api/juegos/{id}` updates an existing game using the information provided in the request body.

`DELETE /api/juegos/{id}` removes a game from the database and returns HTTP `204` when the operation is successful.

Validation errors, such as an empty title or a progress value outside the allowed range, return HTTP `400`.

The API uses Spring Data JPA with an H2 file database to persist the game information.

## Endpoints

| Método | Endpoint | Descripción | Código exitoso |
|---|---|---|---|
| GET | `/api/juegos` | Obtener todos los juegos | `200 OK` |
| GET | `/api/juegos/{id}` | Obtener un juego por ID | `200 OK` |
| POST | `/api/juegos` | Crear un nuevo juego | `201 Created` |
| PUT | `/api/juegos/{id}` | Actualizar un juego existente | `200 OK` |
| DELETE | `/api/juegos/{id}` | Eliminar un juego | `204 No Content` |

## Ejemplo de creación de un juego

### Petición

```http
POST /api/juegos
```

### Body

```json
{
  "titulo": "Minecraft",
  "genero": "Sandbox",
  "estado": "Jugando",
  "progreso": 30
}
```

### Respuesta exitosa

```text
201 Created
```

### Ejemplo de respuesta

```json
{
  "id": 2,
  "titulo": "Minecraft",
  "genero": "Sandbox",
  "estado": "Jugando",
  "progreso": 30
}
```

## Ejemplo de actualización

### Petición

```http
PUT /api/juegos/2
```

### Body

```json
{
  "titulo": "Minecraft",
  "genero": "Sandbox",
  "estado": "Completado",
  "progreso": 100
}
```

### Respuesta exitosa

```text
200 OK
```

## Ejemplo de eliminación

### Petición

```http
DELETE /api/juegos/2
```

### Respuesta exitosa

```text
204 No Content
```

## Manejo de errores

La API implementa un manejo centralizado de errores.

Si se solicita un juego que no existe, la API responde con:

```text
404 Not Found
```

### Ejemplo

```http
GET /api/juegos/999
```

### Respuesta

```json
{
  "error": "No se encontro el juego con id 999"
}
```

Los errores de validación responden con:

```text
400 Bad Request
```

Por ejemplo, el campo `progreso` debe contener un valor entre `0` y `100`.

## Pruebas con Postman

La API fue probada utilizando Postman.

Se verificaron las siguientes operaciones:

- Obtener todos los juegos con `GET`.
- Obtener un juego por ID.
- Crear un juego con `POST`.
- Actualizar un juego con `PUT`.
- Eliminar un juego con `DELETE`.
- Probar un error `404` al consultar un juego inexistente.

URL principal utilizada en las pruebas:

[http://localhost:8080/api/juegos](http://localhost:8080/api/juegos)

## Códigos HTTP utilizados

| Código | Significado |
|---|---|
| `200 OK` | GET o PUT realizado correctamente |
| `201 Created` | Juego creado correctamente |
| `204 No Content` | Juego eliminado correctamente |
| `400 Bad Request` | Datos enviados inválidos |
| `404 Not Found` | El juego solicitado no existe |
