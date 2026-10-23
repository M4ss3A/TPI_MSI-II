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
src/
└── tpi/solicitudes/
    ├── Main.java
    ├── dominio/          # Entidades, enums y reglas del dominio
    ├── aplicacion/       # Casos de uso e interfaces
    ├── infraestructura/ # Implementaciones en memoria
    └── presentacion/     # Interfaz y formato de consola
```

## Ejecución
Desde la carpeta del proyecto, compilar y ejecutar:

```bash
javac -d out -sourcepath src src/tpi/solicitudes/Main.java
java -cp out tpi.solicitudes.Main
```

## Datos de prueba

- Estudiantes: ID1- Ana Gonzales | ID2- Pablo Gomez | ID3- Lucia Martinez 

- Responsables:  ID1- Secretaria Academica | ID2- Area de Alumnos

Los datos de prueba son ficticios. Las solicitudes se guardan solo en memoria: al cerrar el programa, se pierden.
