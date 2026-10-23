public class Solicitud {
    private final int id;
    private final Estudiante estudiante;
    private final String tipo;
    private final String prioridad;
    private final String descripcion;
    private String estado;
    private Responsable responsable;

    // Una solicitud nueva siempre nace "Pendiente" y sin responsable:
    // ya no hace falta que quien la crea pase esos dos valores.
    public Solicitud(int id, DatosSolicitud datos) {
        this.id = id;
        this.estudiante = datos.getEstudiante();
        this.tipo = datos.getTipo();
        this.prioridad = datos.getPrioridad();
        this.descripcion = datos.getDescripcion();
        this.estado = "Pendiente";
        this.responsable = null;
    }

    public int getId() {
        return id;
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

    public String getEstado() {
        return estado;
    }

    public Responsable getResponsable() {
        return responsable;
    }

    // ---------- Comportamiento movido desde Main (Move Method) ----------

    public void cambiarEstado(String nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public void asignarResponsable(Responsable nuevoResponsable) {
        this.responsable = nuevoResponsable;
    }

    public String nombreResponsable() {
        return responsable == null ? "Sin asignar" : responsable.getNombre();
    }

    public boolean estaEn(String estadoBuscado) {
        return estado.equalsIgnoreCase(estadoBuscado);
    }

    public boolean coincideCon(String filtro) {
        return tipo.equalsIgnoreCase(filtro) || estaEn(filtro);
    }
}
