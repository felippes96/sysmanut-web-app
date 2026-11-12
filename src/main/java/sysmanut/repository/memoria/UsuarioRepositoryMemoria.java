package sysmanut.repository.memoria;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import sysmanut.model.Usuario;
import sysmanut.repository.UsuarioRepository;

/** Implementação em memória de {@link UsuarioRepository} (testes). */
public class UsuarioRepositoryMemoria implements UsuarioRepository {

    private final Map<String, Usuario> porLogin = new LinkedHashMap<>();
    private final AtomicInteger sequencia = new AtomicInteger(0);

    @Override
    public synchronized Usuario inserir(Usuario usuario) {
        Usuario salvo = copiar(usuario);
        salvo.setIdUsuario(sequencia.incrementAndGet());
        porLogin.put(salvo.getLogin(), salvo);
        return copiar(salvo);
    }

    @Override
    public synchronized Optional<Usuario> buscarPorLogin(String login) {
        return Optional.ofNullable(porLogin.get(login)).map(UsuarioRepositoryMemoria::copiar);
    }

    private static Usuario copiar(Usuario u) {
        return new Usuario(u.getIdUsuario(), u.getNome(), u.getLogin(), u.getSenhaHash(), u.getTipoAcesso());
    }
}
