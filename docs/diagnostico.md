# Diagnostico de calidad del sistema

## Analisis de deuda tecnica y Code Smells

Se realizo un analisis de la version inicial del Sistema para identificar problemas de calidad interna y deuda tecnica.
Aunque el sistema cumple con sus funcionalidades principales, se detectaron aspectos que pueden dificultar su mantenimiento y futuras modificaciones.

## Problemas identificados

| N.º | Problema | Ubicación | Evidencia | Impacto y riesgo | Prioridad |
|---|---|---|---|---|---|
| 1 | Large Class | Main.java | La clase concentra el menú y las operaciones del sistema. | Dificulta el mantenimiento y aumenta el riesgo de afectar otras funcionalidades. | Alta |
| 2 | Duplicated Code | Main.java | Se repite la búsqueda por ID en `cambiarEstado()` y `asignarResponsable()`. | Los cambios deben repetirse en distintos métodos y pueden generar inconsistencias. | Alta |
| 3 | Primitive Obsession | Solicitud.java | Se utilizan String para estado, tipo y prioridad. | Permite valores inconsistentes y dificulta controlar los datos. | Media |
| 4 | Long Parameter List | Solicitud.java | El constructor recibe siete parámetros. | Dificulta la lectura y aumenta el riesgo de confundir argumentos. | Media |
| 5 | Long Method | Main.java | `registrarSolicitud()` realiza varias tareas dentro del mismo método. | Dificulta la comprensión, modificación y prueba del código. | Alta |
| 6 | Falta de encapsulamiento | Estudiante.java, Responsable.java y Solicitud.java | Los atributos son públicos. | Permite modificar datos directamente sin controlar sus valores. | Alta |
| 7 | Validaciones incompletas | Main.java | No se validan correctamente el tipo, la prioridad, la descripción ni los estados ingresados. | Puede generar información incorrecta o inconsistente. | Alta |
| 8 | Variables compartidas modificables | Main.java | Las listas se declaran como variables estáticas y se utilizan desde distintos métodos. | Aumenta el acoplamiento y dificulta las pruebas independientes. | Media |

## Conclusion del diagnostico

Los problemas identificados afectan principalmente la legibilidad, la mantenibilidad y la organización del codigo.

Este diagnostico servira como base para aplicar mejoras mediante los principios SOLID, Clean Code y tecnicas de refactoring, buscando mantener el funcionamiento original del sistema.