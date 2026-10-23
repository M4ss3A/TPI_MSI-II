package tpi.solicitudes.dominio;

public class Responsable implements Identificable {
    private final int id;
    private final String nombre;

    public Responsable(int id, String nombre) {
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
