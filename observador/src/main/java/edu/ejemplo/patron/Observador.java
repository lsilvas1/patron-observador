package edu.ejemplo.patron;
@FunctionalInterface
public interface Observador<T> { void actualizar(T evento); }
