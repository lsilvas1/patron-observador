package edu.ejemplo.presentacion;
import edu.ejemplo.patron.Observador;
import edu.ejemplo.dominio.Temperatura;
import java.util.Locale;
import java.util.Objects;
public final class PantallaConsola implements Observador<Temperatura> {
    @Override public void actualizar(Temperatura evento) {
        Objects.requireNonNull(evento, "evento");
        System.out.printf(Locale.ROOT, "Temperatura: %.1f °C%n", evento.getCelsius());
    }
}
