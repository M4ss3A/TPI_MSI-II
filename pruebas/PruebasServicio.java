import tpi.solicitudes.aplicacion.*;
import tpi.solicitudes.dominio.*;
import tpi.solicitudes.infraestructura.*;
import java.util.*;

/** Pruebas unitarias del ServicioSolicitudes (sin consola ni teclado). Sin dependencias externas. */
public class PruebasServicio {
    static int ok = 0, fallos = 0;

    static void verificar(String nombre, boolean condicion) {
        if (condicion) { ok++; System.out.println("[OK]    " + nombre); }
        else { fallos++; System.out.println("[FALLO] " + nombre); }
    }

    static boolean lanza(Runnable accion, String mensajeEsperado) {
        try { accion.run(); return false; }
        catch (SolicitudException e) { return e.getMessage().equals(mensajeEsperado); }
    }

    static ServicioSolicitudes nuevoServicio() {
        return new ServicioSolicitudes(
            new CatalogoMemoria<>(Arrays.asList(new Estudiante(1, "Ana Gonzales"), new Estudiante(2, "Pablo Gomez"))),
            new CatalogoMemoria<>(Arrays.asList(new Responsable(1, "Secretaria Academica"))),
            new RepositorioSolicitudesMemoria());
    }

    public static void main(String[] args) {
        ServicioSolicitudes s = nuevoServicio();
        Estudiante ana = s.obtenerEstudiante(1);
        Solicitud a = s.registrar(new DatosSolicitud(ana, TipoSolicitud.CERTIFICADO, Prioridad.ALTA, "Certificado regular"));
        verificar("TU-01 registrar: asigna ID 1", a.getId() == 1);
        verificar("TU-02 registrar: estado inicial Pendiente", a.getEstado() == Estado.PENDIENTE);
        verificar("TU-03 registrar: sin responsable ('Sin asignar')", a.nombreResponsable().equals("Sin asignar"));
        Solicitud b = s.registrar(new DatosSolicitud(s.obtenerEstudiante(2), TipoSolicitud.BECA, Prioridad.MEDIA, "Beca"));
        verificar("TU-04 registrar: IDs consecutivos", b.getId() == 2);
        s.cambiarEstado(a, Estado.EN_PROCESO);
        verificar("TU-05 cambiarEstado: pasa a En proceso", a.getEstado() == Estado.EN_PROCESO);
        s.asignarResponsable(a, 1);
        verificar("TU-06 asignarResponsable: queda asignado", a.nombreResponsable().equals("Secretaria Academica"));
        verificar("TU-07 asignarResponsable inexistente: error de negocio",
            lanza(() -> s.asignarResponsable(b, 99), "No se encontro el responsable."));
        verificar("TU-08 obtenerSolicitud inexistente: error de negocio",
            lanza(() -> s.obtenerSolicitud(99), "No se encontro la solicitud."));
        verificar("TU-09 obtenerEstudiante inexistente: error de negocio",
            lanza(() -> s.obtenerEstudiante(99), "No se encontro el estudiante."));
        verificar("TU-10 buscar por tipo (sin distinguir mayusculas)", s.buscar("certificado").size() == 1);
        verificar("TU-11 buscar por estado", s.buscar("Pendiente").size() == 1 && s.buscar("Pendiente").get(0).getId() == 2);
        verificar("TU-12 buscar sin coincidencias: lista vacia", s.buscar("Inexistente").isEmpty());
        Map<Estado, Integer> c = s.contarPorEstado();
        verificar("TU-13 contarPorEstado: 1 pendiente, 1 en proceso, 0 resueltas",
            c.get(Estado.PENDIENTE) == 1 && c.get(Estado.EN_PROCESO) == 1 && c.get(Estado.RESUELTA) == 0);
        verificar("TU-14 contarPorEstado: incluye los 3 estados", c.size() == Estado.values().length);
        verificar("TU-15 DatosSolicitud: descripcion vacia rechazada",
            lanza(() -> new DatosSolicitud(ana, TipoSolicitud.BECA, Prioridad.BAJA, "  "), "La descripcion no puede estar vacia."));
        verificar("TU-16 DatosSolicitud: tipo nulo rechazado",
            lanza(() -> new DatosSolicitud(ana, null, Prioridad.BAJA, "x"), "Faltan datos obligatorios de la solicitud."));
        verificar("TU-17 listarSolicitudes: no modificable desde afuera", noModificable(s));
        System.out.println("\nResultado: " + ok + " OK, " + fallos + " fallos");
        System.exit(fallos == 0 ? 0 : 1);
    }

    static boolean noModificable(ServicioSolicitudes s) {
        try { s.listarSolicitudes().clear(); return false; }
        catch (UnsupportedOperationException e) { return true; }
    }
}
