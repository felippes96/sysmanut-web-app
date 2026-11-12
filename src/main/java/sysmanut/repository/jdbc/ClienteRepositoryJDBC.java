package sysmanut.repository.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import sysmanut.exception.RepositorioException;
import sysmanut.infra.ConnectionFactory;
import sysmanut.model.Cliente;
import sysmanut.repository.ClienteRepository;

/**
 * Evolução de ClienteDAO: mesma tabela e mesmos SQLs, porém
 * (1) implementa a interface ClienteRepository,
 * (2) recebe a ConnectionFactory por construtor,
 * (3) lista colunas explícitas em vez de SELECT *,
 * (4) preserva a causa das exceções,
 * (5) informa se a linha existia (linhas afetadas) e devolve o id gerado.
 */
public class ClienteRepositoryJDBC implements ClienteRepository {

    private static final String SQL_INSERIR =
            "INSERT INTO cliente (nome, telefone, email, endereco) VALUES (?, ?, ?, ?)";
    private static final String SQL_ATUALIZAR =
            "UPDATE cliente SET nome = ?, telefone = ?, email = ?, endereco = ? WHERE idCliente = ?";
    private static final String SQL_EXCLUIR = "DELETE FROM cliente WHERE idCliente = ?";
    private static final String SQL_BUSCAR =
            "SELECT idCliente, nome, telefone, email, endereco FROM cliente WHERE idCliente = ?";
    private static final String SQL_LISTAR =
            "SELECT idCliente, nome, telefone, email, endereco FROM cliente ORDER BY idCliente";

    private final ConnectionFactory fabrica;

    public ClienteRepositoryJDBC(ConnectionFactory fabrica) {
        this.fabrica = fabrica;
    }

    @Override
    public Cliente inserir(Cliente cliente) {
        try (Connection conn = fabrica.obter();
             PreparedStatement stmt = conn.prepareStatement(SQL_INSERIR, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getTelefone());
            stmt.setString(3, cliente.getEmail());
            stmt.setString(4, cliente.getEndereco());
            stmt.executeUpdate();

            try (ResultSet chaves = stmt.getGeneratedKeys()) {
                int id = chaves.next() ? chaves.getInt(1) : 0;
                return new Cliente(id, cliente.getNome(), cliente.getTelefone(),
                        cliente.getEmail(), cliente.getEndereco());
            }
        } catch (SQLException e) {
            throw new RepositorioException("Erro ao inserir cliente", e);
        }
    }

    @Override
    public boolean atualizar(Cliente cliente) {
        try (Connection conn = fabrica.obter();
             PreparedStatement stmt = conn.prepareStatement(SQL_ATUALIZAR)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getTelefone());
            stmt.setString(3, cliente.getEmail());
            stmt.setString(4, cliente.getEndereco());
            stmt.setInt(5, cliente.getIdCliente());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RepositorioException("Erro ao atualizar cliente", e);
        }
    }

    @Override
    public boolean excluir(int idCliente) {
        try (Connection conn = fabrica.obter();
             PreparedStatement stmt = conn.prepareStatement(SQL_EXCLUIR)) {

            stmt.setInt(1, idCliente);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RepositorioException("Erro ao excluir cliente", e);
        }
    }

    @Override
    public Optional<Cliente> buscarPorId(int idCliente) {
        try (Connection conn = fabrica.obter();
             PreparedStatement stmt = conn.prepareStatement(SQL_BUSCAR)) {

            stmt.setInt(1, idCliente);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RepositorioException("Erro ao buscar cliente", e);
        }
    }

    @Override
    public List<Cliente> listarTodos() {
        List<Cliente> clientes = new ArrayList<>();

        try (Connection conn = fabrica.obter();
             PreparedStatement stmt = conn.prepareStatement(SQL_LISTAR);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                clientes.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RepositorioException("Erro ao listar clientes", e);
        }
        return clientes;
    }

    private static Cliente mapear(ResultSet rs) throws SQLException {
        return new Cliente(
                rs.getInt("idCliente"),
                rs.getString("nome"),
                rs.getString("telefone"),
                rs.getString("email"),
                rs.getString("endereco"));
    }
}
