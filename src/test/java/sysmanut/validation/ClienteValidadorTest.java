package sysmanut.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import sysmanut.exception.ValidacaoException;
import sysmanut.model.Cliente;

public class ClienteValidadorTest {

    private final ClienteValidador validador = new ClienteValidador();

    @Test
    public void clienteValidoEhAceito() {
        validador.validar(new Cliente("Ana", "5399999-0000", "ana@email.com", "Rua A, 10"));
    }

    @Test
    public void clienteVazioAcumulaQuatroErros() {
        ValidacaoException e = assertThrows(ValidacaoException.class,
                () -> validador.validar(new Cliente("", "  ", null, "")));
        assertEquals(4, e.getErros().size());
    }

    @Test
    public void nomeSoComEspacosEhRejeitado() {
        ValidacaoException e = assertThrows(ValidacaoException.class,
                () -> validador.validar(new Cliente("   ", "1", "a@b.com", "x")));
        assertTrue(e.getMessage().contains("nome"));
    }

    @Test
    public void emailSemArrobaEhRejeitado() {
        ValidacaoException e = assertThrows(ValidacaoException.class,
                () -> validador.validar(new Cliente("Ana", "1", "emailinvalido", "x")));
        assertTrue(e.getMessage().contains("e-mail"));
    }

    @Test
    public void clienteNuloEhRejeitado() {
        assertThrows(ValidacaoException.class, () -> validador.validar(null));
    }
}
