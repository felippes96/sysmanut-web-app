package sysmanut.infra;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Cria conexões MySQL a partir de uma {@link ConfiguracaoBanco}. Substitui ConexaoMySQL. */
public class MySQLConnectionFactory implements ConnectionFactory {

    private final ConfiguracaoBanco configuracao;

    public MySQLConnectionFactory(ConfiguracaoBanco configuracao) {
        this.configuracao = configuracao;
    }

    @Override
    public Connection obter() throws SQLException {
        return DriverManager.getConnection(
                configuracao.getUrl(), configuracao.getUsuario(), configuracao.getSenha());
    }
}
