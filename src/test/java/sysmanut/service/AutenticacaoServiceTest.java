package sysmanut.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Before;
import org.junit.Test;
import sysmanut.exception.RegraNegocioException;
import sysmanut.exception.ValidacaoException;
import sysmanut.model.Usuario;
import sysmanut.repository.memoria.UsuarioRepositoryMemoria;
import sysmanut.security.Pbkdf2PasswordHasher;
import sysmanut.validation.UsuarioValidador;

public class AutenticacaoServiceTest {

    private AutenticacaoService auth;

    @Before
    public void preparar() {
        // 1.000 iterações: rápido nos testes (o padrão de produção é 600.000)
        auth = new AutenticacaoService(new UsuarioRepositoryMemoria(),
                new Pbkdf2PasswordHasher(1_000), new UsuarioValidador());
    }

    @Test
    public void senhaNaoEhGuardadaEmTextoPuro() {
        Usuario u = auth.registrar("Administrador", "admin", "senha123", "ADMIN");
        assertNotEquals("senha123", u.getSenhaHash());
    }

    @Test
    public void loginCorretoAutentica() {
        auth.registrar("Administrador", "admin", "senha123", "ADMIN");
        assertEquals("admin", auth.autenticar("admin", "senha123").getLogin());
    }

    @Test
    public void senhaErradaELoginInexistenteTemMesmaMensagem() {
        auth.registrar("Administrador", "admin", "senha123", "ADMIN");
        RegraNegocioException e1 = assertThrows(RegraNegocioException.class, () -> auth.autenticar("admin", "errada"));
        RegraNegocioException e2 = assertThrows(RegraNegocioException.class, () -> auth.autenticar("fantasma", "senha123"));
        assertEquals(e1.getMessage(), e2.getMessage());
    }

    @Test
    public void loginDuplicadoEhRejeitado() {
        auth.registrar("A", "admin", "senha123", "ADMIN");
        assertThrows(RegraNegocioException.class, () -> auth.registrar("B", "admin", "outrasenha", "USER"));
    }

    @Test
    public void senhaCurtaEhRejeitada() {
        assertThrows(ValidacaoException.class, () -> auth.registrar("Beto", "beto", "123", "USER"));
    }
}
