package tpi.solicitudes.presentacion;

import tpi.solicitudes.aplicacion.ServicioSolicitudes;
import tpi.solicitudes.dominio.ConEtiqueta;
import tpi.solicitudes.dominio.DatosSolicitud;
import tpi.solicitudes.dominio.Estado;
import tpi.solicitudes.dominio.Estudiante;
import tpi.solicitudes.dominio.Prioridad;
import tpi.solicitudes.dominio.Responsable;
import tpi.solicitudes.dominio.Solicitud;
import tpi.solicitudes.dominio.SolicitudException;
import tpi.solicitudes.dominio.TipoSolicitud;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/** Interfaz de usuario por consola: menu, lectura de datos y muestra de resultados. */
public class ConsolaSolicitudes {

    private static final int OPCION_SALIR = 6;

    private final ServicioSolicitudes servicio;
    private final FormateadorSolicitudes formateador;
    private final Scanner teclado;

    public ConsolaSolicitudes(ServicioSolicitudes servicio, FormateadorSolicitudes formateador,
                              Scanner teclado) {
        this.servicio = servicio;
        this.formateador = formateador;
        this.teclado = teclado;
    }

    public void ejecutar() {
        int opcion = 0;
        while (opcion != OPCION_SALIR) {
            mostrarMenu();
            opcion = leerNumero("Opcion: ");
            ejecutarOpcion(opcion);
        }
    }

    // ---------- Menu ----------

    private void mostrarMenu() {
        System.out.println("\n=== GESTION DE SOLICITUDES ACADEMICAS ===");
        System.out.println("1. Registrar solicitud");
        System.out.println("2. Cambiar estado");
        System.out.println("3. Asignar responsable");
        System.out.println("4. Buscar solicitudes por tipo o estado");
        System.out.println("5. Ver reporte");
        System.out.println("6. Salir");
    }

    /** Punto unico donde se muestran los errores de negocio al usuario. */
    private void ejecutarOpcion(int opcion) {
        try {
            despachar(opcion);
        } catch (SolicitudException error) {
            System.out.println(error.getMessage());
        }
    }

    private void despachar(int opcion) {
        switch (opcion) {
            case 1: registrarSolicitud(); break;
            case 2: cambiarEstado(); break;
            case 3: asignarResponsable(); break;
            case 4: buscarSolicitudes(); break;
            case 5: mostrarReporte(); break;
            case OPCION_SALIR: System.out.println("Hasta luego."); break;
            default: System.out.println("Opcion invalida.");
        }
    }

    // ---------- Casos de uso ----------

    private void registrarSolicitud() {
        mostrarEstudiantes();
        Estudiante estudiante = servicio.obtenerEstudiante(leerNumero("ID del estudiante: "));
        TipoSolicitud tipo = leerOpcion("Tipo", TipoSolicitud.class);
        Prioridad prioridad = leerOpcion("Prioridad", Prioridad.class);
        String descripcion = leerTextoNoVacio("Descripcion: ");

        Solicitud nueva = servicio.registrar(
                new DatosSolicitud(estudiante, tipo, prioridad, descripcion));
        System.out.println("Solicitud registrada con ID " + nueva.getId());
    }

    private void cambiarEstado() {
        Solicitud solicitud = servicio.obtenerSolicitud(leerNumero("ID de la solicitud: "));
        Estado nuevoEstado = leerOpcion("Nuevo estado", Estado.class);
        servicio.cambiarEstado(solicitud, nuevoEstado);
        System.out.println("Estado actualizado.");
    }

    private void asignarResponsable() {
        Solicitud solicitud = servicio.obtenerSolicitud(leerNumero("ID de la solicitud: "));
        mostrarResponsables();
        int idResponsable = leerNumero("ID del responsable: ");
        servicio.asignarResponsable(solicitud, idResponsable);
        System.out.println("Responsable asignado.");
    }

    private void buscarSolicitudes() {
        String filtro = leerTexto("Escribi un tipo o estado para filtrar: ");
        List<Solicitud> coincidencias = servicio.buscar(filtro);
        if (coincidencias.isEmpty()) {
            System.out.println("No hay coincidencias.");
            return;
        }
        for (Solicitud solicitud : coincidencias) {
            System.out.println(formateador.lineaBusqueda(solicitud));
        }
    }

    private void mostrarReporte() {
        System.out.println("\n=== REPORTE DE SOLICITUDES ===");
        List<Solicitud> todas = servicio.listarSolicitudes();
        for (Solicitud solicitud : todas) {
            System.out.println(formateador.lineaReporte(solicitud));
        }
        System.out.println("Total: " + todas.size());
        System.out.println(formateador.lineaTotales(servicio.contarPorEstado()));
    }

    // ---------- Listados ----------

    private void mostrarEstudiantes() {
        System.out.println("\nEstudiantes disponibles:");
        for (Estudiante estudiante : servicio.listarEstudiantes()) {
            System.out.println(estudiante.getId() + " - " + estudiante.getNombre());
        }
    }

    private void mostrarResponsables() {
        System.out.println("Responsables disponibles:");
        for (Responsable responsable : servicio.listarResponsables()) {
            System.out.println(responsable.getId() + " - " + responsable.getNombre());
        }
    }

    // ---------- Lectura de datos ----------

    private int leerNumero(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje);
            try {
                return Integer.parseInt(texto);
            } catch (NumberFormatException error) {
                System.out.println("Ingresa un numero valido.");
            }
        }
    }

    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return teclado.nextLine();
    }

    private String leerTextoNoVacio(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje);
            if (!texto.trim().isEmpty()) {
                return texto;
            }
            System.out.println("El dato no puede estar vacio.");
        }
    }

    /** Pide un valor de un enum y repite la pregunta hasta que el usuario ingrese uno valido. */
    private <E extends Enum<E> & ConEtiqueta> E leerOpcion(String campo, Class<E> enumerado) {
        String opciones = ConEtiqueta.opciones(enumerado);
        while (true) {
            String texto = leerTexto(campo + " (" + opciones + "): ");
            Optional<E> opcion = ConEtiqueta.buscar(enumerado, texto);
            if (opcion.isPresent()) {
                return opcion.get();
            }
            System.out.println("Valor invalido. Opciones: " + opciones + ".");
        }
    }
}
