package edu.ejemplo.dominio;
import edu.ejemplo.patron.SujetoBase;
public final class EstacionMeteorologica extends SujetoBase<Temperatura> {
    public void registrar(double celsius) {
        notificar(new Temperatura(celsius));
    }
}
