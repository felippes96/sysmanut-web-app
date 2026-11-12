package sysmanut.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import org.junit.Test;
import sysmanut.exception.ValidacaoException;

/** Testes unitários da regra de garantia. A data "hoje" é sempre informada, então os testes são determinísticos. */
public class RegraGarantiaTest {

    private final RegraGarantia padrao = new RegraGarantia();

    @Test
    public void garantiaPadraoEhDeNoventaDias() {
        assertEquals(LocalDate.of(2026, 4, 1), padrao.ultimoDiaDaGarantia(LocalDate.of(2026, 1, 1)));
    }

    @Test
    public void garantiaConsideraAnoBissexto() {
        assertEquals(LocalDate.of(2024, 3, 31), padrao.ultimoDiaDaGarantia(LocalDate.of(2024, 1, 1)));
    }

    @Test
    public void garantiaAtravessaViradaDeAno() {
        assertEquals(LocalDate.of(2027, 1, 29), padrao.ultimoDiaDaGarantia(LocalDate.of(2026, 10, 31)));
    }

    @Test
    public void estaEmGarantiaNoDiaDaConclusao() {
        LocalDate conclusao = LocalDate.of(2026, 1, 1);
        assertTrue(padrao.estaEmGarantia(conclusao, conclusao));
    }

    @Test
    public void estaEmGarantiaNoUltimoDiaInclusive() {
        assertTrue(padrao.estaEmGarantia(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 4, 1)));
    }

    @Test
    public void naoEstaEmGarantiaUmDiaDepois() {
        assertFalse(padrao.estaEmGarantia(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 4, 2)));
    }

    @Test
    public void garantiaPersonalizadaDeTrintaDias() {
        RegraGarantia trinta = new RegraGarantia(30);
        assertEquals(LocalDate.of(2026, 1, 31), trinta.ultimoDiaDaGarantia(LocalDate.of(2026, 1, 1)));
    }

    @Test
    public void garantiaZeroCobreSomenteODiaDaConclusao() {
        RegraGarantia zero = new RegraGarantia(0);
        LocalDate dia = LocalDate.of(2026, 5, 10);
        assertTrue(zero.estaEmGarantia(dia, dia));
        assertFalse(zero.estaEmGarantia(dia, dia.plusDays(1)));
    }

    @Test
    public void diasNegativosSaoRejeitados() {
        assertThrows(ValidacaoException.class, () -> new RegraGarantia(-1));
    }

    @Test
    public void dataNulaEhRejeitada() {
        assertThrows(ValidacaoException.class, () -> padrao.ultimoDiaDaGarantia(null));
        assertThrows(ValidacaoException.class, () -> padrao.estaEmGarantia(LocalDate.of(2026, 1, 1), null));
    }
}
