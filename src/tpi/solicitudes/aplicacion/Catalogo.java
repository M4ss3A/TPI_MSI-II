package tpi.solicitudes.aplicacion;

import tpi.solicitudes.dominio.Identificable;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz de SOLO LECTURA (ISP): quien solo necesita consultar estudiantes o
 * responsables no esta obligado a depender de metodos para guardar.
 */
public interface Catalogo<T extends Identificable> {

    List<T> listar();

    Optional<T> buscarPorId(int id);
}
