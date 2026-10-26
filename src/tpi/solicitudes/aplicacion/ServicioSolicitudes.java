package tpi.solicitudes.aplicacion;

import tpi.solicitudes.dominio.DatosSolicitud;
import tpi.solicitudes.dominio.Estado;
import tpi.solicitudes.dominio.Estudiante;
import tpi.solicitudes.dominio.Responsable;
import tpi.solicitudes.dominio.Solicitud;
import tpi.solicitudes.dominio.SolicitudException;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Casos de uso del sistema (registrar, cambiar estado, asignar, buscar, contar).
 * No lee del teclado ni imprime: eso lo hace la consola. Depende de interfaces,
 * no de implementaciones concretas (DIP), por lo que se puede probar sin consola.
 */
public class ServicioSolicitudes {

    private final Catalogo<Estudiante> estudiantes;
    private final Catalogo<Responsable> responsables;
    private final RepositorioSolicitudes solicitudes;

    public ServicioSolicitudes(Catalogo<Estudiante> estudiantes,
                               Catalogo<Responsable> responsables,
                               RepositorioSolicitudes solicitudes) {
        this.estudiantes = estudiantes;
        this.responsables = responsables;
        this.solicitudes = solicitudes;
    }

    // ---------- Consultas ----------

    public List<Estudiante> listarEstudiantes() {
        return estudiantes.listar();
    }

    public List<Responsable> listarResponsables() {
        return responsables.listar();
    }

    public List<Solicitud> listarSolicitudes() {
        return solicitudes.listar();
    }

    public Estudiante obtenerEstudiante(int id) {
        return estudiantes.buscarPorId(id)
                .orElseThrow(() -> new SolicitudException("No se encontro el estudiante."));
    }

    public Responsable obtenerResponsable(int id) {
        return responsables.buscarPorId(id)
                .orElseThrow(() -> new SolicitudException("No se encontro el responsable."));
    }

    public Solicitud obtenerSolicitud(int id) {
        return solicitudes.buscarPorId(id)
                .orElseThrow(() -> new SolicitudException("No se encontro la solicitud."));
    }

    public List<Solicitud> buscar(String filtro) {
        List<Solicitud> coincidencias = new ArrayList<>();
        for (Solicitud solicitud : solicitudes.listar()) {
            if (solicitud.coincideCon(filtro)) {
                coincidencias.add(solicitud);
            }
        }
        return coincidencias;
    }

    /** Cantidad de solicitudes por estado; incluye todos los estados, aunque tengan 0. */
    public Map<Estado, Integer> contarPorEstado() {
        Map<Estado, Integer> cantidades = new EnumMap<>(Estado.class);
        for (Estado estado : Estado.values()) {
            cantidades.put(estado, 0);
        }
        for (Solicitud solicitud : solicitudes.listar()) {
            cantidades.merge(solicitud.getEstado(), 1, Integer::sum);
        }
        return cantidades;
    }

    // ---------- Comandos ----------

    public Solicitud registrar(DatosSolicitud datos) {
        Solicitud nueva = new Solicitud(solicitudes.siguienteId(), datos);
        solicitudes.guardar(nueva);
        return nueva;
    }

    public void cambiarEstado(Solicitud solicitud, Estado nuevoEstado) {
        solicitud.cambiarEstado(nuevoEstado);
    }

    public void asignarResponsable(Solicitud solicitud, int idResponsable) {
        solicitud.asignarResponsable(obtenerResponsable(idResponsable));
    }
}
