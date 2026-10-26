package tpi.solicitudes.aplicacion;

import tpi.solicitudes.dominio.Solicitud;

/** Contrato de almacenamiento de solicitudes: lectura (heredada de Catalogo) + escritura. */
public interface RepositorioSolicitudes extends Catalogo<Solicitud> {

    /** Entrega un ID que todavia no fue usado por ninguna solicitud. */
    int siguienteId();

    void guardar(Solicitud solicitud);
}
