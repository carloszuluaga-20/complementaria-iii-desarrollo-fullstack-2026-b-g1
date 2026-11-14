
package com.fullstack.gamelibrary;

import com.fullstack.gamelibrary.entity.Usuario;
import com.fullstack.gamelibrary.repository.UsuarioRepository;
import com.fullstack.gamelibrary.service.AuthService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class JuegoSecurityTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AuthService authService;

    @Test
    void debeRechazarPeticionSinToken() throws Exception {

        mockMvc.perform(get("/api/juegos"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void debePermitirPeticionConJwtValido() throws Exception {

        String email = "test-" + UUID.randomUUID() + "@example.com";

        authService.registrar(
                "Usuario Test",
                email,
                "Prueba12345!"
        );

        String token = authService.iniciarSesion(
                email,
                "Prueba12345!"
        );

        mockMvc.perform(
                get("/api/juegos")
                    .header("Authorization", "Bearer " + token)
                    .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }
}
    