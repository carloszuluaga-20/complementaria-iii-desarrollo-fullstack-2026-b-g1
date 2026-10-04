package com.fullstack.gamelibrary.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "juegos")
public class Juego {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El titulo es obligatorio")
    @Size(max = 120, message = "El titulo no puede superar los 120 caracteres")
    private String titulo;

    @NotBlank(message = "El genero es obligatorio")
    private String genero;

    @NotBlank(message = "El estado es obligatorio")
    private String estado;

    @NotNull(message = "El progreso es obligatorio")
    @Min(value = 0, message = "El progreso no puede ser menor que 0")
    @Max(value = 100, message = "El progreso no puede ser mayor que 100")
    private Integer progreso;

    public Juego() {
    }

    public Juego(Long id, String titulo, String genero, String estado, Integer progreso) {
        this.id = id;
        this.titulo = titulo;
        this.genero = genero;
        this.estado = estado;
        this.progreso = progreso;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getProgreso() {
        return progreso;
    }

    public void setProgreso(Integer progreso) {
        this.progreso = progreso;
    }
}