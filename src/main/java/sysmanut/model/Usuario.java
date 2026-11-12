package sysmanut.model;

/**
 * Usuário do sistema. Antes ficava solto no pacote raiz; agora faz parte do modelo.
 * O campo {@code senhaHash} guarda SEMPRE o hash da senha, nunca o texto puro.
 */
public class Usuario {

    private int idUsuario;
    private String nome;
    private String login;
    private String senhaHash;
    private String tipoAcesso;

    public Usuario() {
    }

    public Usuario(int idUsuario, String nome, String login, String senhaHash, String tipoAcesso) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.login = login;
        this.senhaHash = senhaHash;
        this.tipoAcesso = tipoAcesso;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }

    public String getTipoAcesso() {
        return tipoAcesso;
    }

    public void setTipoAcesso(String tipoAcesso) {
        this.tipoAcesso = tipoAcesso;
    }

    @Override
    public String toString() {
        return idUsuario + " - " + login + " (" + tipoAcesso + ")";
    }
}
