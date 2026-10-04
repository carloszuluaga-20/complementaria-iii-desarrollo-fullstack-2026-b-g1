package repository;

import entity.Juego;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JuegoRepository extends JpaRepository<Juego, Long> {

    List<Juego> findByGenero(String genero);
}