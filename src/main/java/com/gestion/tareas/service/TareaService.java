package com.gestion.tareas.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
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

        String prioridad = request.getPrioridad();
        if (prioridad.equals("BAJA")) {
            nuevaTarea.setPrioridad(PrioridadTarea.BAJA);
        }
        if (prioridad.equals("MEDIA")) {
            nuevaTarea.setPrioridad(PrioridadTarea.MEDIA);
        }
        if (prioridad.equals("ALTA")) {
            nuevaTarea.setPrioridad(PrioridadTarea.ALTA);
        }
        if (prioridad.equals("URGENTE")) {
            nuevaTarea.setPrioridad(PrioridadTarea.URGENTE);
        }

        nuevaTarea.setEstado(EstadoTarea.PENDIENTE);
        Tarea tareaGuardada = tareaRepository.save(nuevaTarea);
        
        response.setSuccess(true);
        response.setMensaje("Tarea creada exitosamente");
        response.setData(mapearADto(tareaGuardada));
        return response;
    }

    public GlobalResponse<List<TareaResponseDTO>> obtenerTodas() {
        return obtenerTodas(null);
    }

    public GlobalResponse<List<TareaResponseDTO>> obtenerTodas(String ordenarPor) {
        GlobalResponse<List<TareaResponseDTO>> response = new GlobalResponse<>();
        List<TareaResponseDTO> listaDtos = new ArrayList<>();
        
        String criterio = "fechaCreacion";
        if (ordenarPor != null) {
            if (ordenarPor.trim().isEmpty() == false) {
                criterio = ordenarPor;
            }
        }
        
        List<Tarea> tareasEncontradas = tareaRepository.findByActivoTrue(Sort.by(Sort.Direction.ASC, criterio));

        for (Tarea tarea : tareasEncontradas) {
            listaDtos.add(mapearADto(tarea));
        }

        response.setSuccess(true);
        response.setMensaje("Tareas obtuvo correctamente");
        response.setData(listaDtos);
        return response;
    }

    public GlobalResponse<TareaResponseDTO> obtenerPorId(Long id) {
        GlobalResponse<TareaResponseDTO> response = new GlobalResponse<>();
        Optional<Tarea> optional = tareaRepository.findById(id);

        if (optional.isPresent() == false) {
            response.setSuccess(false);
            response.setMensaje("Tarea no encontrada");
            return response;
        }

        Tarea tarea = optional.get();
        if (tarea.getActivo() == false) {
            response.setSuccess(false);
            response.setMensaje("Tarea no encontrada");
            return response;
        }

        response.setSuccess(true);
        response.setMensaje("Tarea obtuvo correctamente");
        response.setData(mapearADto(tarea));
        return response;
    }

    public GlobalResponse<TareaResponseDTO> actualizarTarea(Long id, TareaRequestDTO request) {
        GlobalResponse<TareaResponseDTO> response = new GlobalResponse<>();
        Optional<Tarea> optional = tareaRepository.findById(id);

        if (optional.isPresent() == false) {
            response.setSuccess(false);
            response.setMensaje("Tarea no encontrada");
            return response;
        }

        Tarea tarea = optional.get();
        if (tarea.getActivo() == false) {
            response.setSuccess(false);
            response.setMensaje("Tarea no encontrada");
            return response;
        }

        tarea.setTitulo(request.getTitulo());
        tarea.setDescripcion(request.getDescripcion());

        String prioridad = request.getPrioridad();
        if (prioridad.equals("BAJA")) {
            tarea.setPrioridad(PrioridadTarea.BAJA);
        }
        if (prioridad.equals("MEDIA")) {
            tarea.setPrioridad(PrioridadTarea.MEDIA);
        }
        if (prioridad.equals("ALTA")) {
            tarea.setPrioridad(PrioridadTarea.ALTA);
        }
        if (prioridad.equals("URGENTE")) {
            tarea.setPrioridad(PrioridadTarea.URGENTE);
        }

        Tarea tareaActualizada = tareaRepository.save(tarea);
        response.setSuccess(true);
        response.setMensaje("Tarea actualizada exitosamente");
        response.setData(mapearADto(tareaActualizada));
        return response;
    }

    public GlobalResponse<TareaResponseDTO> cambiarEstado(Long id, EstadoTarea nuevoEstado) {
        GlobalResponse<TareaResponseDTO> response = new GlobalResponse<>();
        Optional<Tarea> optional = tareaRepository.findById(id);

        if (optional.isPresent() == false) {
            response.setSuccess(false);
            response.setMensaje("Tarea no encontrada");
            return response;
        }

        Tarea tarea = optional.get();
        if (tarea.getActivo() == false) {
            response.setSuccess(false);
            response.setMensaje("Tarea no encontrada");
            return response;
        }

        if (tarea.getEstado() == EstadoTarea.CANCELADA) {
            response.setSuccess(false);
            response.setMensaje("No se puede modificar una tarea CANCELADA");
            return response;
        }

        if (tarea.getEstado() == EstadoTarea.COMPLETADA) {
            if (nuevoEstado == EstadoTarea.PENDIENTE) {
                response.setSuccess(false);
                response.setMensaje("Una tarea COMPLETADA no puede retroceder de estado");
                return response;
            }
            if (nuevoEstado == EstadoTarea.EN_PROGRESO) {
                response.setSuccess(false);
                response.setMensaje("Una tarea COMPLETADA no puede retroceder de estado");
                return response;
            }
        }

        tarea.setEstado(nuevoEstado);
        tareaRepository.save(tarea);

        response.setSuccess(true);
        response.setMensaje("Estado actualizado exitosamente");
        response.setData(mapearADto(tarea));
        return response;
    }

    public GlobalResponse<TareaResponseDTO> eliminarTarea(Long id) {
        GlobalResponse<TareaResponseDTO> response = new GlobalResponse<>();
        Optional<Tarea> optional = tareaRepository.findById(id);

        if (optional.isPresent() == false) {
            response.setSuccess(false);
            response.setMensaje("Tarea no encontrada");
            return response;
        }

        Tarea tarea = optional.get();
        if (tarea.getActivo() == false) {
            response.setSuccess(false);
            response.setMensaje("La tarea ya se encuentra eliminada");
            return response;
        }

        tarea.setActivo(false);
        tareaRepository.save(tarea);

        response.setSuccess(true);
        response.setMensaje("Tarea eliminada exitosamente");
        response.setData(mapearADto(tarea));
        return response;
    }

    public GlobalResponse<List<TareaResponseDTO>> filtrarPorEstado(String estado) {
        GlobalResponse<List<TareaResponseDTO>> response = new GlobalResponse<>();
        List<TareaResponseDTO> listaDtos = new ArrayList<>();
        
        EstadoTarea estadoEnum = EstadoTarea.PENDIENTE;
        if (estado.equals("PENDIENTE")) {
            estadoEnum = EstadoTarea.PENDIENTE;
        }
        if (estado.equals("EN_PROGRESO")) {
            estadoEnum = EstadoTarea.EN_PROGRESO;
        }
        if (estado.equals("COMPLETADA")) {
            estadoEnum = EstadoTarea.COMPLETADA;
        }
        if (estado.equals("CANCELADA")) {
            estadoEnum = EstadoTarea.CANCELADA;
        }

        List<Tarea> tareas = tareaRepository.findByEstadoAndActivoTrue(estadoEnum);
        for (Tarea tarea : tareas) {
            listaDtos.add(mapearADto(tarea));
        }

        response.setSuccess(true);
        response.setMensaje("Tareas filtradas por estado correctamente");
        response.setData(listaDtos);
        return response;
    }

    public GlobalResponse<List<TareaResponseDTO>> filtrarPorPrioridad(String prioridad) {
        GlobalResponse<List<TareaResponseDTO>> response = new GlobalResponse<>();
        List<TareaResponseDTO> listaDtos = new ArrayList<>();
        
        PrioridadTarea prioridadEnum = PrioridadTarea.BAJA;
        if (prioridad.equals("BAJA")) {
            prioridadEnum = PrioridadTarea.BAJA;
        }
        if (prioridad.equals("MEDIA")) {
            prioridadEnum = PrioridadTarea.MEDIA;
        }
        if (prioridad.equals("ALTA")) {
            prioridadEnum = PrioridadTarea.ALTA;
        }
        if (prioridad.equals("URGENTE")) {
            prioridadEnum = PrioridadTarea.URGENTE;
        }

        List<Tarea> tareas = tareaRepository.findByPrioridadAndActivoTrue(prioridadEnum);
        for (Tarea tarea : tareas) {
            listaDtos.add(mapearADto(tarea));
        }

        response.setSuccess(true);
        response.setMensaje("Tareas filtradas por prioridad correctamente");
        response.setData(listaDtos);
        return response;
    }

    public GlobalResponse<List<TareaResponseDTO>> buscarPorTitulo(String q) {
        GlobalResponse<List<TareaResponseDTO>> response = new GlobalResponse<>();
        List<TareaResponseDTO> listaDtos = new ArrayList<>();
        List<Tarea> tareas = tareaRepository.findByTituloContainingIgnoreCaseAndActivoTrue(q);

        for (Tarea tarea : tareas) {
            listaDtos.add(mapearADto(tarea));
        }

        response.setSuccess(true);
        response.setMensaje("Tareas encontradas correctamente");
        response.setData(listaDtos);
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
