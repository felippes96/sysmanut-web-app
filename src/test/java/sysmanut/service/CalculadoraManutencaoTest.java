package sysmanut.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import sysmanut.exception.ValidacaoException;

/** Testes unitários das regras de cálculo do valor da manutenção (sem banco de dados). */
public class CalculadoraManutencaoTest {

    private CalculadoraManutencao calc;

    @Before
    public void preparar() {
        calc = new CalculadoraManutencao();
    }

    private static BigDecimal v(String valor) {
        return new BigDecimal(valor);
    }

    // ---------------------------------------------------------------- subtotal

    @Test
    public void subtotalSomaMaoDeObraEPecas() {
        List<BigDecimal> pecas = Arrays.asList(v("50.50"), v("25.25"));
        assertEquals(v("175.75"), calc.calcularSubtotal(v("100.00"), pecas));
    }

    @Test
    public void subtotalSemPecasEIgualAMaoDeObra() {
        assertEquals(v("80.00"), calc.calcularSubtotal(v("80"), Collections.<BigDecimal>emptyList()));
    }

    @Test
    public void subtotalComListaDePecasNulaTrataComoSemPecas() {
        assertEquals(v("80.00"), calc.calcularSubtotal(v("80"), null));
    }

    @Test
    public void subtotalAceitaMaoDeObraZeroSeHouverPecas() {
        assertEquals(v("30.00"), calc.calcularSubtotal(BigDecimal.ZERO, Arrays.asList(v("10"), v("20"))));
    }

    @Test
    public void subtotalArredondaParaDuasCasasHalfUp() {
        assertEquals(v("10.01"), calc.calcularSubtotal(v("10.005"), null));
        assertEquals(v("10.00"), calc.calcularSubtotal(v("10.004"), null));
    }

    @Test
    public void subtotalRejeitaMaoDeObraNegativa() {
        assertThrows(ValidacaoException.class, () -> calc.calcularSubtotal(v("-1"), null));
    }

    @Test
    public void subtotalRejeitaMaoDeObraNula() {
        assertThrows(ValidacaoException.class, () -> calc.calcularSubtotal(null, null));
    }

    @Test
    public void subtotalRejeitaPecaNegativa() {
        assertThrows(ValidacaoException.class,
                () -> calc.calcularSubtotal(v("10"), Arrays.asList(v("5"), v("-0.01"))));
    }

    @Test
    public void subtotalRejeitaPecaNula() {
        assertThrows(ValidacaoException.class,
                () -> calc.calcularSubtotal(v("10"), Arrays.asList(v("5"), null)));
    }

    @Test
    public void subtotalAcumulaErrosDeMaoDeObraEPeca() {
        ValidacaoException e = assertThrows(ValidacaoException.class,
                () -> calc.calcularSubtotal(v("-1"), Arrays.asList(v("-2"))));
        assertEquals(2, e.getErros().size());
    }

    // ---------------------------------------------------------------- desconto

    @Test
    public void descontoDeDezPorCentoEmDuzentos() {
        assertEquals(v("180.00"), calc.aplicarDesconto(v("200"), v("10")));
    }

    @Test
    public void descontoZeroNaoAlteraOValor() {
        assertEquals(v("200.00"), calc.aplicarDesconto(v("200"), BigDecimal.ZERO));
    }

    @Test
    public void descontoDeCemPorCentoZeraOValor() {
        assertEquals(v("0.00"), calc.aplicarDesconto(v("200"), v("100")));
    }

    @Test
    public void descontoFracionarioDeDozeVirgulaCinco() {
        assertEquals(v("70.00"), calc.aplicarDesconto(v("80"), v("12.5")));
    }

    @Test
    public void descontoArredondaOValorDoDescontoHalfUp() {
        // 33,33 x 15% = 4,9995 -> 5,00 ; 33,33 - 5,00 = 28,33
        assertEquals(v("28.33"), calc.aplicarDesconto(v("33.33"), v("15")));
    }

    @Test
    public void descontoMaiorQueCemEhRejeitado() {
        assertThrows(ValidacaoException.class, () -> calc.aplicarDesconto(v("100"), v("100.01")));
    }

    @Test
    public void descontoNegativoEhRejeitado() {
        assertThrows(ValidacaoException.class, () -> calc.aplicarDesconto(v("100"), v("-5")));
    }

    @Test
    public void descontoNuloEhRejeitado() {
        assertThrows(ValidacaoException.class, () -> calc.aplicarDesconto(v("100"), null));
    }

    @Test
    public void valorNegativoParaDescontoEhRejeitado() {
        assertThrows(ValidacaoException.class, () -> calc.aplicarDesconto(v("-1"), v("10")));
    }

    // ---------------------------------------------------------------- total

    @Test
    public void totalCombinaSubtotalEDesconto() {
        // (150 + 200 + 50) = 400 ; 10% de desconto = 360
        BigDecimal total = calc.calcularTotal(v("150"), Arrays.asList(v("200"), v("50")), v("10"));
        assertEquals(v("360.00"), total);
    }

    @Test
    public void totalSemDescontoEIgualAoSubtotal() {
        assertEquals(v("125.50"), calc.calcularTotal(v("100"), Arrays.asList(v("25.50")), BigDecimal.ZERO));
    }

    @Test
    public void totalPropagaValidacaoDoSubtotal() {
        assertThrows(ValidacaoException.class, () -> calc.calcularTotal(v("-10"), null, v("5")));
    }

    @Test
    public void totalPropagaValidacaoDoDesconto() {
        assertThrows(ValidacaoException.class, () -> calc.calcularTotal(v("10"), null, v("101")));
    }

    @Test
    public void totalNuncaEhNegativo() {
        BigDecimal total = calc.calcularTotal(v("0.01"), null, v("100"));
        assertTrue(total.signum() >= 0);
    }
}
