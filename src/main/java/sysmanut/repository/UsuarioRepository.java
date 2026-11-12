package sysmanut.repository;

import java.util.Optional;
import sysmanut.model.Usuario;

/** Abstração de persistência de usuários. */
public interface UsuarioRepository {

    /** Grava um novo usuário e devolve uma cópia já com o id gerado. */
    Usuario inserir(Usuario usuario);

    Optional<Usuario> buscarPorLogin(String login);
}
