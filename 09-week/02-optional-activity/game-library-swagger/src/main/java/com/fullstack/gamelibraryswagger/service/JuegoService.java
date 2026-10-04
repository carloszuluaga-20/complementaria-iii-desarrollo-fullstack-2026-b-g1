package com.fullstack.gamelibraryswagger.service;

import com.fullstack.gamelibraryswagger.entity.Juego;
import com.fullstack.gamelibraryswagger.exception.ResourceNotFoundException;
import com.fullstack.gamelibraryswagger.repository.JuegoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JuegoService {

    private final JuegoRepository juegoRepository;

    public JuegoService(JuegoRepository juegoRepository) {
        this.juegoRepository = juegoRepository;
    }

    public List<Juego> listar() {
        return juegoRepository.findAll();
    }

    public Juego obtenerPorId(Long id) {
        return juegoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Juego no encontrado con id " + id));
    }

    public Juego crear(Juego juego) {
        return juegoRepository.save(juego);
    }

    public Juego actualizar(Long id, Juego juegoActualizado) {
        Juego juego = obtenerPorId(id);

        juego.setTitulo(juegoActualizado.getTitulo());
        juego.setGenero(juegoActualizado.getGenero());
        juego.setEstado(juegoActualizado.getEstado());
        juego.setProgreso(juegoActualizado.getProgreso());

        return juegoRepository.save(juego);
    }

    public void eliminar(Long id) {
        Juego juego = obtenerPorId(id);
        juegoRepository.delete(juego);
    }
}