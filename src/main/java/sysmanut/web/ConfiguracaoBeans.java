package sysmanut.web;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sysmanut.infra.ConnectionFactory;
import sysmanut.repository.*;
import sysmanut.repository.jdbc.*;
import sysmanut.security.Pbkdf2PasswordHasher;
import sysmanut.service.*;
import sysmanut.validation.*;

/** Liga o núcleo (sysmanut-core) ao Spring: injeção das classes das etapas 6 e 7. */
@Configuration
public class ConfiguracaoBeans {
    @Bean ConnectionFactory connectionFactory(DataSource ds) { return ds::getConnection; }
    @Bean ClienteRepository clienteRepository(ConnectionFactory f) { return new ClienteRepositoryJDBC(f); }
    @Bean UsuarioRepository usuarioRepository(ConnectionFactory f) { return new UsuarioRepositoryJDBC(f); }
    @Bean ClienteService clienteService(ClienteRepository r) { return new ClienteService(r, new ClienteValidador()); }
    @Bean AutenticacaoService autenticacaoService(UsuarioRepository r) {
        return new AutenticacaoService(r, new Pbkdf2PasswordHasher(), new UsuarioValidador());
    }
    @Bean CalculadoraManutencao calculadoraManutencao() { return new CalculadoraManutencao(); }
    @Bean RegraGarantia regraGarantia() { return new RegraGarantia(); }

    /** Cria o usuário "admin" na 1ª execução, se SYSMANUT_ADMIN_PASSWORD estiver definida. */
    @Bean ApplicationRunner adminInicial(AutenticacaoService auth, UsuarioRepository repo,
                                         @Value("${SYSMANUT_ADMIN_PASSWORD:}") String senha) {
        return args -> {
            if (!senha.isBlank() && repo.buscarPorLogin("admin").isEmpty()) {
                auth.registrar("Administrador", "admin", senha, "ADMIN");
            }
        };
    }
}
