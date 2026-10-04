package com.fullstack.gamelibrarycrud.controller;

import com.fullstack.gamelibrarycrud.entity.Juego;
import com.fullstack.gamelibrarycrud.service.JuegoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/juegos")
public class JuegoController {

    private final JuegoService juegoService;

    public JuegoController(JuegoService juegoService) {
        this.juegoService = juegoService;
    }

    @GetMapping
    public ResponseEntity<List<Juego>> listar() {
        return ResponseEntity.ok(juegoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Juego> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(juegoService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<Juego> crear(@RequestBody Juego juego) {
        Juego nuevoJuego = juegoService.crear(juego);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoJuego);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Juego> actualizar(
            @PathVariable Long id,
            @RequestBody Juego juego) {

        return ResponseEntity.ok(juegoService.actualizar(id, juego));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        juegoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}