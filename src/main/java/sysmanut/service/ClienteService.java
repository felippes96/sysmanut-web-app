package sysmanut.service;

import java.util.List;
import sysmanut.exception.RegraNegocioException;
import sysmanut.model.Cliente;
import sysmanut.repository.ClienteRepository;
import sysmanut.validation.Validador;

/**
 * Casos de uso de cliente. Concentra o que antes estava espalhado nos
 * actionPerformed de TelaCadastroCliente e TelaClientes.
 *
 * Depende só de abstrações (ClienteRepository e Validador), recebidas pelo construtor.
 * Não importa nenhuma classe de javax.swing/java.awt: pode ser usada por Swing, Servlet, Spring etc.
 */
public class ClienteService {

    private final ClienteRepository repositorio;
    private final Validador<Cliente> validador;

    public ClienteService(ClienteRepository repositorio, Validador<Cliente> validador) {
        this.repositorio = repositorio;
        this.validador = validador;
    }

    /** Valida, normaliza (remove espaços das pontas) e grava. Devolve o cliente com o id gerado. */
    public Cliente cadastrar(Cliente cliente) {
        validador.validar(cliente);
        return repositorio.inserir(normalizar(cliente));
    }

    /** Estava como "Edição pode ser implementada depois" na tela; o DAO já tinha o SQL. */
    public Cliente atualizar(Cliente cliente) {
        validador.validar(cliente);
        Cliente normalizado = normalizar(cliente);
        if (!repositorio.atualizar(normalizado)) {
            throw clienteNaoEncontrado(cliente.getIdCliente());
        }
        return normalizado;
    }

    public void excluir(int idCliente) {
        if (!repositorio.excluir(idCliente)) {
            throw clienteNaoEncontrado(idCliente);
        }
    }

    public Cliente buscarPorId(int idCliente) {
        return repositorio.buscarPorId(idCliente).orElseThrow(() -> clienteNaoEncontrado(idCliente));
    }

    public List<Cliente> listar() {
        return repositorio.listarTodos();
    }

    private static Cliente normalizar(Cliente c) {
        return new Cliente(c.getIdCliente(), c.getNome().trim(), c.getTelefone().trim(),
                c.getEmail().trim(), c.getEndereco().trim());
    }

    private static RegraNegocioException clienteNaoEncontrado(int id) {
        return new RegraNegocioException("Cliente não encontrado (id " + id + ").");
    }
}
