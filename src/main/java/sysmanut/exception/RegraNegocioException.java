package sysmanut.exception;

/** Violação de regra de negócio (ex.: cliente inexistente, login duplicado, credenciais inválidas). */
public class RegraNegocioException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
