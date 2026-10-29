# Sistema de Gestion de Solicitudes Académicas 
Proyecto desarrollado en **Java** para el Trabajo Práctico Integrador de Metodología de Sistemas II, de la Tecnicatura Universitaria en Programación

### Integrantes: 
- Gabriel Torres
- Gabriela Machin
-  Masseroni Ayelen

## Descripción
El sistema permite gestionar solicitudes académicas realizadas por estudiantes, facilitando su registro, seguimiento y asignación a los responsables correspondientes.
El proyecto se utiliza como punto de partida para analizar la calidad del software, identificar deuda técnica y aplicar mejoras mediante los conceptos trabajados en la materia.


## Objetivos del trabajo
A partir del sistema base se busca:
- Analizar la calidad interna y externa del software.
- Identificar problemas de diseño y code smells.
- Aplicar principios SOLID y Clean Code.
- Realizar refactorizaciones.
- Verificar el funcionamiento mediante pruebas.
- Documentar los cambios y realizar una revisión técnica del código.

## Funcionalidades
1. Registrar solicitud de un estudiante.
2. Cambiar el estado de una solicitud.
3. Asignar un responsable.
4. Buscar solicitudes por tipo o estado.
5. Listar un reporte con conteos por estado.


## Estructura del proyecto
```text
TPI_MSI-II/
├── src/tpi/solicitudes/
│   ├── Main.java                  -> Punto de entrada: arma y conecta los objetos.
│   ├── dominio/                   -> Solicitud, DatosSolicitud, Estudiante, Responsable,
│   │                                 Estado, TipoSolicitud, Prioridad, ConEtiqueta,
│   │                                 Identificable, SolicitudException
│   ├── aplicacion/                -> ServicioSolicitudes, Catalogo, RepositorioSolicitudes
│   ├── infraestructura/           -> CatalogoMemoria, RepositorioSolicitudesMemoria
│   └── presentacion/              -> ConsolaSolicitudes, FormateadorSolicitudes
├── pruebas/                       -> Pruebas unitarias, casos antes/después y evidencias.
└── docs/                          -> Diagnostico, SOLID/Clean Code y refactorings.
```

## Ejecución
Con VS Code: abrir esta carpeta y ejecutar `src/tpi/solicitudes/Main.java`.

Por consola, desde la raíz del repositorio (PowerShell):

```powershell
javac -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp out tpi.solicitudes.Main
```

En Linux o macOS:

```bash
javac -d out $(find src -name "*.java")
java -cp out tpi.solicitudes.Main
```

## Datos de prueba

- Estudiantes: ID1- Ana Gonzales | ID2- Pablo Gomez | ID3- Lucia Martinez 

- Responsables:  ID1- Secretaria Academica | ID2- Area de Alumnos

Los datos de prueba son ficticios. Las solicitudes se guardan solo en memoria: al cerrar el programa, se pierden.

## Tecnologías
- Java 11 o superior (probado con JDK 21)
- Visual Studio Code
- Git y GitHub para el control de versiones

## Pruebas
La carpeta `pruebas/` contiene la verificación del comportamiento (etapa 6):

- `PruebasServicio.java`: 17 pruebas unitarias del servicio, sin consola.
- `ComparadorVersiones.java`: ejecuta la versión base (tag `estado-inicial`) y la refactorizada con las mismas entradas y compara las salidas.
- `entradas/` y `salidas/`: archivos de entrada simulados y salidas obtenidas de cada versión.

```powershell
javac -sourcepath src -d out src/tpi/solicitudes/Main.java
javac -cp out -d out pruebas/PruebasServicio.java
java -cp out PruebasServicio

git clone https://github.com/M4ss3A/TPI_MSI-II.git ../base
cd ../base
git checkout estado-inicial
javac -sourcepath . -d out Main.java
cd ../TPI_MSI-II
java pruebas/ComparadorVersiones.java ../base/out out
```

Detalle en `docs/etapa6-pruebas.md`.
