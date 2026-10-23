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
            mostrarMenu();
            opcion = leerNumero("Opcion: ");
            ejecutarOpcion(opcion);
        }
    }

    static void mostrarMenu() {
        System.out.println("\n=== GESTION DE SOLICITUDES ACADEMICAS ===");
        System.out.println("1. Registrar solicitud");
        System.out.println("2. Cambiar estado");
        System.out.println("3. Asignar responsable");
        System.out.println("4. Buscar solicitudes por tipo o estado");
        System.out.println("5. Ver reporte");
        System.out.println("6. Salir");
    }

    static void ejecutarOpcion(int opcion) {
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

    static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return teclado.nextLine();
    }

    // ---------- Busquedas por ID (anteriormente estaban copiadas en 4 lugares) ----------

    static Estudiante buscarEstudiante(int id) {
        for (Estudiante e : estudiantes) {
            if (e.getId() == id) {
                return e;
            }
        }
        return null;
    }

    static Responsable buscarResponsable(int id) {
        for (Responsable r : responsables) {
            if (r.getId() == id) {
                return r;
            }
        }
        return null;
    }

    static Solicitud buscarSolicitud(int id) {
        for (Solicitud s : solicitudes) {
            if (s.getId() == id) {
                return s;
            }
        }
        return null;
    }

    // ---------- Metodos para mostrar estudiantes y responsables ----------

    static void mostrarEstudiantes() {
        System.out.println("\nEstudiantes disponibles:");
        for (Estudiante e : estudiantes) {
            System.out.println(e.getId() + " - " + e.getNombre());
        }
    }

    static void mostrarResponsables() {
        System.out.println("Responsables disponibles:");
        for (Responsable r : responsables) {
            System.out.println(r.getId() + " - " + r.getNombre());
        }
    }

    // ---------- Casos de uso ----------

    static void registrarSolicitud() {
        mostrarEstudiantes();
        int idEstudiante = leerNumero("ID del estudiante: ");
        Estudiante elegido = buscarEstudiante(idEstudiante);
        if (elegido == null) {
            System.out.println("No se encontro el estudiante.");
            return;
        }
        String tipo = leerTexto("Tipo (Certificado/Beca/Inscripcion): ");
        String prioridad = leerTexto("Prioridad (Alta/Media/Baja): ");
        String descripcion = leerTexto("Descripcion: ");
        DatosSolicitud datos = new DatosSolicitud(elegido, tipo, prioridad, descripcion);
        Solicitud nueva = new Solicitud(proximoId, datos);
        solicitudes.add(nueva);
        System.out.println("Solicitud registrada con ID " + proximoId);
        proximoId++;
    }

    static void cambiarEstado() {
        int id = leerNumero("ID de la solicitud: ");
        Solicitud encontrada = buscarSolicitud(id);
        if (encontrada == null) {
            System.out.println("No se encontro la solicitud.");
            return;
        }
        String nuevoEstado = leerTexto("Nuevo estado (Pendiente/En proceso/Resuelta): ");
        encontrada.cambiarEstado(nuevoEstado);
        System.out.println("Estado actualizado.");
    }

    static void asignarResponsable() {
        int id = leerNumero("ID de la solicitud: ");
        Solicitud encontrada = buscarSolicitud(id);
        if (encontrada == null) {
            System.out.println("No se encontro la solicitud.");
            return;
        }
        mostrarResponsables();
        int idResponsable = leerNumero("ID del responsable: ");
        Responsable elegido = buscarResponsable(idResponsable);
        if (elegido == null) {
            System.out.println("No se encontro el responsable.");
            return;
        }
        encontrada.asignarResponsable(elegido);
        System.out.println("Responsable asignado.");
    }

    static void buscarSolicitudes() {
        String filtro = leerTexto("Escribi un tipo o estado para filtrar: ");
        boolean hayResultados = false;
        for (Solicitud s : solicitudes) {
            if (s.coincideCon(filtro)) {
                System.out.println("ID: " + s.getId() + " | Estudiante: " + s.getEstudiante().getNombre()
                        + " | Tipo: " + s.getTipo() + " | Estado: " + s.getEstado()
                        + " | Responsable: " + s.nombreResponsable());
                hayResultados = true;
            }
        }
        if (!hayResultados) {
            System.out.println("No hay coincidencias.");
        }
    }

    static void mostrarReporte() {
        System.out.println("\n=== REPORTE DE SOLICITUDES ===");
        for (Solicitud s : solicitudes) {
            System.out.println("ID: " + s.getId() + " | Estudiante: " + s.getEstudiante().getNombre()
                    + " | Tipo: " + s.getTipo() + " | Prioridad: " + s.getPrioridad()
                    + " | Estado: " + s.getEstado() + " | Responsable: " + s.nombreResponsable());
        }
        System.out.println("Total: " + solicitudes.size());
        System.out.println("Pendientes: " + contarPorEstado("Pendiente")
                + " | En proceso: " + contarPorEstado("En proceso")
                + " | Resueltas: " + contarPorEstado("Resuelta"));
    }

    static int contarPorEstado(String estado) {
        int cantidad = 0;
        for (Solicitud s : solicitudes) {
            if (s.estaEn(estado)) {
                cantidad++;
            }
        }
        return cantidad;
    }
}
