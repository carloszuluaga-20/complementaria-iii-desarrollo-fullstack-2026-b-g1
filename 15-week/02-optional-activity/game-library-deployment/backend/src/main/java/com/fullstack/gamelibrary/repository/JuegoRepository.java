
package com.fullstack.gamelibrary.repository;

import com.fullstack.gamelibrary.entity.Juego;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JuegoRepository extends JpaRepository<Juego, Long> {

    // Obtener los videojuegos de un usuario.
    List<Juego> findByUsuarioId(Long usuarioId);

    // Buscar un videojuego solamente si pertenece al usuario.
    Optional<Juego> findByIdAndUsuarioId(Long id, Long usuarioId);
}
