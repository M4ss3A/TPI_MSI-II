# Etapa 7 - Code review profesional

- **Objeto de la revisión:** rama `main`, commit `ba84235` (versión refactorizada, etapas 4 a 6).
- **Revisor:** Gabriel Torres. **Autora del código revisado:** Gabriela Machin (etapas 4 y 5).
- **Método:** lectura de las 18 clases de `src/`, ejecución de la aplicación con entradas de borde y aplicación del checklist.

## 1. Checklist técnico

Resultado: Cumple (C), Observación (O), No cumple (X).

| # | Criterio | Res. | Evidencia |
|---|---|---|---|
| 1 | Nombres claros y consistentes | C | Clases y métodos descriptivos, sin abreviaturas |
| 2 | Métodos cortos, una tarea por método | C | Ningún método supera ~15 líneas |
| 3 | Sin números ni textos mágicos | X | `case 1..5` en `despachar()` junto a `OPCION_SALIR` (O-04) |
| 4 | Sin código muerto | X | `Solicitud.estaEn()` sin usos (O-03) |
| 5 | Una responsabilidad por clase (SRP) | O | `ConsolaSolicitudes` (183 líneas) mezcla menú, casos de uso y lectura (O-05) |
| 6 | Dependencias hacia abstracciones (DIP) | C | `ServicioSolicitudes` usa `Catalogo` y `RepositorioSolicitudes` |
| 7 | Encapsulamiento | O | Atributos private, pero `Solicitud` queda mutable desde afuera (O-06) |
| 8 | Evita `null` | O | `Solicitud.responsable` es `null` hasta asignarlo (O-07) |
| 9 | API consistente | O | `cambiarEstado(Solicitud, Estado)` vs `asignarResponsable(Solicitud, int)` (O-09) |
| 10 | Errores de negocio centralizados | C | `SolicitudException`, mostrada en un solo punto |
| 11 | Entrada de usuario validada | O | Se rechazaban valores con espacios, como "Alta " (O-02) |
| 12 | Sin excepciones no controladas | X | Fin de entrada terminaba con stack trace (O-01) |
| 13 | Mensajes consistentes entre capas | O | "El dato no puede estar vacio." vs "La descripcion no puede estar vacia." (O-10) |
| 14 | Sin duplicación | O | `lineaBusqueda` y `lineaReporte` repiten el armado (O-11) |
| 15 | Funcionalidad completa y visible | O | La descripción se guarda pero nunca se muestra (O-08) |
| 16 | Pruebas de la lógica de negocio | C | 17 pruebas unitarias en `PruebasServicio` |
| 17 | Comportamiento externo preservado | C | CP-01 a CP-03 idénticos a la versión base |
| 18 | Pruebas con framework y cobertura medida | O | Pruebas propias sin JUnit ni medición (O-12) |

Resumen: 6 cumple, 9 observaciones, 3 no cumple.

## 2. Observaciones y feedback

| ID | Hallazgo | Ubicación | Prioridad | Acción | Estado |
|---|---|---|---|---|---|
| O-01 | Si la entrada se cierra antes de elegir Salir, el programa termina con `NoSuchElementException` (también ocurría en la versión base) | `ConsolaSolicitudes.ejecutar()` | Alta | Capturar el fin de entrada y cerrar con mensaje | Corregida |
| O-02 | `"Alta "` o `" Beca"` se rechazan; la búsqueda `"Beca "` no devuelve nada | `ConEtiqueta.coincideCon()` | Media | `trim()` y protección contra `null` | Corregida |
| O-03 | Método sin usos | `Solicitud.estaEn()` | Baja | Eliminarlo | Corregida |
| O-04 | Números literales en el `switch` | `ConsolaSolicitudes.despachar()` | Baja | Constantes `OPCION_*` | Corregida |
| O-05 | La consola concentra menú, casos de uso, listados y lectura de datos | `ConsolaSolicitudes` | Media | Extraer una clase `LectorConsola` | Pendiente |
| O-06 | `Solicitud` expone `cambiarEstado()` y `asignarResponsable()` públicos; se puede saltear el servicio | `Solicitud` | Media | Restringir visibilidad o mover reglas al servicio | Pendiente |
| O-07 | `responsable` vale `null` y `getResponsable()` lo devuelve | `Solicitud` | Media | Usar `Optional<Responsable>` | Pendiente |
| O-08 | La descripción se registra pero ninguna salida la muestra | `FormateadorSolicitudes` | Media | Decidir si se muestra; cambiaría la salida y exige un caso de prueba nuevo | Pendiente |
| O-09 | Un método recibe el objeto y otro el ID | `ServicioSolicitudes` | Baja | Unificar firmas | Pendiente |
| O-10 | Misma validación con textos distintos | `ConsolaSolicitudes` y `DatosSolicitud` | Baja | Unificar mensajes | Pendiente |
| O-11 | Repetición en el armado de líneas | `FormateadorSolicitudes` | Baja | Reusar un método base | Pendiente |
| O-12 | Pruebas sin framework ni cobertura | `pruebas/` | Baja | Migrar a JUnit y medir con JaCoCo | Pendiente |

## 3. Correcciones realizadas

Verificadas con `pruebas/PruebasCodeReview.java` (4 pruebas):

| Prueba | Verifica | Corrige |
|---|---|---|
| CR-01 | `buscar` de etiquetas ignora espacios | O-02 |
| CR-02 | `buscar` con `null` no falla | O-02 |
| CR-03 | `buscar(" beca ")` encuentra la solicitud | O-02 |
| CR-04 | La consola termina limpia ante fin de entrada | O-01 |

## 4. Criterios de aceptación técnica

| Criterio | Resultado |
|---|---|
| Compila con `javac -Xlint:all` sin advertencias | Cumple |
| `PruebasServicio`: 17 OK, 0 fallos | Cumple |
| `PruebasCodeReview`: 4 OK, 0 fallos | Cumple |
| `ComparadorVersiones`: 5 de 5 casos como se esperaba | Cumple |
| Las correcciones no alteran las salidas de CP-01 a CP-05 | Cumple |
| Observaciones de prioridad Alta corregidas | Cumple |
| Observaciones de prioridad Media corregidas o justificadas | Justificadas como mejoras futuras |

## 5. Cómo verificar

```
javac -Xlint:all -sourcepath src -d out src/tpi/solicitudes/Main.java
javac -cp out -d out pruebas/PruebasServicio.java pruebas/PruebasCodeReview.java
java -cp out PruebasServicio
java -cp out PruebasCodeReview
java pruebas/ComparadorVersiones.java ../base/out out
```
