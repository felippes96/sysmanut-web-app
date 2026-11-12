package sysmanut.security;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * PBKDF2-HMAC-SHA256 com salt aleatório, usando apenas a JDK (sem bibliotecas extras).
 * Formato armazenado: {@code iteracoes:salt(base64):hash(base64)}.
 */
public class Pbkdf2PasswordHasher implements PasswordHasher {

    public static final int ITERACOES_PADRAO = 600_000;

    private static final String ALGORITMO = "PBKDF2WithHmacSHA256";
    private static final int BYTES_SALT = 16;
    private static final int BITS_HASH = 256;

    private final SecureRandom aleatorio = new SecureRandom();
    private final int iteracoes;

    public Pbkdf2PasswordHasher() {
        this(ITERACOES_PADRAO);
    }

    public Pbkdf2PasswordHasher(int iteracoes) {
        this.iteracoes = iteracoes;
    }

    @Override
    public String gerarHash(String senha) {
        byte[] salt = new byte[BYTES_SALT];
        aleatorio.nextBytes(salt);
        byte[] hash = derivar(senha, salt, iteracoes);
        return iteracoes + ":" + Base64.getEncoder().encodeToString(salt)
                + ":" + Base64.getEncoder().encodeToString(hash);
    }

    @Override
    public boolean confere(String senha, String hashArmazenado) {
        if (senha == null || hashArmazenado == null) {
            return false;
        }
        String[] partes = hashArmazenado.split(":");
        if (partes.length != 3) {
            return false;
        }
        try {
            int it = Integer.parseInt(partes[0]);
            byte[] salt = Base64.getDecoder().decode(partes[1]);
            byte[] esperado = Base64.getDecoder().decode(partes[2]);
            return MessageDigest.isEqual(esperado, derivar(senha, salt, it));
        } catch (IllegalArgumentException e) {
            return false; // formato corrompido
        }
    }

    private static byte[] derivar(String senha, byte[] salt, int iteracoes) {
        PBEKeySpec spec = new PBEKeySpec(senha.toCharArray(), salt, iteracoes, BITS_HASH);
        try {
            return SecretKeyFactory.getInstance(ALGORITMO).generateSecret(spec).getEncoded();
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Algoritmo " + ALGORITMO + " indisponível", e);
        } finally {
            spec.clearPassword();
        }
    }
}
