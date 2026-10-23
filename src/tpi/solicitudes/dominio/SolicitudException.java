package tpi.solicitudes.dominio;

/**
 * Error de negocio (ej: "No se encontro la solicitud."). El mensaje esta pensado
 * para mostrarse tal cual al usuario; la capa de consola lo imprime.
 */
public class SolicitudException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public SolicitudException(String mensaje) {
        super(mensaje);
    }
}
