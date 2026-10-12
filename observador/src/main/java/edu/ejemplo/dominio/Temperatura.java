package edu.ejemplo.dominio;
public final class Temperatura {
    private final double celsius;
    public Temperatura(double celsius) {
        if (!Double.isFinite(celsius) || celsius < -273.15) {
            throw new IllegalArgumentException("Temperatura inválida");
        }
        this.celsius = celsius;
    }
    public double getCelsius() { return celsius; }
}
