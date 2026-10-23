/**
 * Parameter Object: agrupa los datos que el usuario ingresa al registrar una solicitud.
 * Reemplaza a los 4 parametros sueltos (estudiante, tipo, prioridad, descripcion).
 */
public class DatosSolicitud {
    private final Estudiante estudiante;
    private final String tipo;
    private final String prioridad;
    private final String descripcion;

    public DatosSolicitud(Estudiante estudiante, String tipo, String prioridad, String descripcion) {
        this.estudiante = estudiante;
        this.tipo = tipo;
        this.prioridad = prioridad;
        this.descripcion = descripcion;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public String getTipo() {
        return tipo;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
