package com.gestion.tareas.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gestion.tareas.dto.GlobalResponse;
import com.gestion.tareas.dto.TareaRequestDTO;
import com.gestion.tareas.dto.TareaResponseDTO;
import com.gestion.tareas.entity.EstadoTarea;
import com.gestion.tareas.service.TareaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tareas")
@RequiredArgsConstructor
public class TareaController {

    private final TareaService tareaService;

    @PostMapping
    public ResponseEntity<GlobalResponse<TareaResponseDTO>> crearTarea(@Valid @RequestBody TareaRequestDTO request) {
        GlobalResponse<TareaResponseDTO> response = tareaService.crearTarea(request);

        if (response.isSuccess() == true) {
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<GlobalResponse<List<TareaResponseDTO>>> obtenerTodas() {
        GlobalResponse<List<TareaResponseDTO>> response = tareaService.obtenerTodas();

        if (response.isSuccess() == true) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<GlobalResponse<TareaResponseDTO>> cambiarEstado(@PathVariable Long id, @RequestParam String estado) {
        EstadoTarea nuevoEstado = null;

        if (estado.equals("PENDIENTE")) {
            nuevoEstado = EstadoTarea.PENDIENTE;
        }
        if (estado.equals("EN_PROGRESO")) {
            nuevoEstado = EstadoTarea.EN_PROGRESO;
        }
        if (estado.equals("COMPLETADA")) {
            nuevoEstado = EstadoTarea.COMPLETADA;
        }
        if (estado.equals("CANCELADA")) {
            nuevoEstado = EstadoTarea.CANCELADA;
        }

        GlobalResponse<TareaResponseDTO> response = tareaService.cambiarEstado(id, nuevoEstado);

        if (response.isSuccess() == true) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GlobalResponse<TareaResponseDTO>> eliminarTarea(@PathVariable Long id) {
        GlobalResponse<TareaResponseDTO> response = tareaService.eliminarTarea(id);

        if (response.isSuccess() == true) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
