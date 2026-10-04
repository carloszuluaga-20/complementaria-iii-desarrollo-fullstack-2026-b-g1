# Entity y Repository con JPA - Game Library

Actividad opcional de la Semana 7 de Desarrollo Fullstack.

El caso elegido es una biblioteca de videojuegos.

## Entity

Se creó la entidad `Juego` utilizando JPA.

La clase utiliza las anotaciones:

* `@Entity`: indica que la clase representa una entidad de base de datos.
* `@Table(name = "juegos")`: define el nombre de la tabla.
* `@Id`: identifica la llave primaria.
* `@GeneratedValue`: permite generar automáticamente el ID.

La entidad contiene los siguientes atributos:

* `id`
* `titulo`
* `genero`
* `estado`
* `progreso`

### Código principal

```java
@Entity
@Table(name = "juegos")
public class Juego {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String genero;
    private String estado;
    private Integer progreso;
}
```

## Repository

Se creó `JuegoRepository`, el cual extiende `JpaRepository`.

```java
public interface JuegoRepository extends JpaRepository<Juego, Long> {

    List<Juego> findByGenero(String genero);

}
```

Al extender `JpaRepository<Juego, Long>`, se obtienen operaciones CRUD sin necesidad de escribir consultas manuales.

## Consulta por método

Se agregó la siguiente consulta:

```java
List<Juego> findByGenero(String genero);
```

Spring Data JPA interpreta el nombre del método y genera automáticamente la consulta para buscar todos los juegos que pertenezcan a un género determinado.

**Ejemplo:**

```java
findByGenero("RPG")
```

La consulta devolvería todos los videojuegos cuyo género sea `RPG`.

## Operaciones CRUD

### Create

Para crear un nuevo videojuego se utiliza:

```java
save(juego);
```

Este método guarda una nueva entidad `Juego` en la base de datos.

### Read

Para obtener todos los videojuegos se utiliza:

```java
findAll();
```

Para buscar un videojuego por su ID se utiliza:

```java
findById(id);
```

También se puede utilizar la consulta personalizada:

```java
findByGenero(genero);
```

### Update

Para actualizar un videojuego se puede buscar primero por su ID, modificar sus atributos y luego utilizar:

```java
save(juego);
```

Si la entidad ya tiene un ID existente, JPA actualiza el registro.

### Delete

Para eliminar un videojuego se puede utilizar:

```java
deleteById(id);
```

También se puede eliminar directamente una entidad con:

```java
delete(juego);
```

## Estructura

```text
game-library-jpa
│
├── src
│   ├── entity
│   │   └── Juego.java
│   │
│   └── repository
│       └── JuegoRepository.java
│
└── README.md
```

## Resumen

La entidad `Juego` representa la tabla de videojuegos en la base de datos.

`JuegoRepository` permite realizar operaciones CRUD utilizando Spring Data JPA.

Además, se agregó la consulta por método `findByGenero`, que permite buscar videojuegos según su género.
