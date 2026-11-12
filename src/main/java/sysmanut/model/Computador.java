package sysmanut.model;

import java.time.LocalDate;

/**
 * Computador de um cliente. A data de aquisição passou de String para
 * {@link LocalDate} (elimina o code smell "Primitive Obsession").
 */
public class Computador {

    private int idComputador;
    private String modelo;
    private String fabricante;
    private String numeroSerie;
    private LocalDate dataAquisicao;
    private String status;
    private Cliente cliente;

    public Computador() {
    }

    public Computador(int idComputador, String modelo, String fabricante, String numeroSerie,
                      LocalDate dataAquisicao, String status, Cliente cliente) {
        this.idComputador = idComputador;
        this.modelo = modelo;
        this.fabricante = fabricante;
        this.numeroSerie = numeroSerie;
        this.dataAquisicao = dataAquisicao;
        this.status = status;
        this.cliente = cliente;
    }

    public int getIdComputador() {
        return idComputador;
    }

    public void setIdComputador(int idComputador) {
        this.idComputador = idComputador;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public LocalDate getDataAquisicao() {
        return dataAquisicao;
    }

    public void setDataAquisicao(LocalDate dataAquisicao) {
        this.dataAquisicao = dataAquisicao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}
