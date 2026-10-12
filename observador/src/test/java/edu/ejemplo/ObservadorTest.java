package edu.ejemplo;
import edu.ejemplo.patron.*;
import edu.ejemplo.dominio.*;
import edu.ejemplo.presentacion.*;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ObservadorTest {
    private static final class SujetoPrueba extends SujetoBase<String> {
        void emitir(String evento) { notificar(evento); }
    }
    @Test public void suscribirEvitaDuplicados() {
        SujetoPrueba s = new SujetoPrueba(); List<String> recibidos = new ArrayList<>();
        Observador<String> o = recibidos::add;
        assertTrue(s.suscribir(o)); assertFalse(s.suscribir(o));
        s.emitir("A"); assertEquals(Arrays.asList("A"), recibidos);
    }
    @Test public void desuscribirImpideNuevasEntregas() {
        SujetoPrueba s = new SujetoPrueba(); List<String> recibidos = new ArrayList<>();
        Observador<String> o = recibidos::add; s.suscribir(o);
        assertTrue(s.desuscribir(o)); assertFalse(s.desuscribir(o));
        s.emitir("A"); assertTrue(recibidos.isEmpty());
    }
    @Test public void notificarEntregaEnOrdenATodos() {
        SujetoPrueba s = new SujetoPrueba(); List<String> recibidos = new ArrayList<>();
        s.suscribir(e -> recibidos.add("1:" + e)); s.suscribir(e -> recibidos.add("2:" + e));
        s.emitir("A"); assertEquals(Arrays.asList("1:A", "2:A"), recibidos);
    }
    @Test public void notificarSinSuscriptoresNoTieneEfectos() { new SujetoPrueba().emitir("A"); }
    @Test public void rechazaReferenciasNulas() {
        SujetoPrueba s = new SujetoPrueba();
        assertThrows(NullPointerException.class, () -> s.suscribir(null));
        assertThrows(NullPointerException.class, () -> s.desuscribir(null));
        assertThrows(NullPointerException.class, () -> s.emitir(null));
    }
    @Test public void cambioDuranteCallbackAfectaSiguienteEvento() {
        SujetoPrueba s = new SujetoPrueba(); List<String> recibidos = new ArrayList<>();
        Observador<String> segundo = e -> recibidos.add("2:" + e);
        s.suscribir(e -> { recibidos.add("1:" + e); s.desuscribir(segundo); });
        s.suscribir(segundo); s.emitir("A"); s.emitir("B");
        assertEquals(Arrays.asList("1:A", "2:A", "1:B"), recibidos);
    }
    @Test public void falloDeObservadorSePropagaYDetieneEntrega() {
        SujetoPrueba s = new SujetoPrueba(); List<String> recibidos = new ArrayList<>();
        IllegalStateException fallo = new IllegalStateException("fallo");
        s.suscribir(e -> { throw fallo; }); s.suscribir(recibidos::add);
        assertSame(fallo, assertThrows(IllegalStateException.class, () -> s.emitir("A")));
        assertTrue(recibidos.isEmpty());
    }
    @Test public void temperaturaConservaValorYLimiteFisico() {
        assertEquals(24.5, new Temperatura(24.5).getCelsius(), 0.0001);
        assertEquals(-273.15, new Temperatura(-273.15).getCelsius(), 0.0001);
    }
    @Test public void temperaturaRechazaValoresInvalidos() {
        for (double valor : new double[]{-274, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY}) {
            assertThrows(IllegalArgumentException.class, () -> new Temperatura(valor));
        }
    }
    @Test public void registrarPublicaTemperatura() {
        EstacionMeteorologica e = new EstacionMeteorologica(); List<Temperatura> r = new ArrayList<>();
        e.suscribir(r::add); e.registrar(20);
        assertEquals(1, r.size()); assertEquals(20, r.get(0).getCelsius(), 0.0001);
    }
    @Test public void registroInvalidoNoNotifica() {
        EstacionMeteorologica e = new EstacionMeteorologica(); List<Temperatura> r = new ArrayList<>();
        e.suscribir(r::add); assertThrows(IllegalArgumentException.class, () -> e.registrar(-300));
        assertTrue(r.isEmpty());
    }
    @Test public void pantallaEscribeEnSalidaEstandar() throws Exception {
        PrintStream original = System.out; ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try (PrintStream capturada = new PrintStream(buffer, true, "UTF-8")) {
            System.setOut(capturada); new PantallaConsola().actualizar(new Temperatura(24.5));
            assertEquals("Temperatura: 24.5 °C" + System.lineSeparator(), buffer.toString("UTF-8"));
        } finally { System.setOut(original); }
    }
    @Test public void pantallaRechazaEventoNulo() {
        assertThrows(NullPointerException.class, () -> new PantallaConsola().actualizar(null));
    }
    @Test public void integracionEstacionPantallaYDesuscripcion() throws Exception {
        PrintStream original = System.out; ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try (PrintStream capturada = new PrintStream(buffer, true, "UTF-8")) {
            System.setOut(capturada); EstacionMeteorologica e = new EstacionMeteorologica();
            PantallaConsola p = new PantallaConsola(); e.suscribir(p); e.registrar(18);
            e.desuscribir(p); e.registrar(19);
            assertEquals("Temperatura: 18.0 °C" + System.lineSeparator(), buffer.toString("UTF-8"));
        } finally { System.setOut(original); }
    }
}
