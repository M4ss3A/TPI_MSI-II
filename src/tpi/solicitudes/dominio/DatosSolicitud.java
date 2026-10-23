package tpi.solicitudes.dominio;

/**
 * Parameter Object: agrupa los datos que se necesitan para registrar una solicitud.
 * Valida sus propios datos: no se puede construir un DatosSolicitud incompleto.
 */
public class DatosSolicitud {
    private final Estudiante estudiante;
    private final TipoSolicitud tipo;
    private final Prioridad prioridad;
    private final String descripcion;

    public DatosSolicitud(Estudiante estudiante, TipoSolicitud tipo, Prioridad prioridad,
                          String descripcion) {
        if (estudiante == null || tipo == null || prioridad == null) {
            throw new SolicitudException("Faltan datos obligatorios de la solicitud.");
        }
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new SolicitudException("La descripcion no puede estar vacia.");
        }
        this.estudiante = estudiante;
        this.tipo = tipo;
        this.prioridad = prioridad;
        this.descripcion = descripcion;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public TipoSolicitud getTipo() {
        return tipo;
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
