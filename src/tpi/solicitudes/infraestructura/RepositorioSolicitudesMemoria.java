package tpi.solicitudes.infraestructura;

import tpi.solicitudes.aplicacion.RepositorioSolicitudes;
import tpi.solicitudes.dominio.Solicitud;

import java.util.ArrayList;

/**
 * Guarda las solicitudes solo en memoria (se pierden al cerrar el programa).
 * Si en el futuro se usa archivo o base de datos, se crea otra clase que implemente
 * RepositorioSolicitudes y el resto del sistema no cambia (DIP / OCP).
 */
public class RepositorioSolicitudesMemoria extends CatalogoMemoria<Solicitud>
        implements RepositorioSolicitudes {

    private int proximoId = 1;

    public RepositorioSolicitudesMemoria() {
        super(new ArrayList<Solicitud>());
    }

    @Override
    public int siguienteId() {
        return proximoId++;
    }

    @Override
    public void guardar(Solicitud solicitud) {
        agregar(solicitud);
    }
}
