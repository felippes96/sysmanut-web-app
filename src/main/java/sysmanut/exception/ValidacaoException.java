package sysmanut.exception;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Dados inválidos. Carrega TODAS as mensagens de erro, para que a interface
 * (Swing ou web) possa exibi-las junto de cada campo.
 */
public class ValidacaoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final ArrayList<String> erros;

    public ValidacaoException(List<String> erros) {
        super(String.join("; ", erros));
        this.erros = new ArrayList<>(erros);
    }

    public List<String> getErros() {
        return Collections.unmodifiableList(erros);
    }
}
