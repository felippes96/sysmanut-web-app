package sysmanut.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Serviço de manutenção de um computador. A data passou de String para
 * {@link LocalDate} e o preço de double para {@link BigDecimal}
 * (valores monetários não devem usar ponto flutuante binário).
 */
public class Manutencao {

    private int idManutencao;
    private LocalDate data;
    private String descricao;
    private String tipo;
    private BigDecimal preco;
    private String status;
    private Computador computador;

    public Manutencao() {
    }

    public Manutencao(int idManutencao, LocalDate data, String descricao, String tipo,
                      BigDecimal preco, String status, Computador computador) {
        this.idManutencao = idManutencao;
        this.data = data;
        this.descricao = descricao;
        this.tipo = tipo;
        this.preco = preco;
        this.status = status;
        this.computador = computador;
    }

    public int getIdManutencao() {
        return idManutencao;
    }

    public void setIdManutencao(int idManutencao) {
        this.idManutencao = idManutencao;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Computador getComputador() {
        return computador;
    }

    public void setComputador(Computador computador) {
        this.computador = computador;
    }
}
