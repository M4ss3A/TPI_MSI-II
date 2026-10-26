# Etapa 4 - Aplicación de SOLID y Clean Code

Cada mejora se vincula con un problema del `docs/diagnostico.md` (columna "Smell") y con el refactoring donde se aplicó.
El detalle técnico de cada transformación (antes/después de código) está en `docs/etapa5-refactoring.md`.

## 1. Principios SOLID

### SRP - Responsabilidad única
| | Antes | Después |
|---|---|---|
| `Main` | Menú, lectura del teclado, búsqueda de datos, reglas de negocio, formato de salida, almacenamiento (listas estáticas) y conteos: **6 razones para cambiar**. | `Main` solo arma y conecta los objetos. |
| Responsabilidades | Todas en una clase de 168 líneas. | `ConsolaSolicitudes` y `FormateadorSolicitudes` (paquete `presentacion`), `ServicioSolicitudes` (`aplicacion`), `RepositorioSolicitudesMemoria` y `CatalogoMemoria` (`infraestructura`), `Solicitud` y el resto del negocio (`dominio`). |

Smells resueltos: 1 (Large Class), 8 (variables estáticas compartidas). Refactoring: *Extract Class* (con organización en paquetes).

### OCP - Abierto/cerrado
- **Estados:** el reporte recorre `Estado.values()` y el conteo se arma con `EnumMap`. Agregar un estado nuevo (ej. `RECHAZADA`) es **agregar una línea al enum**; el reporte, la validación del menú y el texto "Nuevo estado (…)" se actualizan solos. Antes había que editar `mostrarReporte()`, el texto del prompt y los `equalsIgnoreCase` de a uno.
- **Tipos y prioridades:** igual, vía `TipoSolicitud` y `Prioridad`.
- **Almacenamiento:** se puede agregar un repositorio en archivo o base de datos sin modificar `ServicioSolicitudes` (ver DIP).

Smell resuelto: 3 (Primitive Obsession). Refactoring: *Replace Type Code with Enum*.

### LSP - Sustitución de Liskov
El sistema original no tiene herencia, así que **LSP no aplica de forma natural y no se forzó una jerarquía artificial**. El único caso real es `RepositorioSolicitudesMemoria extends CatalogoMemoria<Solicitud>`: la subclase respeta el contrato de la clase base (`listar()` devuelve una lista no modificable, `buscarPorId()` nunca devuelve `null` sino `Optional`) y puede usarse en cualquier lugar donde se espere un `Catalogo<Solicitud>`.

### ISP - Segregación de interfaces
`Catalogo<T>` es de **solo lectura** (`listar`, `buscarPorId`). `RepositorioSolicitudes` la extiende y agrega la escritura (`siguienteId`, `guardar`). `ServicioSolicitudes` recibe estudiantes y responsables como `Catalogo` (no necesita guardarlos), por lo que no depende de métodos que nunca usa.

### DIP - Inversión de dependencias
`ServicioSolicitudes` depende de las interfaces `Catalogo` y `RepositorioSolicitudes`, no de las clases en memoria. Las interfaces viven en el paquete `aplicacion` y sus implementaciones en `infraestructura`, de modo que el servicio no importa nada de `infraestructura`. Las implementaciones concretas se eligen en un único lugar: `Main`. Consecuencia práctica: el servicio puede probarse sin consola ni teclado (útil para la Etapa 6), y cambiar el almacenamiento no toca la lógica.

## 2. Clean Code

| Práctica | Antes | Después | Smell |
|---|---|---|---|
| **Encapsulamiento** | Atributos `public` modificables desde cualquier clase (`encontrada.estado = nuevoEstado`). | Atributos `private`, `final` cuando no cambian; el estado solo cambia mediante `cambiarEstado()` y el responsable mediante `asignarResponsable()`. | 6 |
| **Funciones pequeñas** | `registrarSolicitud()` hacía listado, búsqueda, lectura de 3 datos, creación, guardado e impresión. | Cada método hace una cosa: `registrarSolicitud()` (flujo), `leerOpcion()`, `leerTextoNoVacio()`, `servicio.registrar()`. | 5 |
| **No repetirse (DRY)** | Búsqueda por ID copiada 4 veces; el ternario `Sin asignar` copiado 2 veces. | `buscarPorId()` único en `CatalogoMemoria`; `Solicitud.nombreResponsable()`. | 2 |
| **Nombres que revelan intención** | `Method` importado sin uso, `s`, `e`, `r`. | Se elimina el import muerto; nombres como `solicitud`, `coincidencias`, `nuevoEstado`, `idResponsable`; constante `OPCION_SALIR` en lugar del número `6` repetido. | - |
| **Menos parámetros** | Constructor de `Solicitud` con 7 parámetros. | `Solicitud(int id, DatosSolicitud datos)`: 2 parámetros. | 4 |
| **Manejo de errores** | Se devolvía `null` y cada método repetía `if (x == null) { print; return; }`. | `Optional` + `SolicitudException`; los errores se muestran en **un solo punto** (`ConsolaSolicitudes.ejecutarOpcion`). | 2 |
| **Validación de datos** | Aceptaba cualquier texto como tipo, prioridad, estado y descripción vacía. | La consola valida tipo, prioridad y estado antes de llamar al servicio, y vuelve a preguntar ante una entrada inválida. También rechaza descripciones vacías; `DatosSolicitud` rechaza datos incompletos. | 7 |
| **Baja cohesión / alto acoplamiento** | `Main` accedía a los campos internos de `Solicitud` (`s.responsable.nombre`) - *Feature Envy*. | `Solicitud` expone comportamiento (`coincideCon`, `estaEn`, `nombreResponsable`); la consola no accede a los campos de `Solicitud` ni al almacenamiento: usa el servicio. | 1 |

## 3. Cambios de comportamiento intencionales

En el flujo por consola se aplican estas validaciones (smell 7):

1. Las entradas de tipo, prioridad y estado deben coincidir con una opción del enum; si no, se muestra `Valor invalido. Opciones: …` y se vuelve a preguntar.
2. La descripción vacía se rechaza tanto en la consola como al construir `DatosSolicitud`.
3. Las opciones ingresadas sin distinguir mayúsculas se representan con la etiqueta del enum; por ejemplo, `alta` corresponde a `Alta`.

Esta validación ocurre en la interfaz de consola y en `DatosSolicitud`; no implica que todos los métodos del servicio o del dominio rechacen por sí mismos cualquier argumento nulo.