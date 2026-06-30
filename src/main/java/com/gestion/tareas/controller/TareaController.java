package com.gestion.tareas.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.gestion.tareas.dto.GlobalResponse;
import com.gestion.tareas.dto.TareaRequestDTO;
import com.gestion.tareas.dto.TareaResponseDTO;
import com.gestion.tareas.entity.EstadoTarea;
import com.gestion.tareas.service.TareaService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tareas")
@RequiredArgsConstructor
public class TareaController {

    private final TareaService tareaService;

    @PostMapping
    public ResponseEntity<GlobalResponse<TareaResponseDTO>> crearTarea(@RequestBody TareaRequestDTO request) {
        GlobalResponse<TareaResponseDTO> res = tareaService.crearTarea(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @GetMapping
    public ResponseEntity<GlobalResponse<List<TareaResponseDTO>>> obtenerTodas(@RequestParam(required = false) String ordenarPor) {
        GlobalResponse<List<TareaResponseDTO>> res = tareaService.obtenerTodas(ordenarPor);
        return evaluarRespuesta(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GlobalResponse<TareaResponseDTO>> obtenerPorId(@PathVariable Long id) {
        GlobalResponse<TareaResponseDTO> res = tareaService.obtenerPorId(id);
        return evaluarRespuesta(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GlobalResponse<TareaResponseDTO>> actualizarTarea(@PathVariable Long id, @RequestBody TareaRequestDTO request) {
        GlobalResponse<TareaResponseDTO> res = tareaService.actualizarTarea(id, request);
        return evaluarRespuesta(res);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<GlobalResponse<TareaResponseDTO>> cambiarEstado(@PathVariable Long id, @RequestParam String nuevoEstado) {
        EstadoTarea estadoEnum = EstadoTarea.PENDIENTE;
        if (nuevoEstado.equals("PENDIENTE")) {
            estadoEnum = EstadoTarea.PENDIENTE;
        }
        if (nuevoEstado.equals("EN_PROGRESO")) {
            estadoEnum = EstadoTarea.EN_PROGRESO;
        }
        if (nuevoEstado.equals("COMPLETADA")) {
            estadoEnum = EstadoTarea.COMPLETADA;
        }
        if (nuevoEstado.equals("CANCELADA")) {
            estadoEnum = EstadoTarea.CANCELADA;
        }
        GlobalResponse<TareaResponseDTO> res = tareaService.cambiarEstado(id, estadoEnum);
        return evaluarRespuesta(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GlobalResponse<TareaResponseDTO>> eliminarTarea(@PathVariable Long id) {
        GlobalResponse<TareaResponseDTO> res = tareaService.eliminarTarea(id);
        return evaluarRespuesta(res);
    }

    @GetMapping("/filtrar-estado")
    public ResponseEntity<GlobalResponse<List<TareaResponseDTO>>> filtrarPorEstado(@RequestParam String estado) {
        GlobalResponse<List<TareaResponseDTO>> res = tareaService.filtrarPorEstado(estado);
        return evaluarRespuesta(res);
    }

    @GetMapping("/filtrar-prioridad")
    public ResponseEntity<GlobalResponse<List<TareaResponseDTO>>> filtrarPorPrioridad(@RequestParam String prioridad) {
        GlobalResponse<List<TareaResponseDTO>> res = tareaService.filtrarPorPrioridad(prioridad);
        return evaluarRespuesta(res);
    }

    @GetMapping("/buscar")
    public ResponseEntity<GlobalResponse<List<TareaResponseDTO>>> buscarPorTitulo(@RequestParam String q) {
        GlobalResponse<List<TareaResponseDTO>> res = tareaService.buscarPorTitulo(q);
        return evaluarRespuesta(res);
    }

    private <T> ResponseEntity<GlobalResponse<T>> evaluarRespuesta(GlobalResponse<T> res) {
        if (res.isSuccess() == false) {
            String msg = res.getMensaje();
            if (msg.equals("Tarea no encontrada")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
            }
            if (msg.equals("La tarea ya se encuentra eliminada")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
            }
            if (msg.equals("No se puede modificar una tarea CANCELADA")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(res);
            }
            if (msg.equals("Una tarea COMPLETADA no puede retroceder de estado")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(res);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        }
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
