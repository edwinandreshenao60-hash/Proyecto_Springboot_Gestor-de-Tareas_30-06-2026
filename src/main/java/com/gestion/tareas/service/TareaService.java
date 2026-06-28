package com.gestion.tareas.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.gestion.tareas.dto.GlobalResponse;
import com.gestion.tareas.dto.TareaRequestDTO;
import com.gestion.tareas.dto.TareaResponseDTO;
import com.gestion.tareas.entity.EstadoTarea;
import com.gestion.tareas.entity.PrioridadTarea;
import com.gestion.tareas.entity.Tarea;
import com.gestion.tareas.repository.TareaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TareaService {

    private final TareaRepository tareaRepository;

    public GlobalResponse<TareaResponseDTO> crearTarea(TareaRequestDTO request) {
        GlobalResponse<TareaResponseDTO> response = new GlobalResponse<>();

        Tarea nuevaTarea = new Tarea();
        nuevaTarea.setTitulo(request.getTitulo());
        nuevaTarea.setDescripcion(request.getDescripcion());
        
        // Conversión segura de String a Enum mapeando la prioridad recibida
        nuevaTarea.setPrioridad(PrioridadTarea.valueOf(request.getPrioridad().toUpperCase()));
        nuevaTarea.setEstado(EstadoTarea.PENDIENTE);

        Tarea tareaGuardada = tareaRepository.save(nuevaTarea);

        TareaResponseDTO dto = mapearADto(tareaGuardada);

        response.setSuccess(true);
        response.setMensaje("Tarea creada exitosamente");
        response.setData(dto);

        return response;
    }

    public GlobalResponse<List<TareaResponseDTO>> obtenerTodas() {
        GlobalResponse<List<TareaResponseDTO>> response = new GlobalResponse<>();
        List<TareaResponseDTO> listaDtos = new ArrayList<>();
        
        // Se utiliza el método personalizado para traer únicamente los registros activos
        List<Tarea> tareasEncontradas = tareaRepository.findByActivoTrue();

        for (Tarea tarea : tareasEncontradas) {
            listaDtos.add(mapearADto(tarea));
        }

        response.setSuccess(true);
        response.setMensaje("Tareas obtenidas correctamente");
        response.setData(listaDtos);

        return response;
    }

    public GlobalResponse<TareaResponseDTO> cambiarEstado(Long id, EstadoTarea nuevoEstado) {
        GlobalResponse<TareaResponseDTO> response = new GlobalResponse<>();
        Optional<Tarea> tareaEncontrada = tareaRepository.findById(id);

        if (tareaEncontrada.isEmpty() || !tareaEncontrada.get().getActivo()) {
            response.setSuccess(false);
            response.setMensaje("Tarea no encontrada");
            return response;
        }

        Tarea tarea = tareaEncontrada.get();

        if (tarea.getEstado() == EstadoTarea.CANCELADA) {
            response.setSuccess(false);
            response.setMensaje("No se puede modificar una tarea CANCELADA");
            return response;
        }

        if (tarea.getEstado() == EstadoTarea.COMPLETADA && 
           (nuevoEstado == EstadoTarea.PENDIENTE || nuevoEstado == EstadoTarea.EN_PROGRESO)) {
            response.setSuccess(false);
            response.setMensaje("Una tarea COMPLETADA no puede retroceder de estado");
            return response;
        }

        tarea.setEstado(nuevoEstado);
        tareaRepository.save(tarea);

        response.setSuccess(true);
        response.setMensaje("Estado actualizado exitosamente");
        response.setData(mapearADto(tarea));

        return response;
    }

    private TareaResponseDTO mapearADto(Tarea tarea) {
        TareaResponseDTO dto = new TareaResponseDTO();
        dto.setId(tarea.getId());
        dto.setTitulo(tarea.getTitulo());
        dto.setDescripcion(tarea.getDescripcion());
        dto.setEstado(tarea.getEstado().name());
        dto.setPrioridad(tarea.getPrioridad().name());
        dto.setFechaCreacion(tarea.getFechaCreacion());
        dto.setFechaActualizacion(tarea.getFechaActualizacion());
        return dto;
    }
}
