import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static ArrayList<Estudiante> estudiantes = new ArrayList<>();
    static ArrayList<Responsable> responsables = new ArrayList<>();
    static ArrayList<Solicitud> solicitudes = new ArrayList<>();
    static Scanner teclado = new Scanner(System.in);
    static int proximoId = 1;

    public static void main(String[] args) {
        estudiantes.add(new Estudiante(1, "Ana Gonzales"));
        estudiantes.add(new Estudiante(2, "Pablo Gomez"));
        estudiantes.add(new Estudiante(3, "Lucia Martinez"));
        responsables.add(new Responsable(1, "Secretaria Academica"));
        responsables.add(new Responsable(2, "Area de Alumnos"));

        int opcion = 0;
        while (opcion != 6) {
            System.out.println("\n=== GESTION DE SOLICITUDES ACADEMICAS ===");
            System.out.println("1. Registrar solicitud");
            System.out.println("2. Cambiar estado");
            System.out.println("3. Asignar responsable");
            System.out.println("4. Buscar solicitudes por tipo o estado");
            System.out.println("5. Ver reporte");
            System.out.println("6. Salir");
            opcion = leerNumero("Opcion: ");

            switch (opcion) {
                case 1: registrarSolicitud(); break;
                case 2: cambiarEstado(); break;
                case 3: asignarResponsable(); break;
                case 4: buscarSolicitudes(); break;
                case 5: mostrarReporte(); break;
                case 6: System.out.println("Hasta luego."); break;
                default: System.out.println("Opcion invalida.");
            }
        }
    }

    static int leerNumero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(teclado.nextLine());
            } catch (NumberFormatException error) {
                System.out.println("Ingresa un numero valido.");
            }
        }
    }

    static void registrarSolicitud() {
        System.out.println("\nEstudiantes disponibles:");
        for (Estudiante e : estudiantes) {
            System.out.println(e.id + " - " + e.nombre);
        }
        int idEstudiante = leerNumero("ID del estudiante: ");
        Estudiante elegido = null;
        for (Estudiante e : estudiantes) {
            if (e.id == idEstudiante) {
                elegido = e;
            }
        }
        if (elegido == null) {
            System.out.println("No se encontro el estudiante.");
            return;
        }
        System.out.print("Tipo (Certificado/Beca/Inscripcion): ");
        String tipo = teclado.nextLine();
        System.out.print("Prioridad (Alta/Media/Baja): ");
        String prioridad = teclado.nextLine();
        System.out.print("Descripcion: ");
        String descripcion = teclado.nextLine();
        Solicitud nueva = new Solicitud(proximoId, elegido, tipo, prioridad,
                descripcion, "Pendiente", null);
        solicitudes.add(nueva);
        System.out.println("Solicitud registrada con ID " + proximoId);
        proximoId++;
    }

    static void cambiarEstado() {
        int id = leerNumero("ID de la solicitud: ");
        Solicitud encontrada = null;
        for (Solicitud s : solicitudes) {
            if (s.id == id) {
                encontrada = s;
            }
        }
        if (encontrada == null) {
            System.out.println("No se encontro la solicitud.");
            return;
        }
        System.out.print("Nuevo estado (Pendiente/En proceso/Resuelta): ");
        String nuevoEstado = teclado.nextLine();
        encontrada.estado = nuevoEstado;
        System.out.println("Estado actualizado.");
    }

    static void asignarResponsable() {
        int id = leerNumero("ID de la solicitud: ");
        Solicitud encontrada = null;
        for (Solicitud s : solicitudes) {
            if (s.id == id) {
                encontrada = s;
            }
        }
        if (encontrada == null) {
            System.out.println("No se encontro la solicitud.");
            return;
        }
        System.out.println("Responsables disponibles:");
        for (Responsable r : responsables) {
            System.out.println(r.id + " - " + r.nombre);
        }
        int idResponsable = leerNumero("ID del responsable: ");
        Responsable elegido = null;
        for (Responsable r : responsables) {
            if (r.id == idResponsable) {
                elegido = r;
            }
        }
        if (elegido == null) {
            System.out.println("No se encontro el responsable.");
            return;
        }
        encontrada.responsable = elegido;
        System.out.println("Responsable asignado.");
    }

    static void buscarSolicitudes() {
        System.out.print("Escribi un tipo o estado para filtrar: ");
        String filtro = teclado.nextLine();
        boolean hayResultados = false;
        for (Solicitud s : solicitudes) {
            if (s.tipo.equalsIgnoreCase(filtro) || s.estado.equalsIgnoreCase(filtro)) {
                String nombreResponsable = s.responsable == null
                        ? "Sin asignar" : s.responsable.nombre;
                System.out.println("ID: " + s.id + " | Estudiante: " + s.estudiante.nombre
                        + " | Tipo: " + s.tipo + " | Estado: " + s.estado
                        + " | Responsable: " + nombreResponsable);
                hayResultados = true;
            }
        }
        if (!hayResultados) {
            System.out.println("No hay coincidencias.");
        }
    }

    static void mostrarReporte() {
        int pendientes = 0;
        int enProceso = 0;
        int resueltas = 0;
        System.out.println("\n=== REPORTE DE SOLICITUDES ===");
        for (Solicitud s : solicitudes) {
            String nombreResponsable = s.responsable == null
                    ? "Sin asignar" : s.responsable.nombre;
            System.out.println("ID: " + s.id + " | Estudiante: " + s.estudiante.nombre
                    + " | Tipo: " + s.tipo + " | Prioridad: " + s.prioridad
                    + " | Estado: " + s.estado + " | Responsable: " + nombreResponsable);
            if (s.estado.equalsIgnoreCase("Pendiente")) pendientes++;
            if (s.estado.equalsIgnoreCase("En proceso")) enProceso++;
            if (s.estado.equalsIgnoreCase("Resuelta")) resueltas++;
        }
        System.out.println("Total: " + solicitudes.size());
        System.out.println("Pendientes: " + pendientes + " | En proceso: " + enProceso
                + " | Resueltas: " + resueltas);
    }
}
