package sysmanut.infra;

import java.sql.Connection;
import java.sql.SQLException;

/** Fábrica de conexões. Os repositórios JDBC recebem esta abstração (injeção de dependência). */
public interface ConnectionFactory {

    Connection obter() throws SQLException;
}
