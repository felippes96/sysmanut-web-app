package sysmanut.repository.jdbc;

import java.sql.*;
import java.util.Optional;
import sysmanut.exception.RepositorioException;
import sysmanut.infra.ConnectionFactory;
import sysmanut.model.Usuario;
import sysmanut.repository.UsuarioRepository;

/** Persistência de usuários em MySQL (a coluna "senha" guarda o hash PBKDF2). */
public class UsuarioRepositoryJDBC implements UsuarioRepository {
    private final ConnectionFactory fabrica;

    public UsuarioRepositoryJDBC(ConnectionFactory fabrica) { this.fabrica = fabrica; }

    @Override
    public Usuario inserir(Usuario u) {
        String sql = "INSERT INTO usuario (nome, login, senha, tipoAcesso) VALUES (?, ?, ?, ?)";
        try (Connection c = fabrica.obter(); PreparedStatement st = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, u.getNome()); st.setString(2, u.getLogin());
            st.setString(3, u.getSenhaHash()); st.setString(4, u.getTipoAcesso());
            st.executeUpdate();
            try (ResultSet k = st.getGeneratedKeys()) {
                return new Usuario(k.next() ? k.getInt(1) : 0, u.getNome(), u.getLogin(), u.getSenhaHash(), u.getTipoAcesso());
            }
        } catch (SQLException e) { throw new RepositorioException("Erro ao inserir usuário", e); }
    }

    @Override
    public Optional<Usuario> buscarPorLogin(String login) {
        String sql = "SELECT idUsuario, nome, login, senha, tipoAcesso FROM usuario WHERE login = ?";
        try (Connection c = fabrica.obter(); PreparedStatement st = c.prepareStatement(sql)) {
            st.setString(1, login);
            try (ResultSet r = st.executeQuery()) {
                return r.next() ? Optional.of(new Usuario(r.getInt(1), r.getString(2), r.getString(3), r.getString(4), r.getString(5))) : Optional.empty();
            }
        } catch (SQLException e) { throw new RepositorioException("Erro ao buscar usuário", e); }
    }
}
