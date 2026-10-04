package com.fullstack.gamelibrarycrud.repository;

import com.fullstack.gamelibrarycrud.entity.Juego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JuegoRepository extends JpaRepository<Juego, Long> {
}