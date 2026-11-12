package sysmanut.model;

/**
 * Entidade de domínio: cliente da assistência técnica.
 * Não conhece banco de dados nem interface gráfica.
 */
public class Cliente {

    private int idCliente;
    private String nome;
    private String telefone;
    private String email;
    private String endereco;

    public Cliente() {
    }

    public Cliente(String nome, String telefone, String email, String endereco) {
        this(0, nome, telefone, email, endereco);
    }

    public Cliente(int idCliente, String nome, String telefone, String email, String endereco) {
        this.idCliente = idCliente;
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.endereco = endereco;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    @Override
    public String toString() {
        return idCliente + " - " + nome + " <" + email + ">";
    }
}
