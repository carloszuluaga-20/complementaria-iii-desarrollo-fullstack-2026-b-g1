# Arquitectura en capas - Game Library API

Actividad opcional de la Semana 6 de Desarrollo Fullstack.

El caso elegido es una API REST para gestionar una biblioteca de videojuegos.

## Arquitectura en capas

La API está organizada en cuatro capas principales:

```text
Cliente / Frontend
        |
        v
+----------------------+
|      Controller      |
| JuegoController.java |
+----------------------+
        |
        v
+----------------------+
|       Service        |
|  JuegoService.java   |
+----------------------+
        |
        v
+------------------------+
|       Repository       |
|  JuegoRepository.java  |
+------------------------+
        |
        v
+-------------------+
|      Entity       |
|     Juego.java    |
+-------------------+
        |
        v
   Base de datos
```

El flujo principal de la aplicación es:

```text
Cliente
   ↓
Controller
   ↓
Service
   ↓
Repository
   ↓
Entity / Base de datos
```

## Responsabilidad de cada capa

### Controller

La capa `Controller` recibe las solicitudes HTTP realizadas por el cliente.

En este caso, `JuegoController` define los endpoints relacionados con los videojuegos.

Sus principales responsabilidades son:

* Recibir solicitudes HTTP.
* Leer parámetros y cuerpos JSON.
* Llamar a los métodos de la capa `Service`.
* Retornar respuestas HTTP al cliente.
* Utilizar códigos como `200`, `201`, `204` y `404`.

**Ejemplo:**

```text
GET /api/juegos
```

El controller recibe la petición y solicita al service la lista de videojuegos.

### Service

La capa `Service` contiene la lógica de negocio de la aplicación.

En este proyecto, `JuegoService` se encarga de ejecutar las operaciones necesarias antes de acceder a los datos.

Sus responsabilidades son:

* Obtener todos los videojuegos.
* Buscar un videojuego por ID.
* Crear nuevos videojuegos.
* Actualizar videojuegos existentes.
* Eliminar videojuegos.
* Validar que un videojuego exista antes de actualizarlo o eliminarlo.

El `Service` funciona como intermediario entre el `Controller` y el `Repository`.

### Repository

La capa `Repository` se encarga de acceder a la base de datos.

En este caso se utiliza:

```text
JuegoRepository
```

Este repository extiende `JpaRepository`, por lo que permite realizar operaciones CRUD como:

* `findAll()`
* `findById()`
* `save()`
* `delete()`

Esta capa evita colocar consultas o acceso a datos directamente en el controller.

### Entity

La capa `Entity` representa la estructura de los datos que se almacenan en la base de datos.

La entidad utilizada es:

```text
Juego
```

Sus atributos principales son:

* `id`
* `titulo`
* `genero`
* `estado`
* `progreso`

La entidad también contiene reglas de validación.

Por ejemplo:

* El título no puede estar vacío.
* El género no puede estar vacío.
* El estado no puede estar vacío.
* El progreso debe estar entre `0` y `100`.

## Endpoint de ejemplo

Un ejemplo de endpoint de la API es:

```text
GET /api/juegos/2
```

Este endpoint permite obtener un videojuego por su identificador.

## Flujo del endpoint

Cuando el cliente realiza:

```text
GET /api/juegos/2
```

La solicitud pasa por las siguientes capas:

```text
1. Cliente
   |
   | GET /api/juegos/2
   v
2. JuegoController
   |
   | obtenerPorId(2)
   v
3. JuegoService
   |
   | obtenerPorId(2)
   v
4. JuegoRepository
   |
   | findById(2)
   v
5. Base de datos
```

Después, la respuesta regresa en sentido contrario:

```text
Base de datos
   ↓
JuegoRepository
   ↓
JuegoService
   ↓
JuegoController
   ↓
Cliente
```

Si el juego existe, la API puede responder:

```json
{
  "id": 2,
  "titulo": "Minecraft",
  "genero": "Sandbox",
  "estado": "Jugando",
  "progreso": 30
}
```

Con el código HTTP:

```text
200 OK
```

Si el juego no existe, el service genera un error y la API responde:

```text
404 Not Found
```

## Resumen del flujo

```text
HTTP Request
     ↓
Controller
     ↓
Service
     ↓
Repository
     ↓
Entity / Database
     ↓
Repository
     ↓
Service
     ↓
Controller
     ↓
HTTP Response
```

Esta separación permite que cada capa tenga una responsabilidad específica y hace que la API sea más organizada, mantenible y fácil de probar.
