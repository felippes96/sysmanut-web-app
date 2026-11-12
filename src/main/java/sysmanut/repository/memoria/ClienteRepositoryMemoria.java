package sysmanut.repository.memoria;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import sysmanut.model.Cliente;
import sysmanut.repository.ClienteRepository;

/**
 * Implementação em memória, usada nos testes do main() (não precisa de MySQL).
 * Guarda e devolve CÓPIAS, imitando o comportamento de um banco real.
 */
public class ClienteRepositoryMemoria implements ClienteRepository {

    private final Map<Integer, Cliente> dados = new TreeMap<>();
    private final AtomicInteger sequencia = new AtomicInteger(0);

    @Override
    public synchronized Cliente inserir(Cliente cliente) {
        Cliente salvo = copiar(cliente);
        salvo.setIdCliente(sequencia.incrementAndGet());
        dados.put(salvo.getIdCliente(), salvo);
        return copiar(salvo);
    }

    @Override
    public synchronized boolean atualizar(Cliente cliente) {
        if (!dados.containsKey(cliente.getIdCliente())) {
            return false;
        }
        dados.put(cliente.getIdCliente(), copiar(cliente));
        return true;
    }

    @Override
    public synchronized boolean excluir(int idCliente) {
        return dados.remove(idCliente) != null;
    }

    @Override
    public synchronized Optional<Cliente> buscarPorId(int idCliente) {
        return Optional.ofNullable(dados.get(idCliente)).map(ClienteRepositoryMemoria::copiar);
    }

    @Override
    public synchronized List<Cliente> listarTodos() {
        List<Cliente> lista = new ArrayList<>();
        for (Cliente c : dados.values()) {
            lista.add(copiar(c));
        }
        return lista;
    }

    private static Cliente copiar(Cliente c) {
        return new Cliente(c.getIdCliente(), c.getNome(), c.getTelefone(), c.getEmail(), c.getEndereco());
    }
}
