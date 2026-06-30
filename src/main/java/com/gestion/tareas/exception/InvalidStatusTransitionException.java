package com.gestion.tareas.exception;

public class InvalidStatusTransitionException extends RuntimeException {
    public InvalidStatusTransitionException(String mensaje) {
        super(mensaje);
    }
}
