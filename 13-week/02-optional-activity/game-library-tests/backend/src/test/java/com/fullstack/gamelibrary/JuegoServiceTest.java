package com.fullstack.gamelibrary;

import com.fullstack.gamelibrary.entity.Juego;
import com.fullstack.gamelibrary.entity.Usuario;
import com.fullstack.gamelibrary.exception.ResourceNotFoundException;
import com.fullstack.gamelibrary.repository.JuegoRepository;
import com.fullstack.gamelibrary.repository.UsuarioRepository;
import com.fullstack.gamelibrary.service.JuegoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias de JuegoService.
 * Los repositorios se simulan con Mockito, asi que no se levanta
 * Spring ni la base de datos: son rapidas y no necesitan JWT_SECRET_TEST.
 */
@ExtendWith(MockitoExtension.class)
class JuegoServiceTest {

    private static final String EMAIL = "ana@example.com";

    @Mock
    private JuegoRepository juegoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private JuegoService juegoService;

    private Usuario ana;

    @BeforeEach
    void prepararUsuario() {
        ana = new Usuario("Ana", EMAIL, "hash");
        ReflectionTestUtils.setField(ana, "id", 1L);
    }

    // ---------- Camino feliz ----------

    @Test
    void crear_asignaElPropietarioDelTokenEIgnoraElIdDelCliente() {
        when(usuarioRepository.findByEmail(EMAIL)).thenReturn(Optional.of(ana));
        when(juegoRepository.save(any(Juego.class)))
                .thenAnswer(invocacion -> invocacion.getArgument(0));

        // El cliente intenta "forzar" un id que ya existe.
        Juego entrante = new Juego(99L, "Zelda", "Aventura", "Jugando", 30);

        Juego guardado = juegoService.crear(entrante, EMAIL);

        assertNull(guardado.getId(), "El id debe generarlo la base de datos");
        assertSame(ana, guardado.getUsuario(), "El dueno sale del JWT, no del cliente");
    }

    @Test
    void obtenerTodos_devuelveSoloLosJuegosDelUsuario() {
        Juego zelda = new Juego(1L, "Zelda", "Aventura", "Jugando", 30);

        when(usuarioRepository.findByEmail(EMAIL)).thenReturn(Optional.of(ana));
        when(juegoRepository.findByUsuarioId(1L)).thenReturn(List.of(zelda));

        List<Juego> resultado = juegoService.obtenerTodos(EMAIL);

        assertEquals(1, resultado.size());
        assertEquals("Zelda", resultado.get(0).getTitulo());
    }

    @Test
    void actualizar_modificaLosCamposDelJuegoPropio() {
        Juego existente = new Juego(5L, "Zelda", "Aventura", "Jugando", 30);
        Juego cambios = new Juego(null, "Zelda TOTK", "Aventura", "Completado", 100);

        when(usuarioRepository.findByEmail(EMAIL)).thenReturn(Optional.of(ana));
        when(juegoRepository.findByIdAndUsuarioId(5L, 1L)).thenReturn(Optional.of(existente));
        when(juegoRepository.save(any(Juego.class)))
                .thenAnswer(invocacion -> invocacion.getArgument(0));

        Juego resultado = juegoService.actualizar(5L, cambios, EMAIL);

        assertEquals("Zelda TOTK", resultado.getTitulo());
        assertEquals("Completado", resultado.getEstado());
        assertEquals(100, resultado.getProgreso());
    }

    // ---------- Casos de error / limite ----------

    @Test
    void obtenerPorId_conJuegoDeOtroUsuario_lanzaNoEncontrado() {
        when(usuarioRepository.findByEmail(EMAIL)).thenReturn(Optional.of(ana));
        // El repositorio filtra por dueno: un juego ajeno "no existe" para Ana.
        when(juegoRepository.findByIdAndUsuarioId(7L, 1L)).thenReturn(Optional.empty());

        ResourceNotFoundException error = assertThrows(
                ResourceNotFoundException.class,
                () -> juegoService.obtenerPorId(7L, EMAIL)
        );

        assertEquals("No se encontro el juego con id 7", error.getMessage());
    }

    @Test
    void eliminar_conJuegoAjeno_noBorraNada() {
        when(usuarioRepository.findByEmail(EMAIL)).thenReturn(Optional.of(ana));
        when(juegoRepository.findByIdAndUsuarioId(7L, 1L)).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> juegoService.eliminar(7L, EMAIL)
        );

        verify(juegoRepository, never()).delete(any(Juego.class));
    }

    @Test
    void obtenerTodos_conUsuarioInexistente_lanzaErrorSinConsultarJuegos() {
        when(usuarioRepository.findByEmail("fantasma@example.com"))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> juegoService.obtenerTodos("fantasma@example.com")
        );

        verifyNoInteractions(juegoRepository);
    }
}
