package sysmanut.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import sysmanut.exception.ValidacaoException;

/**
 * Regras de cálculo do valor de uma manutenção.
 *
 * Classe sem estado e sem acesso a banco: recebe valores e devolve valores,
 * por isso é a candidata ideal a testes unitários.
 *
 * total = (mão de obra + soma das peças) - desconto percentual, arredondado em 2 casas (HALF_UP).
 */
public class CalculadoraManutencao {

    public static final BigDecimal CEM = new BigDecimal("100");
    private static final int CASAS = 2;

    /** Soma a mão de obra com o valor de todas as peças. */
    public BigDecimal calcularSubtotal(BigDecimal maoDeObra, List<BigDecimal> pecas) {
        List<String> erros = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;

        if (maoDeObra == null || maoDeObra.signum() < 0) {
            erros.add("A mão de obra deve ser informada e não pode ser negativa.");
        } else {
            subtotal = subtotal.add(maoDeObra);
        }

        if (pecas != null) {
            for (BigDecimal peca : pecas) {
                if (peca == null || peca.signum() < 0) {
                    erros.add("O valor de cada peça deve ser informado e não pode ser negativo.");
                    break;
                }
                subtotal = subtotal.add(peca);
            }
        }

        if (!erros.isEmpty()) {
            throw new ValidacaoException(erros);
        }
        return subtotal.setScale(CASAS, RoundingMode.HALF_UP);
    }

    /** Aplica um desconto percentual (0 a 100) sobre um valor. */
    public BigDecimal aplicarDesconto(BigDecimal valor, BigDecimal percentual) {
        List<String> erros = new ArrayList<>();
        if (valor == null || valor.signum() < 0) {
            erros.add("O valor deve ser informado e não pode ser negativo.");
        }
        if (percentual == null || percentual.signum() < 0 || percentual.compareTo(CEM) > 0) {
            erros.add("O desconto deve estar entre 0% e 100%.");
        }
        if (!erros.isEmpty()) {
            throw new ValidacaoException(erros);
        }
        BigDecimal desconto = valor.multiply(percentual).divide(CEM, CASAS, RoundingMode.HALF_UP);
        return valor.subtract(desconto).setScale(CASAS, RoundingMode.HALF_UP);
    }

    /** Valor final cobrado do cliente. */
    public BigDecimal calcularTotal(BigDecimal maoDeObra, List<BigDecimal> pecas, BigDecimal descontoPercentual) {
        BigDecimal subtotal = calcularSubtotal(maoDeObra, pecas);
        return aplicarDesconto(subtotal, descontoPercentual);
    }
}
