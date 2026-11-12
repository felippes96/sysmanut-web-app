package sysmanut.validation;

import java.util.ArrayList;
import java.util.List;
import sysmanut.exception.ValidacaoException;
import sysmanut.model.Usuario;

/** Valida os dados cadastrais do usuário (a política de senha fica em AutenticacaoService). */
public class UsuarioValidador implements Validador<Usuario> {

    @Override
    public void validar(Usuario usuario) {
        List<String> erros = new ArrayList<>();

        if (usuario == null) {
            erros.add("Usuário não informado.");
            throw new ValidacaoException(erros);
        }
        if (vazio(usuario.getNome())) {
            erros.add("O nome é obrigatório.");
        }
        if (vazio(usuario.getLogin())) {
            erros.add("O login é obrigatório.");
        }
        if (vazio(usuario.getTipoAcesso())) {
            erros.add("O tipo de acesso é obrigatório.");
        }
        if (!erros.isEmpty()) {
            throw new ValidacaoException(erros);
        }
    }

    private static boolean vazio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }
}
