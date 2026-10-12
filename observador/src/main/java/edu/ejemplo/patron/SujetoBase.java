package edu.ejemplo.patron;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
/** Registro y entrega sincrónica; no ofrece seguridad entre hilos. */
public abstract class SujetoBase<T> implements Sujeto<T> {
    private final Set<Observador<T>> observadores = new LinkedHashSet<>();
    @Override public final boolean suscribir(Observador<T> observador) {
        return observadores.add(Objects.requireNonNull(observador, "observador"));
    }
    @Override public final boolean desuscribir(Observador<T> observador) {
        return observadores.remove(Objects.requireNonNull(observador, "observador"));
    }
    protected final void notificar(T evento) {
        Objects.requireNonNull(evento, "evento");
        // La copia permite cambiar suscripciones dentro de un callback.
        for (Observador<T> observador : new ArrayList<>(observadores)) {
            observador.actualizar(evento);
        }
    }
}
