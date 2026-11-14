
package com.fullstack.gamelibrary.controller;

import com.fullstack.gamelibrary.entity.Juego;
import com.fullstack.gamelibrary.service.JuegoService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/juegos")
public class JuegoController {

    private final JuegoService juegoService;

    public JuegoController(JuegoService juegoService) {
        this.juegoService = juegoService;
    }

    // Listar los juegos del usuario autenticado.
    @GetMapping
    public ResponseEntity<List<Juego>> obtenerTodos(
            @AuthenticationPrincipal Jwt jwt) {

        return ResponseEntity.ok(
                juegoService.obtenerTodos(jwt.getSubject())
        );
    }

    // Consultar un videojuego propio.
    @GetMapping("/{id}")
    public ResponseEntity<Juego> obtenerPorId(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt) {

        return ResponseEntity.ok(
                juegoService.obtenerPorId(id, jwt.getSubject())
        );
    }

    // Crear un videojuego para el usuario autenticado.
    @PostMapping
    public ResponseEntity<Juego> crear(
            @Valid @RequestBody Juego juego,
            @AuthenticationPrincipal Jwt jwt) {

        Juego nuevoJuego = juegoService.crear(
                juego,
                jwt.getSubject()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(nuevoJuego);
    }

    // Actualizar un videojuego propio.
    @PutMapping("/{id}")
    public ResponseEntity<Juego> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Juego juego,
            @AuthenticationPrincipal Jwt jwt) {

        return ResponseEntity.ok(
                juegoService.actualizar(
                        id,
                        juego,
                        jwt.getSubject()
                )
        );
    }

    // Eliminar un videojuego propio.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt) {

        juegoService.eliminar(id, jwt.getSubject());

        return ResponseEntity.noContent().build();
    }
}
