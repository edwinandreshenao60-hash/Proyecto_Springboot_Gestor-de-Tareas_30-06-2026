package com.gestion.tareas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.gestion.tareas.dto.GlobalResponse;

@RestControllerAdvice 
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GlobalResponse<Object>> manejarErroresValidacion(MethodArgumentNotValidException ex) {
        GlobalResponse<Object> respuesta = new GlobalResponse<>();
        respuesta.setSuccess(false);
        respuesta.setMensaje("Los datos enviados son incorrectos");
        
        String detalleError = ex.getBindingResult().getFieldError().getDefaultMessage();
        respuesta.setData(detalleError);
        
        return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GlobalResponse<Object>> manejarErroresGenerales(Exception ex) {
        GlobalResponse<Object> respuesta = new GlobalResponse<>();
        respuesta.setSuccess(false);
        respuesta.setMensaje("Ocurrio un error interno en el servidor");
        respuesta.setData(ex.getMessage());
        
        return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
