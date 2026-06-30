package com.gestion.tareas.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.gestion.tareas.entity.Tarea;
import com.gestion.tareas.entity.EstadoTarea;
import com.gestion.tareas.entity.PrioridadTarea;
import java.util.List;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {
    
    List<Tarea> findByActivoTrue(Sort sort);
    
    List<Tarea> findByEstadoAndActivoTrue(EstadoTarea estado);
    
    List<Tarea> findByPrioridadAndActivoTrue(PrioridadTarea prioridad);
    
    List<Tarea> findByTituloContainingIgnoreCaseAndActivoTrue(String titulo);
}
