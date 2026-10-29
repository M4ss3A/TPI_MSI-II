package tpi.solicitudes.dominio;

public class Solicitud implements Identificable {
    private final int id;
    private final Estudiante estudiante;
    private final TipoSolicitud tipo;
    private final Prioridad prioridad;
    private final String descripcion;
    private Estado estado;
    private Responsable responsable;

    // Una solicitud nueva siempre nace "Pendiente" y sin responsable:
    // ya no hace falta que quien la crea pase esos dos valores.
    public Solicitud(int id, DatosSolicitud datos) {
        this.id = id;
        this.estudiante = datos.getEstudiante();
        this.tipo = datos.getTipo();
        this.prioridad = datos.getPrioridad();
        this.descripcion = datos.getDescripcion();
        this.estado = Estado.PENDIENTE;
        this.responsable = null;
    }

    @Override
    public int getId() {
        return id;
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

    public Estado getEstado() {
        return estado;
    }

    public Responsable getResponsable() {
        return responsable;
    }

    // ---------- Comportamiento movido desde Main (Move Method) ----------

    public void cambiarEstado(Estado nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public void asignarResponsable(Responsable nuevoResponsable) {
        this.responsable = nuevoResponsable;
    }

    public String nombreResponsable() {
        return responsable == null ? "Sin asignar" : responsable.getNombre();
    }

    public boolean coincideCon(String filtro) {
        return tipo.coincideCon(filtro) || estado.coincideCon(filtro);
    }
}
