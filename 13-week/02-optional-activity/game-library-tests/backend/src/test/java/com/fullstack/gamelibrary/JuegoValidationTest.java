package com.fullstack.gamelibrary;

import com.fullstack.gamelibrary.entity.Juego;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas de los valores limite de la entidad Juego (Bean Validation).
 * No necesitan Spring: solo el validador de Jakarta.
 */
class JuegoValidationTest {

    private static ValidatorFactory fabrica;
    private static Validator validador;

    @BeforeAll
    static void iniciarValidador() {
        fabrica = Validation.buildDefaultValidatorFactory();
        validador = fabrica.getValidator();
    }

    @AfterAll
    static void cerrarValidador() {
        fabrica.close();
    }

    private Juego juegoConProgreso(int progreso) {
        return new Juego(null, "Zelda", "Aventura", "Jugando", progreso);
    }

    // Limites validos: 0 y 100 estan dentro del rango.
    @ParameterizedTest
    @ValueSource(ints = {0, 50, 100})
    void progresoDentroDelRango_esValido(int progreso) {
        assertTrue(validador.validate(juegoConProgreso(progreso)).isEmpty());
    }

    // Limites invalidos: justo por debajo de 0 y justo por encima de 100.
    @ParameterizedTest
    @ValueSource(ints = {-1, 101})
    void progresoFueraDelRango_esInvalido(int progreso) {
        Set<ConstraintViolation<Juego>> errores =
                validador.validate(juegoConProgreso(progreso));

        assertEquals(1, errores.size());
        assertEquals("progreso", errores.iterator().next().getPropertyPath().toString());
    }

    @Test
    void tituloVacio_esInvalido() {
        Juego juego = new Juego(null, "   ", "Aventura", "Jugando", 10);

        Set<ConstraintViolation<Juego>> errores = validador.validate(juego);

        assertEquals(1, errores.size());
        assertEquals("El titulo es obligatorio", errores.iterator().next().getMessage());
    }

    @Test
    void tituloDe121Caracteres_superaElLimite() {
        Juego juego = new Juego(null, "x".repeat(121), "Aventura", "Jugando", 10);

        assertEquals(1, validador.validate(juego).size());
    }

    @Test
    void tituloDe120Caracteres_esValido() {
        Juego juego = new Juego(null, "x".repeat(120), "Aventura", "Jugando", 10);

        assertTrue(validador.validate(juego).isEmpty());
    }
}
