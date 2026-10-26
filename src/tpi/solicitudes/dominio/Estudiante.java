package tpi.solicitudes.dominio;

public class Estudiante implements Identificable {
    private final int id;
    private final String nombre;

    public Estudiante(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    @Override
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }
}
