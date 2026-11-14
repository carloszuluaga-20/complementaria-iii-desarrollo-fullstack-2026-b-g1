
package com.fullstack.gamelibrary.service;

import com.fullstack.gamelibrary.entity.Usuario;
import com.fullstack.gamelibrary.repository.UsuarioRepository;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    public AuthService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtEncoder jwtEncoder) {

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
    }

    public void registrar(
            String nombre,
            String email,
            String password) {

        String emailNormalizado = normalizarEmail(email);

        if (usuarioRepository.existsByEmail(emailNormalizado)) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "El correo ya esta registrado"
            );
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre.trim());
        usuario.setEmail(emailNormalizado);

        // Nunca guardamos la contrasena en texto plano.
        usuario.setPassword(passwordEncoder.encode(password));

        usuarioRepository.save(usuario);
    }

    public String iniciarSesion(
            String email,
            String password) {

        String emailNormalizado = normalizarEmail(email);

        Usuario usuario = usuarioRepository
            .findByEmail(emailNormalizado)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Credenciales incorrectas"
            ));

        if (!passwordEncoder.matches(
                password, usuario.getPassword())) {

            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Credenciales incorrectas"
            );
        }

        return generarToken(usuario);
    }

    private String generarToken(Usuario usuario) {

        Instant ahora = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuer("game-library-api")
            .subject(usuario.getEmail())
            .issuedAt(ahora)
            .expiresAt(ahora.plus(2, ChronoUnit.HOURS))
            .claim("userId", usuario.getId())
            .build();

        JwsHeader header = JwsHeader
            .with(MacAlgorithm.HS256)
            .build();

        return jwtEncoder.encode(
            JwtEncoderParameters.from(header, claims)
        ).getTokenValue();
    }

    private String normalizarEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
