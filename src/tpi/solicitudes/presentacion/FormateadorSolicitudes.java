package tpi.solicitudes.presentacion;

import tpi.solicitudes.dominio.Estado;
import tpi.solicitudes.dominio.Solicitud;

import java.util.Map;

/** Arma los textos que se muestran por pantalla (presentación separada de la lógica). */
public class FormateadorSolicitudes {

    /** Linea corta usada en los resultados de búsqueda. */
    public String lineaBusqueda(Solicitud solicitud) {
        return "ID: " + solicitud.getId() + " | Estudiante: " + solicitud.getEstudiante().getNombre()
                + " | Tipo: " + solicitud.getTipo().etiqueta()
                + " | Estado: " + solicitud.getEstado().etiqueta()
                + " | Responsable: " + solicitud.nombreResponsable();
    }

    /** Linea completa usada en el reporte (incluye prioridad). */
    public String lineaReporte(Solicitud solicitud) {
        return "ID: " + solicitud.getId() + " | Estudiante: " + solicitud.getEstudiante().getNombre()
                + " | Tipo: " + solicitud.getTipo().etiqueta()
                + " | Prioridad: " + solicitud.getPrioridad().etiqueta()
                + " | Estado: " + solicitud.getEstado().etiqueta()
                + " | Responsable: " + solicitud.nombreResponsable();
    }

    /** Ej: "Pendientes: 1 | En proceso: 0 | Resueltas: 2". Incluye cualquier Estado nuevo. */
    public String lineaTotales(Map<Estado, Integer> cantidades) {
        StringBuilder linea = new StringBuilder();
        for (Map.Entry<Estado, Integer> cantidad : cantidades.entrySet()) {
            if (linea.length() > 0) {
                linea.append(" | ");
            }
            linea.append(cantidad.getKey().etiquetaReporte()).append(": ")
                    .append(cantidad.getValue());
        }
        return linea.toString();
    }
}