package sysmanut.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Before;
import org.junit.Test;
import sysmanut.exception.RegraNegocioException;
import sysmanut.exception.ValidacaoException;
import sysmanut.model.Cliente;
import sysmanut.repository.memoria.ClienteRepositoryMemoria;
import sysmanut.validation.ClienteValidador;

/** Testa os casos de uso de cliente usando o repositório em memória (sem MySQL). */
public class ClienteServiceTest {

    private ClienteService service;

    @Before
    public void preparar() {
        service = new ClienteService(new ClienteRepositoryMemoria(), new ClienteValidador());
    }

    private static Cliente ana() {
        return new Cliente("Ana", "5399999-0000", "ana@email.com", "Rua A, 10");
    }

    @Test
    public void cadastrarGeraIdEPersiste() {
        Cliente salvo = service.cadastrar(ana());
        assertEquals(1, salvo.getIdCliente());
        assertEquals(1, service.listar().size());
    }

    @Test
    public void cadastrarRemoveEspacosDasPontas() {
        Cliente salvo = service.cadastrar(new Cliente("  Bruno ", " 1 ", " b@b.com ", " Rua "));
        assertEquals("Bruno", salvo.getNome());
        assertEquals("b@b.com", service.buscarPorId(salvo.getIdCliente()).getEmail());
    }

    @Test
    public void cadastroInvalidoNaoGravaNada() {
        assertThrows(ValidacaoException.class,
                () -> service.cadastrar(new Cliente("Carla", "", "c@c.com", "Rua")));
        assertEquals(0, service.listar().size());
    }

    @Test
    public void buscarInexistenteLancaRegraDeNegocio() {
        assertThrows(RegraNegocioException.class, () -> service.buscarPorId(99));
    }

    @Test
    public void atualizarPersisteAsAlteracoes() {
        Cliente salvo = service.cadastrar(ana());
        service.atualizar(new Cliente(salvo.getIdCliente(), "Ana Souza", "1", "as@email.com", "Rua Nova"));
        assertEquals("Ana Souza", service.buscarPorId(salvo.getIdCliente()).getNome());
        assertEquals(1, service.listar().size());
    }

    @Test
    public void atualizarInexistenteLancaRegraDeNegocio() {
        assertThrows(RegraNegocioException.class,
                () -> service.atualizar(new Cliente(99, "X", "1", "x@x.com", "y")));
    }

    @Test
    public void excluirRemoveEDuplaExclusaoFalha() {
        Cliente salvo = service.cadastrar(ana());
        service.excluir(salvo.getIdCliente());
        assertEquals(0, service.listar().size());
        assertThrows(RegraNegocioException.class, () -> service.excluir(salvo.getIdCliente()));
    }
}
