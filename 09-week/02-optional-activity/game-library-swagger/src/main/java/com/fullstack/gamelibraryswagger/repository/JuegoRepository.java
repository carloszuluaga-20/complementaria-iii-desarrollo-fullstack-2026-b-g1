package com.fullstack.gamelibraryswagger.repository;

import com.fullstack.gamelibraryswagger.entity.Juego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JuegoRepository extends JpaRepository<Juego, Long> {
}