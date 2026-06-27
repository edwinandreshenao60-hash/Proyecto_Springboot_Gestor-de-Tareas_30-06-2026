package com.gestion.tareas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TareaRequestDTO {
    
    @NotBlank(message = "El título no puede estar vacío")
    @Size(min = 3, message = "El título debe tener al menos 3 caracteres")
    private String titulo;

    private String descripcion;

    private String estado;

    @NotNull(message = "La prioridad es obligatoria")
    private String prioridad;
}
