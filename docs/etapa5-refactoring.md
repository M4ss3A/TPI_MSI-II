# Etapa 5 - Refactoring seguro

## Resumen

| # | Refactoring | Smells que ataca |
|---|---|---|
| 1 | Extract Method | 2 Duplicated Code, 5 Long Method |
| 2 | Introduce Parameter Object | 4 Long Parameter List |
| 3 | Encapsulate Field | 6 Falta de encapsulamiento |
| 4 | Move Method (reubicación de responsabilidad) | 1 Large Class (Feature Envy) |
| 5 | Replace Type Code with Enum | 3 Primitive Obsession, 7 Validaciones incompletas |
| 6 | Extract Class (con organización en paquetes) | 1 Large Class, 8 Variables compartidas |

La validación de valores de tipo, prioridad y estado en la consola, junto con el rechazo de datos obligatorios incompletos, forma parte del comportamiento actual.

---

## Refactoring 1 - Extract Method

**Problema:** la búsqueda por ID estaba copiada 4 veces (estudiante en `registrarSolicitud`, solicitud en `cambiarEstado` y en `asignarResponsable`, responsable en `asignarResponsable`) y `Main` mezclaba todo en métodos largos.

**Antes** (versión original, en `cambiarEstado`, de forma similar en `asignarResponsable`):
```java
Solicitud encontrada = null;
for (Solicitud s : solicitudes) {
    if (s.id == id) {
        encontrada = s;
    }
}
if (encontrada == null) { ... }
```
**Después de Extract Method**, el bucle pasó a un método reutilizable:
```java
Solicitud encontrada = buscarSolicitud(id);
if (encontrada == null) { ... }
```
En la versión final, esa búsqueda reutilizable está encapsulada en `Catalogo.buscarPorId()`, implementada por `CatalogoMemoria`. `ServicioSolicitudes.obtenerEstudiante()`, `obtenerResponsable()` y `obtenerSolicitud()` usan esa operación y convierten la ausencia del resultado en `SolicitudException`. La consola utiliza el servicio, sin buscar directamente en listas.

También se extrajeron métodos para separar tareas de la interfaz y el reporte, como `mostrarEstudiantes()`, `mostrarResponsables()`, `mostrarMenu()`, `ejecutarOpcion()`, `leerTexto()` y `contarPorEstado()`; en la versión actual, el conteo forma parte de `ServicioSolicitudes.contarPorEstado()`. Se eliminó además el `import java.lang.reflect.Method` que no se usaba (código muerto).
**Resultado:** la búsqueda por ID dejó de repetirse y ahora se reutiliza mediante una interfaz común; las responsabilidades de consulta están separadas de la consola y del almacenamiento concreto.

## Refactoring 2 - Introduce Parameter Object

**Antes:**
```java
new Solicitud(proximoId, elegido, tipo, prioridad, descripcion, "Pendiente", null);   // 7 parámetros
```
**Después:**
```java
DatosSolicitud datos = new DatosSolicitud(elegido, tipo, prioridad, descripcion);
Solicitud nueva = new Solicitud(proximoId, datos);                                    // 2 parámetros
```
`estado = "Pendiente"` y `responsable = null` dejaron de ser parámetros: son valores que una solicitud nueva siempre tiene, así que pasaron a ser regla interna del constructor.
**Resultado:** 7 -> 2 parámetros. Quien crea una solicitud ya no puede equivocarse en el estado inicial ni en el responsable, porque no los recibe.

## Refactoring 3 - Encapsulate Field

**Antes:** `public int id; public String estado; ...` en las 3 clases (y cualquiera podía hacer `encontrada.estado = "cualquier valor"`).
**Después:** `private final` para lo que no cambia (id, estudiante, tipo, prioridad, descripción) y `private` solo para lo que cambia (estado, responsable), acceso mediante getters.
**Resultado:** 0 campos públicos (antes: 11 entre las 3 clases).

## Refactoring 4 - Move Method

**Problema (Feature Envy):** `Main` manipulaba los datos internos de `Solicitud`: decidía si coincidía con un filtro, calculaba el nombre del responsable, cambiaba el estado.

**Antes** (`Main`):
```java
if (s.getTipo().equalsIgnoreCase(filtro) || s.getEstado().equalsIgnoreCase(filtro)) { ... }
static String nombreResponsable(Solicitud s) {
    return s.getResponsable() == null ? "Sin asignar" : s.getResponsable().getNombre();
}
encontrada.setEstado(nuevoEstado);
```
**Después** (`Solicitud`):
```java
public boolean coincideCon(String filtro) { ... }
public String nombreResponsable() { ... }
public void cambiarEstado(Estado nuevoEstado) { ... }
public void asignarResponsable(Responsable nuevoResponsable) { ... }
```
Se eliminaron los setters públicos: ahora el objeto expone **operaciones del negocio**, no simples asignaciones.
**Resultado:** la lógica de una solicitud vive en `Solicitud`. `Main` reduce su tamaño y deja de conocer su estructura interna.

## Refactoring 5 - Replace Type Code with Enum

**Antes:** `String tipo, prioridad, estado` -> cualquier texto era válido y los estados se comparaban con `equalsIgnoreCase("Pendiente")` en varios lugares.
**Después:** enums `TipoSolicitud`, `Prioridad`, `Estado` que implementan `ConEtiqueta` (texto visible + búsqueda sin distinguir mayúsculas). La consola usa un único método genérico `leerOpcion(campo, Enum.class)` que repite la pregunta hasta recibir un valor válido.
```java
TipoSolicitud tipo = leerOpcion("Tipo", TipoSolicitud.class);
```
**Resultado:** la consola rechaza entradas que no coinciden con las opciones de los enums y vuelve a preguntar. Los valores ingresados sin distinguir mayúsculas se representan mediante la etiqueta del enum. El prompt (por ejemplo, `Tipo (Certificado/Beca/Inscripcion)`) se genera desde el enum. Además, `tipo` y `prioridad` ahora son tipos distintos, por lo que el compilador impide intercambiarlos por error. `DatosSolicitud` también rechaza datos obligatorios nulos y descripciones vacías. Esta validación de entrada no significa que todos los métodos del dominio impidan recibir `null` si se invocan directamente.

## Refactoring 6 - Extract Class

**Problema:** `Main` (168 líneas) era menú + lógica + almacenamiento + formato, con listas `static` compartidas.
**Después:**

| Clase nueva | Responsabilidad |
|---|---|
| `ConsolaSolicitudes` | Menú, lectura de teclado, mostrar resultados y errores |
| `ServicioSolicitudes` | Casos de uso: registrar, cambiar estado, asignar, buscar, contar |
| `FormateadorSolicitudes` | Texto de cada línea del reporte/búsqueda |
| `Catalogo` / `CatalogoMemoria` | Consulta de estudiantes y responsables (solo lectura) |
| `RepositorioSolicitudes` / `RepositorioSolicitudesMemoria` | Guardar y consultar solicitudes, generar IDs |
| `SolicitudException` | Errores de negocio con mensaje para el usuario |

`Main` queda como punto de entrada: solo crea los objetos y los conecta. No quedan variables estáticas mutables (antes: 5).

### Organización en paquetes (decisión de diseño de este refactoring)

Al extraer estas responsabilidades, cada clase nueva se ubicó directamente en un paquete según su rol, dentro de `src/tpi/solicitudes/`:

| Paquete | Clases | Responsabilidad |
|---|---|---|
| `dominio` | `Solicitud`, `DatosSolicitud`, `Estudiante`, `Responsable`, `Estado`, `TipoSolicitud`, `Prioridad`, `ConEtiqueta`, `Identificable`, `SolicitudException` | Reglas y conceptos del negocio |
| `aplicacion` | `ServicioSolicitudes`, `Catalogo`, `RepositorioSolicitudes` | Casos de uso y contratos (interfaces) que necesitan |
| `infraestructura` | `CatalogoMemoria`, `RepositorioSolicitudesMemoria` | Implementaciones concretas de almacenamiento |
| `presentacion` | `ConsolaSolicitudes`, `FormateadorSolicitudes` | Interacción con el usuario por consola |

`Main` queda en el paquete raíz `tpi.solicitudes`, porque es el único que conoce todas las capas y las conecta.

**Criterio:** las dependencias van en un solo sentido. `presentacion` depende de `aplicacion`, que depende de `dominio`; `infraestructura` depende de `aplicacion` y `dominio`; `dominio` no depende de ninguna otra capa. Así la estructura de carpetas hace visible la separación de responsabilidades (SRP) y la inversión de dependencias (DIP): el servicio conoce las interfaces de `aplicacion`, nunca las implementaciones de `infraestructura`. Esta organización cambia la forma de compilar y ejecutar el sistema (ver README).

## Comparación global antes/después

| Métrica | Antes | Después |
|---|---|---|
| Líneas de `Main.java` | 168 | menos de 40 (solo arma los objetos) |
| Tipos Java | 4 | 18 (11 clases, 3 enums y 4 interfaces) |
| Paquetes | 1 (por defecto) | 4 (`dominio`, `aplicacion`, `infraestructura`, `presentacion`) |
| Campos públicos | 11 | 0 |
| Variables `static` mutables | 5 | 0 |
| Parámetros del constructor de `Solicitud` | 7 | 2 |
| Búsquedas por ID duplicadas | 4 | 1 |
| Valores de tipo, prioridad y estado fuera de los enums ingresados por consola | Sí | No |

## Límites y mejoras pendientes
- La persistencia sigue siendo solo en memoria. El diseño permite agregar archivo o base de datos creando otra implementación de `RepositorioSolicitudes`, sin tocar el servicio.