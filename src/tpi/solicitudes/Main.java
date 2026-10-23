package tpi.solicitudes;

import tpi.solicitudes.aplicacion.Catalogo;
import tpi.solicitudes.aplicacion.RepositorioSolicitudes;
import tpi.solicitudes.aplicacion.ServicioSolicitudes;
import tpi.solicitudes.dominio.Estudiante;
import tpi.solicitudes.dominio.Responsable;
import tpi.solicitudes.infraestructura.CatalogoMemoria;
import tpi.solicitudes.infraestructura.RepositorioSolicitudesMemoria;
import tpi.solicitudes.presentacion.ConsolaSolicitudes;
import tpi.solicitudes.presentacion.FormateadorSolicitudes;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Punto de entrada y "armado" del sistema: crea los objetos y los conecta entre si.
 * No contiene logica de negocio ni de pantalla.
 */
public class Main {

    public static void main(String[] args) {
        Catalogo<Estudiante> estudiantes = new CatalogoMemoria<>(Arrays.asList(
                new Estudiante(1, "Ana Gonzales"),
                new Estudiante(2, "Pablo Gomez"),
                new Estudiante(3, "Lucia Martinez")));
        Catalogo<Responsable> responsables = new CatalogoMemoria<>(Arrays.asList(
                new Responsable(1, "Secretaria Academica"),
                new Responsable(2, "Area de Alumnos")));
        RepositorioSolicitudes solicitudes = new RepositorioSolicitudesMemoria();

        ServicioSolicitudes servicio =
                new ServicioSolicitudes(estudiantes, responsables, solicitudes);
        ConsolaSolicitudes consola = new ConsolaSolicitudes(
                servicio, new FormateadorSolicitudes(), new Scanner(System.in));
        consola.ejecutar();
    }
}
