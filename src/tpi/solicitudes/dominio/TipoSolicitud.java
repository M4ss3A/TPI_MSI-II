package tpi.solicitudes.dominio;

public enum TipoSolicitud implements ConEtiqueta {
    CERTIFICADO("Certificado"),
    BECA("Beca"),
    INSCRIPCION("Inscripcion");

    private final String etiqueta;

    TipoSolicitud(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    @Override
    public String etiqueta() {
        return etiqueta;
    }
}
