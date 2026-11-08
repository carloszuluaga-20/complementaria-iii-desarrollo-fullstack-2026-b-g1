
package com.fullstack.gamelibrary.controller;

import com.fullstack.gamelibrary.service.AuthService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    public record RegistroRequest(
        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El correo no es valido")
        String email,

        @NotBlank(message = "La contrasena es obligatoria")
        @Size(
            min = 8,
            message = "La contrasena debe tener minimo 8 caracteres"
        )
        String password
    ) {}

    public record LoginRequest(
        @NotBlank String email,
        @NotBlank String password
    ) {}

    public record MensajeResponse(String mensaje) {}

    public record TokenResponse(
        String token,
        String tipo
    ) {}

    @PostMapping("/register")
    public ResponseEntity<MensajeResponse> registrar(
            @Valid @RequestBody RegistroRequest request) {

        authService.registrar(
            request.nombre(),
            request.email(),
            request.password()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new MensajeResponse(
                "Usuario registrado correctamente"
            ));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(
            @Valid @RequestBody LoginRequest request) {

        String token = authService.iniciarSesion(
            request.email(),
            request.password()
        );

        return ResponseEntity.ok(
            new TokenResponse(token, "Bearer")
        );
    }
}
