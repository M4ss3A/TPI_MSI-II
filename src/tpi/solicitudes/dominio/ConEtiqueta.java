package tpi.solicitudes.dominio;

import java.util.Optional;

/**
 * Contrato comun de los enums del dominio (Estado, TipoSolicitud, Prioridad):
 * cada valor tiene un texto legible ("etiqueta") con el que el usuario lo escribe y lo ve.
 */
public interface ConEtiqueta {

    String etiqueta();

    default boolean coincideCon(String texto) {
        return texto != null && etiqueta().equalsIgnoreCase(texto.trim());
    }

    /** Busca el valor del enum cuya etiqueta coincide (sin distinguir mayusculas). */
    static <E extends Enum<E> & ConEtiqueta> Optional<E> buscar(Class<E> enumerado, String texto) {
        for (E valor : enumerado.getEnumConstants()) {
            if (valor.coincideCon(texto)) {
                return Optional.of(valor);
            }
        }
        return Optional.empty();
    }

    /** Devuelve las opciones validas separadas por "/", ej: "Alta/Media/Baja". */
    static <E extends Enum<E> & ConEtiqueta> String opciones(Class<E> enumerado) {
        StringBuilder texto = new StringBuilder();
        for (E valor : enumerado.getEnumConstants()) {
            if (texto.length() > 0) {
                texto.append("/");
            }
            texto.append(valor.etiqueta());
        }
        return texto.toString();
    }
}
