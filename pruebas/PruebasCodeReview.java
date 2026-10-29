import tpi.solicitudes.aplicacion.*;
import tpi.solicitudes.dominio.*;
import tpi.solicitudes.infraestructura.*;
import tpi.solicitudes.presentacion.*;
import java.io.*;
import java.util.*;

/** Verifica las correcciones aplicadas en la etapa 7 (code review). Sin dependencias externas. */
public class PruebasCodeReview {
    static int ok = 0, fallos = 0;

    static void verificar(String nombre, boolean condicion) {
        if (condicion) { ok++; System.out.println("[OK]    " + nombre); }
        else { fallos++; System.out.println("[FALLO] " + nombre); }
    }

    static ServicioSolicitudes nuevoServicio() {
        return new ServicioSolicitudes(
            new CatalogoMemoria<>(Arrays.asList(new Estudiante(1, "Ana Gonzales"))),
            new CatalogoMemoria<>(Arrays.asList(new Responsable(1, "Secretaria Academica"))),
            new RepositorioSolicitudesMemoria());
    }

    public static void main(String[] args) {
        // CR-01: las etiquetas toleran espacios al inicio y al final
        verificar("CR-01 ConEtiqueta.buscar ignora espacios ('Alta ')",
            ConEtiqueta.buscar(Prioridad.class, "  Alta ").isPresent());
        verificar("CR-02 ConEtiqueta.buscar con null no falla",
            !ConEtiqueta.buscar(Prioridad.class, null).isPresent());
        ServicioSolicitudes s = nuevoServicio();
        s.registrar(new DatosSolicitud(s.obtenerEstudiante(1), TipoSolicitud.BECA, Prioridad.MEDIA, "Beca"));
        verificar("CR-03 buscar(' beca ') encuentra la solicitud", s.buscar(" beca ").size() == 1);

        // CR-04: la consola no se rompe si la entrada se cierra antes de elegir Salir
        PrintStream original = System.out;
        ByteArrayOutputStream captura = new ByteArrayOutputStream();
        boolean sinExcepcion = true;
        try {
            System.setOut(new PrintStream(captura));
            new ConsolaSolicitudes(nuevoServicio(), new FormateadorSolicitudes(), new Scanner("5\n")).ejecutar();
        } catch (RuntimeException e) {
            sinExcepcion = false;
        } finally {
            System.setOut(original);
        }
        verificar("CR-04 consola termina limpia ante fin de entrada", sinExcepcion
            && captura.toString().contains("Entrada finalizada"));

        System.out.println("\nResultado: " + ok + " OK, " + fallos + " fallos");
        System.exit(fallos == 0 ? 0 : 1);
    }
}
