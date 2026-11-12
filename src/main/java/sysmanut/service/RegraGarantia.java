package sysmanut.service;

import java.time.LocalDate;
import sysmanut.exception.ValidacaoException;
import java.util.Collections;

/**
 * Regra de garantia do serviço de manutenção.
 * A data de "hoje" é recebida como parâmetro (e não obtida com LocalDate.now())
 * para que a regra seja determinística e testável.
 */
public class RegraGarantia {

    public static final int DIAS_PADRAO = 90;

    private final int diasDeGarantia;

    public RegraGarantia() {
        this(DIAS_PADRAO);
    }

    public RegraGarantia(int diasDeGarantia) {
        if (diasDeGarantia < 0) {
            throw new ValidacaoException(Collections.singletonList("Os dias de garantia não podem ser negativos."));
        }
        this.diasDeGarantia = diasDeGarantia;
    }

    /** Último dia em que o serviço ainda está coberto pela garantia (inclusive). */
    public LocalDate ultimoDiaDaGarantia(LocalDate dataConclusao) {
        exigirData(dataConclusao);
        return dataConclusao.plusDays(diasDeGarantia);
    }

    public boolean estaEmGarantia(LocalDate dataConclusao, LocalDate hoje) {
        exigirData(hoje);
        return !hoje.isAfter(ultimoDiaDaGarantia(dataConclusao));
    }

    private static void exigirData(LocalDate data) {
        if (data == null) {
            throw new ValidacaoException(Collections.singletonList("A data deve ser informada."));
        }
    }
}
