package sysmanut.repository;

import java.util.List;
import java.util.Optional;
import sysmanut.model.Cliente;

/**
 * Abstração de persistência de clientes (padrão Repository).
 * O serviço depende desta interface, nunca de JDBC, JPA ou Spring Data.
 */
public interface ClienteRepository {

    /** Grava um novo cliente e devolve uma cópia já com o id gerado. */
    Cliente inserir(Cliente cliente);

    /** @return true se o cliente existia e foi atualizado */
    boolean atualizar(Cliente cliente);

    /** @return true se o cliente existia e foi removido */
    boolean excluir(int idCliente);

    Optional<Cliente> buscarPorId(int idCliente);

    List<Cliente> listarTodos();
}
