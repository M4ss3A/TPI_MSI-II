public class Solicitud {
    public int id;
    public Estudiante estudiante;
    public String tipo;
    public String prioridad;
    public String descripcion;
    public String estado;
    public Responsable responsable;

    public Solicitud(int id, Estudiante estudiante, String tipo, String prioridad,
                    String descripcion, String estado, Responsable responsable) {
        this.id = id;
        this.estudiante = estudiante;
        this.tipo = tipo;
        this.prioridad = prioridad;
        this.descripcion = descripcion;
        this.estado = estado;
        this.responsable = responsable;
    }
}
