create database if not exists tareas_db;
use tareas_db;

delete from tarea;

insert into tarea (titulo, descripcion, activo, estado, prioridad, fecha_creacion, fecha_actualizacion) values
('Tarea 1', 'Configurar entorno', 1, 'PENDIENTE', 'BAJA', '2026-06-25 08:00:00', '2026-06-25 08:00:00'),
('Tarea 2', 'Definir endpoints', 1, 'EN_PROGRESO', 'MEDIA', '2026-06-25 09:30:00', '2026-06-25 10:00:00'),
('Tarea 3', 'Crear DTOs', 1, 'COMPLETADA', 'ALTA', '2026-06-26 10:15:00', '2026-06-26 14:00:00'),
('Tarea 4', 'Implementar Service', 1, 'PENDIENTE', 'URGENTE', '2026-06-26 14:00:00', '2026-06-26 14:00:00'),
('Tarea 5', 'Conectar Controller', 1, 'EN_PROGRESO', 'MEDIA', '2026-06-27 09:00:00', '2026-06-27 11:00:00'),
('Tarea 6', 'Manejo de Excepciones', 1, 'PENDIENTE', 'ALTA', '2026-06-27 11:20:00', '2026-06-27 11:20:00'),
('Tarea 7', 'Revisar logs', 1, 'PENDIENTE', 'BAJA', '2026-06-28 10:00:00', '2026-06-28 10:00:00'),
('Tarea 8', 'Documentar API', 1, 'EN_PROGRESO', 'MEDIA', '2026-06-28 15:45:00', '2026-06-28 16:30:00'),
('Tarea 9', 'Pruebas unitarias', 1, 'CANCELADA', 'ALTA', '2026-06-29 09:00:00', '2026-06-29 10:00:00'),
('Tarea 10', 'Despliegue final', 1, 'PENDIENTE', 'URGENTE', '2026-06-29 11:00:00', '2026-06-29 11:00:00');
