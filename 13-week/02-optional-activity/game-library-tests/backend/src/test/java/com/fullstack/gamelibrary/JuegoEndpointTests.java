package com.fullstack.gamelibrary;

import com.fullstack.gamelibrary.service.AuthService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Pruebas de integracion de los endpoints /api/juegos.
 * Usan Spring completo con H2 en memoria (igual que JuegoSecurityTests),
 * por lo que requieren la variable de entorno JWT_SECRET_TEST.
 */
@SpringBootTest
@AutoConfigureMockMvc
class JuegoEndpointTests {

    private static final String PASSWORD = "Prueba12345!";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthService authService;

    // Registra un usuario nuevo y devuelve su JWT.
    private String crearUsuarioYObtenerToken() {
        String email = "test-" + UUID.randomUUID() + "@example.com";
        authService.registrar("Usuario Test", email, PASSWORD);
        return authService.iniciarSesion(email, PASSWORD);
    }

    private String juegoJson(String titulo, int progreso) {
        return """
                {"titulo": "%s", "genero": "Aventura", "estado": "Jugando", "progreso": %d}
                """.formatted(titulo, progreso);
    }

    // ---------- Camino feliz ----------

    @Test
    void crearJuego_conDatosValidos_devuelve201() throws Exception {
        String token = crearUsuarioYObtenerToken();

        mockMvc.perform(
                post("/api/juegos")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(juegoJson("Zelda", 40))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value("Zelda"))
                .andExpect(jsonPath("$.progreso").value(40));
    }

    // ---------- Casos de error / limite ----------

    @Test
    void crearJuego_conProgresoMayorA100_devuelve400ConElCampoInvalido() throws Exception {
        String token = crearUsuarioYObtenerToken();

        mockMvc.perform(
                post("/api/juegos")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(juegoJson("Zelda", 101))
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.progreso")
                        .value("El progreso no puede ser mayor que 100"));
    }

    @Test
    void obtenerJuego_deOtroUsuario_devuelve404() throws Exception {
        String tokenAna = crearUsuarioYObtenerToken();
        String tokenLuis = crearUsuarioYObtenerToken();

        // Ana crea un juego.
        MvcResult creado = mockMvc.perform(
                post("/api/juegos")
                        .header("Authorization", "Bearer " + tokenAna)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(juegoJson("Secreto de Ana", 10))
        ).andExpect(status().isCreated()).andReturn();

        String cuerpo = creado.getResponse().getContentAsString();
        String id = cuerpo.replaceAll(".*\"id\"\\s*:\\s*(\\d+).*", "$1");

        // Luis intenta leerlo: para el, ese juego no existe.
        mockMvc.perform(
                get("/api/juegos/" + id)
                        .header("Authorization", "Bearer " + tokenLuis)
        ).andExpect(status().isNotFound());
    }
}
