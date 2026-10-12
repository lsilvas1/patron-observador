package edu.ejemplo.patron;
public interface Sujeto<T> {
    boolean suscribir(Observador<T> observador);
    boolean desuscribir(Observador<T> observador);
}
