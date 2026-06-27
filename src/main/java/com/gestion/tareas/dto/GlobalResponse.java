package com.gestion.tareas.dto;

import lombok.Data;

@Data
public class GlobalResponse<T> {
    private boolean success;
    private String mensaje;
    private T data;
}
