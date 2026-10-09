# Game Library API - Semana 10

Actividad opcional de Desarrollo Fullstack correspondiente a la Semana 10.

Esta entrega consolida una API REST para gestionar una biblioteca de videojuegos utilizando arquitectura por capas, persistencia con H2, documentación con Swagger y pruebas con Postman.

## Objetivo

Consolidar una API REST funcional con operaciones CRUD, documentarla con Swagger, probarla con Postman y explicar su ejecución y endpoints.

## Tecnologías

- Java 17
- Spring Boot 4
- Spring Web MVC
- Spring Data JPA
- H2 Database
- Maven
- Swagger / OpenAPI
- Postman

## Arquitectura por capas

El proyecto utiliza una estructura por capas:

```text
src/main/java/com/fullstack/gamelibraryswagger
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
└── GameLibrarySwaggerApplication.java
```

## Entidad

La entidad `Juego` representa los videojuegos almacenados en la base de datos.

Atributos principales:

- `id`
- `titulo`
- `genero`
- `estado`
- `progreso`

## Endpoints

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/juegos` | Listar todos los juegos |
| GET | `/api/juegos/{id}` | Obtener un juego por ID |
| POST | `/api/juegos` | Crear un nuevo juego |
| PUT | `/api/juegos/{id}` | Actualizar un juego |
| DELETE | `/api/juegos/{id}` | Eliminar un juego |

## Persistencia

La aplicación utiliza H2 como base de datos.

La persistencia se maneja mediante Spring Data JPA y `JpaRepository`.

## Swagger

La API se encuentra documentada con Swagger mediante SpringDoc OpenAPI.

Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

OpenAPI:

```text
http://localhost:8080/v3/api-docs
```

Desde Swagger se pueden visualizar y probar los endpoints disponibles.

## Pruebas con Postman

La API fue probada mediante Postman.

Se verificaron las operaciones principales:

- Crear un videojuego con `POST`.
- Listar videojuegos con `GET`.
- Obtener un videojuego por ID.
- Actualizar un videojuego con `PUT`.
- Eliminar un videojuego con `DELETE`.
- Consultar un recurso inexistente y obtener `404 Not Found`.

## Códigos HTTP

| Código | Significado |
|---|---|
| 200 OK | Solicitud procesada correctamente |
| 201 Created | Recurso creado correctamente |
| 204 No Content | Recurso eliminado correctamente |
| 404 Not Found | Recurso no encontrado |

## Manejo de errores

La API implementa manejo de errores mediante:

- `ResourceNotFoundException`
- `GlobalExceptionHandler`

Cuando se consulta un videojuego que no existe, la API responde con:

```text
404 Not Found
```

Ejemplo:

```json
{
  "error": "Juego no encontrado con id 999"
}
```

## Cómo ejecutar el proyecto

Abrir una terminal dentro de:

```text
10-week/02-optional-activity/game-library-api
```

Ejecutar:

```bash
mvn spring-boot:run
```

La API quedará disponible en:

```text
http://localhost:8080
```

Swagger estará disponible en:

```text
http://localhost:8080/swagger-ui.html
```

## Resultado

La API REST quedó consolidada con arquitectura por capas, operaciones CRUD, persistencia, documentación con Swagger, pruebas con Postman y manejo de errores.

Esto permite verificar el funcionamiento completo del backend antes de futuras integraciones.

