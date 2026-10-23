package tpi.solicitudes.presentacion;

import tpi.solicitudes.dominio.Estado;
import tpi.solicitudes.dominio.Solicitud;

import java.util.Map;

/** Arma los textos que se muestran por pantalla (presentacion separada de la logica). */
public class FormateadorSolicitudes {

    /** Linea corta usada en los resultados de busqueda. */
    public String lineaBusqueda(Solicitud s) {
        return "ID: " + s.getId() + " | Estudiante: " + s.getEstudiante().getNombre()
                + " | Tipo: " + s.getTipo().etiqueta()
                + " | Estado: " + s.getEstado().etiqueta()
                + " | Responsable: " + s.nombreResponsable();
    }

    /** Linea completa usada en el reporte (incluye prioridad). */
    public String lineaReporte(Solicitud s) {
        return "ID: " + s.getId() + " | Estudiante: " + s.getEstudiante().getNombre()
                + " | Tipo: " + s.getTipo().etiqueta()
                + " | Prioridad: " + s.getPrioridad().etiqueta()
                + " | Estado: " + s.getEstado().etiqueta()
                + " | Responsable: " + s.nombreResponsable();
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
