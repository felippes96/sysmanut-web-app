package sysmanut.exception;

/** Falha de infraestrutura ao acessar o armazenamento. Preserva a causa original (stack trace). */
public class RepositorioException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RepositorioException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
