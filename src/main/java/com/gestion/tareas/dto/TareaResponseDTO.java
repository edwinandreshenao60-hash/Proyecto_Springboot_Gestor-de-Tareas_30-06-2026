package com.gestion.tareas.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class TareaResponseDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private String estado;
    private String prioridad;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}
