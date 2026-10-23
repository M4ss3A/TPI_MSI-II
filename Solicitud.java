public class Solicitud { //Falta de encapsulamiento
    public int id;
    public Estudiante estudiante;
    public String tipo; //Primitive Obsession
    public String prioridad; //Primitive Obsession
    public String descripcion; //Primitive Obsession
    public String estado;
    public Responsable responsable;

    public Solicitud(int id, Estudiante estudiante, String tipo, String prioridad,
                    String descripcion, String estado, Responsable responsable) { //Long Parameter List
        this.id = id;
        this.estudiante = estudiante;
        this.tipo = tipo;
        this.prioridad = prioridad;
        this.descripcion = descripcion;
        this.estado = estado;
        this.responsable = responsable;
    }
}
