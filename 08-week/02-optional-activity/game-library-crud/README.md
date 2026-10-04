# CRUD REST - Game Library

Actividad opcional de la Semana 8 de Desarrollo Fullstack.

El proyecto consiste en una API REST desarrollada con Spring Boot para gestionar una biblioteca de videojuegos mediante operaciones CRUD.

## Tecnologías

* Java 17
* Spring Boot 4
* Spring Web MVC
* Spring Data JPA
* H2 Database
* Maven

## Arquitectura por capas

El proyecto utiliza las siguientes capas:

```text id="x4a7sp"
src/main/java/com/fullstack/gamelibrarycrud
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
└── GameLibraryCrudApplication.java
```

## Entity

La clase `Juego` representa los datos almacenados en la base de datos.

**Atributos:**

* `id`
* `titulo`
* `genero`
* `estado`
* `progreso`

## Repository

`JuegoRepository` extiende `JpaRepository<Juego, Long>` y permite realizar operaciones de persistencia sobre la entidad `Juego`.

## Service

`JuegoService` contiene la lógica de negocio del CRUD.

Se encarga de:

* Listar juegos.
* Obtener un juego por ID.
* Crear juegos.
* Actualizar juegos.
* Eliminar juegos.
* Verificar que un juego exista antes de consultarlo, actualizarlo o eliminarlo.

## Controller

`JuegoController` expone los endpoints REST de la aplicación.

## Endpoints

| Método | Endpoint           | Acción                  |
| ------ | ------------------ | ----------------------- |
| GET    | `/api/juegos`      | Listar todos los juegos |
| GET    | `/api/juegos/{id}` | Obtener un juego por ID |
| POST   | `/api/juegos`      | Crear un nuevo juego    |
| PUT    | `/api/juegos/{id}` | Actualizar un juego     |
| DELETE | `/api/juegos/{id}` | Eliminar un juego       |

## Ejemplo de creación

**Petición:**

```text id="wzqg5k"
POST /api/juegos
```

**Body:**

```json id="xj7k6v"
{
  "titulo": "Minecraft",
  "genero": "Sandbox",
  "estado": "Jugando",
  "progreso": 30
}
```

**Respuesta:**

```json id="e1q4wp"
{
  "id": 1,
  "titulo": "Minecraft",
  "genero": "Sandbox",
  "estado": "Jugando",
  "progreso": 30
}
```

**Código HTTP:**

```text id="x3m8gc"
201 Created
```

## Ejemplo de listado

**Petición:**

```text id="7n7ycs"
GET /api/juegos
```

**Respuesta esperada:**

```json id="q2j6mf"
[
  {
    "id": 1,
    "titulo": "Minecraft",
    "genero": "Sandbox",
    "estado": "Jugando",
    "progreso": 30
  }
]
```

**Código HTTP:**

```text id="j2bd7x"
200 OK
```

## Ejemplo de consulta por ID

**Petición:**

```text id="1q4b7n"
GET /api/juegos/1
```

**Respuesta:**

```json id="5r6k9a"
{
  "id": 1,
  "titulo": "Minecraft",
  "genero": "Sandbox",
  "estado": "Jugando",
  "progreso": 30
}
```

**Código HTTP:**

```text id="3v8m2s"
200 OK
```

## Ejemplo de actualización

**Petición:**

```text id="8x3n5q"
PUT /api/juegos/1
```

**Body:**

```json id="h7w4k2"
{
  "titulo": "Minecraft",
  "genero": "Sandbox",
  "estado": "Completado",
  "progreso": 100
}
```

**Respuesta:**

```json id="n9c4fz"
{
  "id": 1,
  "titulo": "Minecraft",
  "genero": "Sandbox",
  "estado": "Completado",
  "progreso": 100
}
```

**Código HTTP:**

```text id="k5v2px"
200 OK
```

## Ejemplo de eliminación

**Petición:**

```text id="m8q1zt"
DELETE /api/juegos/1
```

**Código HTTP:**

```text id="p6r3wd"
204 No Content
```

## Manejo de errores

Si se intenta consultar un juego que no existe:

```text id="v7n2cx"
GET /api/juegos/1
```

después de haberlo eliminado, la API responde:

```text id="g4m8ks"
404 Not Found
```

La aplicación utiliza un manejador global de excepciones con `GlobalExceptionHandler`.

## Base de datos

La aplicación utiliza H2 con persistencia en archivo.

**Configuración:**

```text id="c2v9lm"
jdbc:h2:file:./data/juegosdb
```

La consola H2 está disponible en:

[http://localhost:8080/h2-console](http://localhost:8080/h2-console?utm_source=chatgpt.com)

## Ejecutar el proyecto

Abrir una terminal dentro de:

```text id="z8q3hf"
08-week/02-optional-activity/game-library-crud
```

Ejecutar:

```bash id="s6m2qa"
mvn spring-boot:run
```

La aplicación quedará disponible en:

[http://localhost:8080](http://localhost:8080?utm_source=chatgpt.com)

## Pruebas realizadas

Se probaron correctamente las siguientes operaciones:

* Crear un juego con `POST`.
* Listar juegos con `GET`.
* Obtener un juego por ID con `GET`.
* Actualizar un juego con `PUT`.
* Eliminar un juego con `DELETE`.
* Verificar respuesta `404 Not Found` al consultar un juego eliminado.
