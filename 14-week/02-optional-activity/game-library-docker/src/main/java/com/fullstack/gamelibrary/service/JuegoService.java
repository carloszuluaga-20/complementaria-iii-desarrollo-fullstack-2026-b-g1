
package com.fullstack.gamelibrary.service;

import com.fullstack.gamelibrary.entity.Juego;
import com.fullstack.gamelibrary.entity.Usuario;
import com.fullstack.gamelibrary.exception.ResourceNotFoundException;
import com.fullstack.gamelibrary.repository.JuegoRepository;
import com.fullstack.gamelibrary.repository.UsuarioRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JuegoService {

    private final JuegoRepository juegoRepository;
    private final UsuarioRepository usuarioRepository;

    public JuegoService(
            JuegoRepository juegoRepository,
            UsuarioRepository usuarioRepository) {

        this.juegoRepository = juegoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // Buscar al usuario autenticado mediante su correo JWT.
    private Usuario obtenerUsuario(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontro el usuario autenticado"
                ));
    }

    // Mostrar solamente los juegos del usuario autenticado.
    public List<Juego> obtenerTodos(String email) {

        Usuario usuario = obtenerUsuario(email);

        return juegoRepository.findByUsuarioId(usuario.getId());
    }

    // Consultar un juego comprobando su propietario.
    public Juego obtenerPorId(Long id, String email) {

        Usuario usuario = obtenerUsuario(email);

        return juegoRepository
                .findByIdAndUsuarioId(id, usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontro el juego con id " + id
                ));
    }

    // Crear un juego asociado al usuario autenticado.
    public Juego crear(Juego juego, String email) {

        Usuario usuario = obtenerUsuario(email);

        // El propietario se obtiene del JWT.
        // Nunca se acepta desde el frontend.
        juego.setId(null);
        juego.setUsuario(usuario);

        return juegoRepository.save(juego);
    }

    // Actualizar solamente juegos propios.
    public Juego actualizar(
            Long id,
            Juego juegoActualizado,
            String email) {

        Juego juego = obtenerPorId(id, email);

        juego.setTitulo(juegoActualizado.getTitulo());
        juego.setGenero(juegoActualizado.getGenero());
        juego.setEstado(juegoActualizado.getEstado());
        juego.setProgreso(juegoActualizado.getProgreso());

        return juegoRepository.save(juego);
    }

    // Eliminar solamente juegos propios.
    public void eliminar(Long id, String email) {

        Juego juego = obtenerPorId(id, email);

        juegoRepository.delete(juego);
    }
}
