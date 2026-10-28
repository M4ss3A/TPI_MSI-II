# Etapa 6 - Pruebas y verificación del comportamiento

## 1. Estrategia

El sistema base es una aplicación de consola sin pruebas automatizadas, así que se usaron tres técnicas complementarias:

| Técnica | Qué demuestra | Dónde |
|---|---|---|
| **Pruebas de caracterización ** | Con las mismas entradas, la versión refactorizada produce la misma salida que la base. | `pruebas/entradas/`, `pruebas/ComparadorVersiones.java` |
| **Pruebas unitarias del servicio** | La lógica de negocio funciona sin consola ni teclado (posible gracias a DIP, Etapa 4). | `pruebas/PruebasServicio.java` |
| **Pruebas de los cambios intencionales** | Los únicos cambios de comportamiento son los documentados (validación, smell 7). | (CP = Caso de Prueba) CP-04 y CP-05 |

**Principio:** primero se fija el comportamiento de la versión `estado-inicial` (ejecutándola con entradas guionadas) y después se ejecutan las mismas entradas sobre la versión refactorizada. Si la salida es idéntica, el refactoring no alteró el comportamiento externo, solo el interno. 

## 2. Casos de prueba antes/después

Cada caso usa un archivo de entradas simuladas por teclado. Se ejecuta con `java ... < entrada.txt` y se compara la salida completa con `diff`.

| ID | Caso | Cubre | Resultado esperado | Resultado |
|---|---|---|---|---|
| CP-01 | Flujo completo válido: 3 solicitudes, cambio de estado, asignación, 3 búsquedas y reporte | Las 5 funcionalidades | Salida idéntica | **IDÉNTICO** |
| CP-02 | Errores de negocio: IDs inexistentes (estudiante, solicitud, responsable), opción de menú inválida, número inválido, búsqueda sin coincidencias | Manejo de errores | Mismos mensajes | **IDÉNTICO** |
| CP-03 | Sistema vacío: reporte y búsqueda sin datos | Borde: lista vacía | Total 0 y "No hay coincidencias." | **IDÉNTICO** |
| CP-04 | Entradas inválidas: tipo `Foo`, prioridad `Urgente`, descripción vacía, estado `Cerrada` | Validación (smell 7) | **Cambio intencional** | **DIFIERE (esperado)** |
| CP-05 | Entradas en minúsculas (`certificado`, `alta`) | Normalización de texto | **Cambio intencional** | **DIFIERE (esperado)** |

### Resultado de CP-01 a CP-03
Los tres casos producen salida idéntica byte a byte entre `estado-inicial` y la versión refactorizada. Esto cubre registrar, cambiar estado, asignar responsable, buscar por tipo y por estado, el reporte con conteos y los mensajes de error.

### CP-04 - Cambio intencional: validación de datos
| | Versión base | Versión refactorizada |
|---|---|---|
| Tipo `Foo` | Lo acepta | `Valor invalido. Opciones: Certificado/Beca/Inscripcion.` y vuelve a preguntar |
| Prioridad `Urgente` | La acepta | `Valor invalido. Opciones: Alta/Media/Baja.` |
| Descripción vacía | La acepta | `El dato no puede estar vacio.` |
| Estado `Cerrada` | Lo acepta | `Valor invalido. Opciones: Pendiente/En proceso/Resuelta.` |

**Evidencia de un defecto real de la versión base:** tras cambiar el estado a `Cerrada`, el reporte muestra `Total: 1` pero `Pendientes: 0 | En proceso: 0 | Resueltas: 0`. Los conteos dejan de sumar el total porque el `String` libre no coincide con ningún estado. En la versión refactorizada ese estado no se puede ingresar y los conteos siempre suman el total.

> Por la validación, la versión refactorizada vuelve a preguntar ante un dato inválido, así que el archivo de entradas de CP-04 es distinto para cada versión (`cp04_invalidas_base.txt` y `cp04_invalidas_refac.txt`).

### CP-05 - Cambio intencional: texto normalizado
La base guardaba el texto tal cual se escribía; la refactorizada lo convierte a la etiqueta del enum.

| Entrada | Base | Refactorizada |
|---|---|---|
| `certificado` / `alta` | `Tipo: certificado \| Prioridad: alta` | `Tipo: Certificado \| Prioridad: Alta` |

Es el cambio 3 documentado en `docs/etapa4-solid-clean-code.md`. La búsqueda sigue siendo insensible a mayúsculas en ambas versiones.

## 3. Pruebas unitarias del servicio

`pruebas/PruebasServicio.java` prueba `ServicioSolicitudes` directamente, sin `Scanner` ni `System.out`. No usa librerías externas.

| ID | Verifica |
|---|---|
| TU-01 a TU-04 | `registrar`: ID, estado inicial Pendiente, sin responsable, IDs consecutivos |
| TU-05, TU-06 | `cambiarEstado` y `asignarResponsable` |
| TU-07 a TU-09 | Errores de negocio: responsable, solicitud y estudiante inexistentes |
| TU-10 a TU-12 | `buscar` por tipo (sin distinguir mayúsculas), por estado y sin coincidencias |
| TU-13, TU-14 | `contarPorEstado` incluye todos los estados, aun con 0 |
| TU-15, TU-16 | `DatosSolicitud` rechaza descripción vacía y datos nulos |
| TU-17 | La lista devuelta por el servicio no se puede modificar desde afuera |

**Resultado: 17 OK, 0 fallos.**

## 4. Cómo reproducir (solo Java)

Se necesitan dos copias del proyecto compiladas: la versión base (tag `estado-inicial`) y la refactorizada (rama `main` actual).

```
# Versión refactorizada: desde la raíz del repo
javac -sourcepath src -d out src/tpi/solicitudes/Main.java

# Versión base: en una carpeta aparte, con el tag estado-inicial
git clone https://github.com/M4ss3A/TPI_MSI-II.git base
cd base
git checkout estado-inicial
mkdir out
javac -sourcepath . -d out Main.java
cd ..

# Pruebas unitarias
javac -cp out -d out pruebas/PruebasServicio.java
java -cp out PruebasServicio

# Comparación antes/después (ejecuta ambas versiones con las mismas entradas)
java pruebas/ComparadorVersiones.java base/out out
```

`ComparadorVersiones.java` lanza cada versión como proceso Java, le pasa por entrada estándar el archivo de `pruebas/entradas/`, captura lo que imprime, guarda ambas salidas en `pruebas/salidas/` y muestra `IDENTICO` o `DIFIERE` contra lo esperado. Salida real obtenida:

```
CP-01 flujo completo valido      esperado=IDENTICO real=IDENTICO [OK]
CP-02 errores de negocio         esperado=IDENTICO real=IDENTICO [OK]
CP-03 sistema vacio              esperado=IDENTICO real=IDENTICO [OK]
CP-04 entradas invalidas         esperado=DIFIERE  real=DIFIERE  [OK]
CP-05 texto en minusculas        esperado=DIFIERE  real=DIFIERE  [OK]

Resultado: 5 de 5 casos como se esperaba
```

## 5. Conclusión de la etapa

- Con entradas válidas y errores de negocio (CP-01 a CP-03) el comportamiento externo se mantiene sin regresiones.
- Los únicos cambios son la validación de datos y la normalización de texto (CP-04 y CP-05), ambos documentados y buscados por el refactoring (smell 7).
- La separación en capas permitió probar la lógica sin consola (TU-01 a TU-17), lo que no era posible en la versión base.
- **Límite del trabajo:** las pruebas cubren los flujos principales y los bordes más comunes, pero no hay cobertura medida ni pruebas de la capa de consola más allá de la comparación de salidas.
