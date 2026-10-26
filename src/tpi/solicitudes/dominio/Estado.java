package tpi.solicitudes.dominio;

public enum Estado implements ConEtiqueta {
    PENDIENTE("Pendiente", "Pendientes"),
    EN_PROCESO("En proceso", "En proceso"),
    RESUELTA("Resuelta", "Resueltas");

    private final String etiqueta;
    private final String etiquetaReporte;

    Estado(String etiqueta, String etiquetaReporte) {
        this.etiqueta = etiqueta;
        this.etiquetaReporte = etiquetaReporte;
    }

    @Override
    public String etiqueta() {
        return etiqueta;
    }

    /** Texto usado en la linea de totales del reporte (ej: "Pendientes"). */
    public String etiquetaReporte() {
        return etiquetaReporte;
    }
}
