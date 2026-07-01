# PRUEBA TÉCNICA

## Desarrollo de API REST con Spring Boot y MySQL

**Nivel:** Intermedio  
**Tecnologías que se utilizaron:** Spring Boot 4.1.0 - MySQL - JPA  
**Fecha de Entrega:** Martes 30 de Junio  

---

# 1. Descripción del proyecto

API REST para la gestión de tareas. Este proyecto demuestra la capacidad de estructurar una aplicación Spring Boot, conectarla a una base de datos MySQL y aplicar buenas prácticas de programación.

## 1.1 Contexto de negocio

La aplicación permite que los usuarios gestionen sus tareas diarias. Cada tarea tiene un estado y una prioridad, y puede ser creada, consultada, modificada y eliminada a través de endpoints REST.

---

# 2. Requisitos técnicos

## 2.1 Dependencias obligatorias

- Java 17
- Spring Boot 4.1.0
- Spring Data JPA + Hibernate
- MySQL Workbench
- Maven
- Lombok

## 2.2 Estructura del proyecto

Separación clara de responsabilidades implementada:

- `controller/` — Endpoints REST y gestión de códigos HTTP.
- `service/` — Lógica de negocio y validaciones .
- `repository/` — Acceso a datos con Spring Data JPA.
- `entity/` — Entidades JPA y mapeos relacionales.
- `dto/` — Objetos de transferencia de datos de entrada y salida.
- `exception/` — Manejo centralizado e infraestructura de errores.

---

# 3. Modelo de datos

## 3.1 Entidad Tarea

| Campo | Tipo Java | Columna MySQL | Observaciones |
|--------|------------|---------------|----------------|
| id | Long | BIGINT PK AUTO | Generado automáticamente |
| titulo | String | VARCHAR(150) | Obligatorio, mín. 3 caracteres |
| descripcion | String | TEXT | Opcional, puede ser null |
| estado | EstadoTarea | VARCHAR | Default: PENDIENTE |
| prioridad | PrioridadTarea | VARCHAR | Obligatorio |
| fechaCreacion | LocalDateTime | DATETIME | Se asigna automáticamente |
| fechaActualizacion | LocalDateTime | DATETIME | Se updates en cada cambio |

## 3.2 Reglas de negocio obligatorias implementadas

- Asignación automática de `fechaCreacion` al persistir mediante `@PrePersist`.
- Actualización automática de `fechaActualizacion` en modificaciones mediante `@PreUpdate`.
- Bloqueo de modificaciones si la tarea se encuentra en estado `CANCELADA`.
- Prohibición de retroceso de estado si la tarea se encuentra en estado `COMPLETADA`.
- Validaciones básicas de estructura sobre el atributo `titulo`.

---

# 4. Endpoints implementados

| Método | URL | Descripción |
|----------|------|-------------|
| GET | `/api/tareas` | Listar todas las tareas (Soporta parámetro opcional `ordenarPor`) |
| GET | `/api/tareas/{id}` | Obtener una tarea por ID |
| POST | `/api/tareas` | Crear una nueva tarea |
| PUT | `/api/tareas/{id}` | Actualizar una tarea completa |
| PATCH | `/api/tareas/{id}/estado` | Cambiar solo el estado (`?nuevoEstado=EN_PROGRESO`) |
| DELETE | `/api/tareas/{id}` | Eliminar una tarea (Borrado lógico) |
| GET | `/api/tareas/filtrar-estado` | Filtrar por estado (`?estado=PENDIENTE`) |
| GET | `/api/tareas/filtrar-prioridad` | Filtrar por prioridad (`?prioridad=ALTA`) |
| GET | `/api/tareas/buscar` | Buscar por coincidencia parcial de título (`?q=texto`) |

---

# 5. SOLUCIÓN DE PREGUNTAS TEÓRICAS

### 11. ¿Cuál es la diferencia entre `@RestController` y `@Controller` en Spring Boot?
La diferencia está en la respuesta que generan:
- `@Controller` está diseñado para el patrón MVC tradicional. Sus métodos devuelven una cadena de texto que Spring interpreta como el nombre de una vista (por ejemplo, un archivo HTML o una plantilla Thymeleaf) para ser renderizada en el navegador. Si se desea devolver datos directamente, se debe añadir manualmente la anotación `@ResponseBody` a cada método.
- `@RestController` es una anotación que combina `@Controller` y `@ResponseBody`. Le indica a Spring que todos los métodos del controlador serializarán automáticamente los objetos de retorno directamente en el cuerpo de la respuesta HTTP (habitualmente en formato JSON), eliminando la necesidad hacerlo con vistas HTML. Es la opción ideal para construir servicios web y APIs REST.

### 12. ¿Por qué se usan DTOs en lugar de exponer directamente la entidad JPA?
El uso de DTOs (Data Transfer Objects) responde a buenas prácticas de arquitectura, seguridad y rendimiento por las siguientes razones:
- **Seguridad y Enmascaramiento:** Evita la exposición involuntaria de campos sensibles de la base de datos (como contraseñas, tokens o IDs internos de auditoría) y protege la aplicación contra ataques de sobreasignación, asegurando que el usuario solo envíe o reciba los atributos permitidos.
- **Desacoplamiento:** Separa la capa de persistencia de la capa de presentación. Si el esquema de la base de datos cambia y la entidad JPA debe modificarse, el contrato público de la API (definido por el DTO) puede mantenerse intacto, evitando romper los clientes que consumen el servicio.
- **Optimización de Transferencia:** Permite moldear y combinar estructuras de datos según las necesidades exactas de la vista o del cliente, enviando únicamente la información requerida y reduciendo el consumo de ancho de banda.

### 13. ¿Qué ventaja tiene `@PrePersist` sobre asignar la fecha en el constructor de la entidad?
La ventaja principal es que `@PrePersist` se ejecuta dentro del ciclo de vida controlado por el proveedor de persistencia (JPA/Hibernate) justo en el instante anterior a que la sentencia SQL `INSERT` sea enviada a la base de datos. 
Si asignamos la fecha directamente en el constructor de la entidad, corremos el riesgo de alterar los datos reales durante los procesos. Cuando Hibernate recupera un registro existente de la base de datos para transformarlo en un objeto Java, utiliza el constructor por defecto de la entidad; si la lógica de asignación temporal estuviera allí, la fecha de creación se sobrescribiría con la hora actual del servidor de aplicaciones en cada consulta, destruyendo el valor histórico original.

### 14. ¿Qué diferencia hay entre `spring.jpa.hibernate.ddl-auto=update` y `ddl-auto=create`? ¿Cuál usarías en producción y por qué?
- **`create`:** Cada vez que la aplicación Spring Boot se inicia, destruye por completo el esquema de base de datos existente (ejecuta un `DROP TABLE`) y lo vuelve a generar desde cero basándose en las entidades JPA. Provoca la pérdida de todos los datos en cada reinicio.
- **`update`:** Examina la estructura actual de la base de datos al arrancar y realiza modificaciones incrementales (como añadir nuevas tablas o nuevas columnas) para emparejarla con el modelo de entidades Java, sin eliminar las tablas preexistentes ni borrar los datos.
- **Uso en Producción:** En un entorno de producción **no se debería usar ninguna de las dos**. El valor correcto debe establecerse en `none` o `validate`. Permitir que Hibernate altere el esquema en producción de forma automatizada con `update` puede provocar bloqueos de tablas, modificaciones o alteración de datos. Las bases de datos productivas deben gestionarse de forma estrictamente controlada mediante scripts SQL independientes o herramientas.

### 15. Si esta API fuera a producción con usuarios reales, menciona al menos 3 cambios que harías en la configuración o arquitectura.
1. **Seguridad de Capas (Autenticación y Autorización):** Integrar Spring Security junto con un mecanismo de tokens sin estado como JWT (JSON Web Tokens) para asegurar que los endpoints no queden expuestos al público y controlar de manera estricta qué usuarios tienen permisos para crear, editar o eliminar tareas.
2. Eliminar por completo cualquier dato sensible o credencial de acceso directo del archivo `application.yaml`. Se deben utilizar marcadores de posición para inyectar las url, usuarios y contraseñas de la base de datos en tiempo de ejecución a través de variables de entorno seguras o herramientas de gestión de secretos.
3. **Paginación y Limitación de Peticiones:** Reemplazar los listados totales por consultas paginadas obligatorias en la capa de persistencia para evitar consumos excesivos de memoria en el servidor ante volúmenes de gran cantidad de datos, e implementar filtros de control de peticiones por IP para mitigar posibles ataques de denegación de servicios.

---
