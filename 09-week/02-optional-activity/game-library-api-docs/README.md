# Documentación y pruebas - Game Library API

Actividad opcional de la Semana 9 de Desarrollo Fullstack.

El objetivo de esta actividad es documentar una API REST con Swagger y realizar pruebas de sus endpoints utilizando Postman.

## Proyecto utilizado

Para esta actividad se creó una API REST independiente llamada:

`game-library-swagger`

La API permite gestionar una biblioteca de videojuegos mediante diferentes endpoints REST.

## Tecnologías

- Java 17
- Spring Boot 4
- Spring Web MVC
- Spring Data JPA
- H2 Database
- Maven
- Swagger / OpenAPI
- Postman

## Swagger

La API fue documentada utilizando Swagger mediante SpringDoc OpenAPI.

Swagger UI se encuentra disponible en:

```text
http://localhost:8080/swagger-ui.html
```

También se puede consultar la especificación OpenAPI en:

```text
http://localhost:8080/v3/api-docs
```

Swagger muestra correctamente los endpoints disponibles de la API.

## Endpoints disponibles

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/juegos` | Listar todos los juegos |
| GET | `/api/juegos/{id}` | Obtener un juego por ID |
| POST | `/api/juegos` | Crear un nuevo juego |
| PUT | `/api/juegos/{id}` | Actualizar un juego |
| DELETE | `/api/juegos/{id}` | Eliminar un juego |

## Pruebas realizadas con Postman

Se realizaron tres pruebas exitosas y una prueba adicional de error.

### Prueba 1 - Crear videojuego

Método:

```text
POST
```

URL:

```text
http://localhost:8080/api/juegos
```

Body:

```json
{
  "titulo": "Minecraft",
  "genero": "Sandbox",
  "estado": "Jugando",
  "progreso": 30
}
```

Respuesta obtenida:

```json
{
  "id": 1,
  "titulo": "Minecraft",
  "genero": "Sandbox",
  "estado": "Jugando",
  "progreso": 30
}
```

Código HTTP:

```text
201 Created
```

Interpretación:

El código `201 Created` indica que el videojuego fue creado correctamente.

---

### Prueba 2 - Consultar videojuego por ID

Método:

```text
GET
```

URL:

```text
http://localhost:8080/api/juegos/1
```

Respuesta obtenida:

```json
{
  "id": 1,
  "titulo": "Minecraft",
  "genero": "Sandbox",
  "estado": "Jugando",
  "progreso": 30
}
```

Código HTTP:

```text
200 OK
```

Interpretación:

El código `200 OK` indica que la petición fue procesada correctamente y el recurso fue encontrado.

---

### Prueba 3 - Actualizar videojuego

Método:

```text
PUT
```

URL:

```text
http://localhost:8080/api/juegos/1
```

Body:

```json
{
  "titulo": "Minecraft",
  "genero": "Sandbox",
  "estado": "Completado",
  "progreso": 100
}
```

Respuesta obtenida:

```json
{
  "id": 1,
  "titulo": "Minecraft",
  "genero": "Sandbox",
  "estado": "Completado",
  "progreso": 100
}
```

Código HTTP:

```text
200 OK
```

Interpretación:

El código `200 OK` indica que el videojuego fue actualizado correctamente.

---

## Prueba de error

Se realizó una consulta utilizando un ID que no existe.

Método:

```text
GET
```

URL:

```text
http://localhost:8080/api/juegos/999
```

Respuesta obtenida:

```json
{
  "error": "Juego no encontrado con id 999"
}
```

Código HTTP:

```text
404 Not Found
```

Interpretación:

El código `404 Not Found` indica que el recurso solicitado no existe.

## Códigos HTTP obtenidos

| Código | Significado |
|---|---|
| 200 OK | La solicitud fue procesada correctamente |
| 201 Created | El recurso fue creado correctamente |
| 404 Not Found | El recurso solicitado no fue encontrado |

## Evidencias realizadas

Durante la actividad se verificó:

- Swagger funcionando correctamente.
- POST con respuesta `201 Created`.
- GET con respuesta `200 OK`.
- PUT con respuesta `200 OK`.
- GET de recurso inexistente con respuesta `404 Not Found`.

## Conclusión

La API fue documentada correctamente utilizando Swagger.

También se probaron diferentes endpoints mediante Postman y se verificaron tanto respuestas exitosas como un caso de error.

Esto permitió comprobar el funcionamiento de los principales códigos HTTP utilizados por la API.

