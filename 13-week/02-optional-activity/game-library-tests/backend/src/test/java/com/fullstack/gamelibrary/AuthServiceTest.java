package com.fullstack.gamelibrary;

import com.fullstack.gamelibrary.entity.Usuario;
import com.fullstack.gamelibrary.repository.UsuarioRepository;
import com.fullstack.gamelibrary.service.AuthService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias de AuthService (registro e inicio de sesion).
 * Todas las dependencias se simulan: no se usa base de datos ni JWT real.
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtEncoder jwtEncoder;

    @InjectMocks
    private AuthService authService;

    // ---------- Camino feliz ----------

    @Test
    void registrar_guardaEmailNormalizadoYPasswordCodificada() {
        when(usuarioRepository.existsByEmail("ana@example.com")).thenReturn(false);
        when(passwordEncoder.encode("Prueba12345!")).thenReturn("HASH");

        authService.registrar("  Ana  ", "  ANA@Example.com ", "Prueba12345!");

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());

        Usuario guardado = captor.getValue();
        assertEquals("Ana", guardado.getNombre());
        assertEquals("ana@example.com", guardado.getEmail());
        // Nunca debe guardarse la contrasena en texto plano.
        assertEquals("HASH", guardado.getPassword());
    }

    @Test
    void iniciarSesion_conCredencialesCorrectas_devuelveElToken() {
        Usuario ana = new Usuario("Ana", "ana@example.com", "HASH");
        ReflectionTestUtils.setField(ana, "id", 1L);

        Jwt jwtFalso = Jwt.withTokenValue("token-de-prueba")
                .header("alg", "HS256")
                .claim("sub", "ana@example.com")
                .build();

        when(usuarioRepository.findByEmail("ana@example.com"))
                .thenReturn(Optional.of(ana));
        when(passwordEncoder.matches("Prueba12345!", "HASH")).thenReturn(true);
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwtFalso);

        String token = authService.iniciarSesion("ANA@example.com", "Prueba12345!");

        assertEquals("token-de-prueba", token);
    }

    // ---------- Casos de error / limite ----------

    @Test
    void registrar_conCorreoDuplicado_lanzaConflictoYNoGuarda() {
        when(usuarioRepository.existsByEmail("ana@example.com")).thenReturn(true);

        ResponseStatusException error = assertThrows(
                ResponseStatusException.class,
                () -> authService.registrar("Ana", "ana@example.com", "Prueba12345!")
        );

        assertEquals(409, error.getStatusCode().value());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void iniciarSesion_conPasswordIncorrecta_lanza401SinGenerarToken() {
        Usuario ana = new Usuario("Ana", "ana@example.com", "HASH");

        when(usuarioRepository.findByEmail("ana@example.com"))
                .thenReturn(Optional.of(ana));
        when(passwordEncoder.matches("incorrecta", "HASH")).thenReturn(false);

        ResponseStatusException error = assertThrows(
                ResponseStatusException.class,
                () -> authService.iniciarSesion("ana@example.com", "incorrecta")
        );

        assertEquals(401, error.getStatusCode().value());
        verifyNoInteractions(jwtEncoder);
    }

    @Test
    void iniciarSesion_conCorreoNoRegistrado_lanza401() {
        when(usuarioRepository.findByEmail("nadie@example.com"))
                .thenReturn(Optional.empty());

        ResponseStatusException error = assertThrows(
                ResponseStatusException.class,
                () -> authService.iniciarSesion("nadie@example.com", "loquesea")
        );

        assertEquals(401, error.getStatusCode().value());
        verifyNoInteractions(passwordEncoder, jwtEncoder);
    }
}
