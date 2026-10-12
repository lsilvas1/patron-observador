package edu.ejemplo.app;
import edu.ejemplo.dominio.EstacionMeteorologica;
import edu.ejemplo.presentacion.PantallaConsola;
public final class Main {
    private Main() { }
    public static void main(String[] args) {
        EstacionMeteorologica estacion = new EstacionMeteorologica();
        PantallaConsola pantalla = new PantallaConsola();
        estacion.suscribir(pantalla);
        estacion.registrar(24.5);
        estacion.desuscribir(pantalla);
        estacion.registrar(25.0);
    }
}
