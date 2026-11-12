package sysmanut.security;

/** Contrato para proteger senhas. Permite trocar o algoritmo sem alterar AutenticacaoService. */
public interface PasswordHasher {

    String gerarHash(String senha);

    boolean confere(String senha, String hashArmazenado);
}
