package sysmanut.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import sysmanut.exception.RegraNegocioException;
import sysmanut.exception.ValidacaoException;
import sysmanut.model.Usuario;
import sysmanut.repository.UsuarioRepository;
import sysmanut.security.PasswordHasher;
import sysmanut.validation.Validador;

/**
 * Cadastro e autenticação de usuários. No desktop, TelaLogin.entrar() abria o menu
 * sem verificar nada e a classe Usuario não era usada; para a versão web o login precisa existir.
 */
public class AutenticacaoService {

    public static final int TAMANHO_MINIMO_SENHA = 6;
    private static final String MSG_CREDENCIAIS_INVALIDAS = "Login ou senha inválidos.";

    private final UsuarioRepository repositorio;
    private final PasswordHasher hasher;
    private final Validador<Usuario> validador;

    public AutenticacaoService(UsuarioRepository repositorio, PasswordHasher hasher, Validador<Usuario> validador) {
        this.repositorio = repositorio;
        this.hasher = hasher;
        this.validador = validador;
    }

    public Usuario registrar(String nome, String login, String senha, String tipoAcesso) {
        Usuario novo = new Usuario(0, nome, login == null ? null : login.trim(), null, tipoAcesso);
        validador.validar(novo);
        validarSenha(senha);

        if (repositorio.buscarPorLogin(novo.getLogin()).isPresent()) {
            throw new RegraNegocioException("Já existe um usuário com o login '" + novo.getLogin() + "'.");
        }
        novo.setNome(nome.trim());
        novo.setTipoAcesso(tipoAcesso.trim());
        novo.setSenhaHash(hasher.gerarHash(senha));
        return repositorio.inserir(novo);
    }

    /** @throws RegraNegocioException com a MESMA mensagem para login inexistente e senha errada */
    public Usuario autenticar(String login, String senha) {
        if (login == null || senha == null) {
            throw new RegraNegocioException(MSG_CREDENCIAIS_INVALIDAS);
        }
        Optional<Usuario> encontrado = repositorio.buscarPorLogin(login.trim());
        if (encontrado.isEmpty() || !hasher.confere(senha, encontrado.get().getSenhaHash())) {
            throw new RegraNegocioException(MSG_CREDENCIAIS_INVALIDAS);
        }
        return encontrado.get();
    }

    private static void validarSenha(String senha) {
        if (senha == null || senha.length() < TAMANHO_MINIMO_SENHA) {
            List<String> erros = new ArrayList<>();
            erros.add("A senha deve ter pelo menos " + TAMANHO_MINIMO_SENHA + " caracteres.");
            throw new ValidacaoException(erros);
        }
    }
}
