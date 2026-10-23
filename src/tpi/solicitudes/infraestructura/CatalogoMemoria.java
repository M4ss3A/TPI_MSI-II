package tpi.solicitudes.infraestructura;

import tpi.solicitudes.aplicacion.Catalogo;
import tpi.solicitudes.dominio.Identificable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/** Implementacion en memoria de Catalogo: reemplaza a las listas estaticas de Main. */
public class CatalogoMemoria<T extends Identificable> implements Catalogo<T> {

    private final List<T> elementos;

    public CatalogoMemoria(List<T> elementosIniciales) {
        this.elementos = new ArrayList<>(elementosIniciales);
    }

    @Override
    public List<T> listar() {
        return Collections.unmodifiableList(elementos);
    }

    @Override
    public Optional<T> buscarPorId(int id) {
        for (T elemento : elementos) {
            if (elemento.getId() == id) {
                return Optional.of(elemento);
            }
        }
        return Optional.empty();
    }

    protected void agregar(T elemento) {
        elementos.add(elemento);
    }
}
