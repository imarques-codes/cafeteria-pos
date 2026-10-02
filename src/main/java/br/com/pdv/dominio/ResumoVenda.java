package br.com.pdv.dominio;

import java.time.LocalDateTime;

public class ResumoVenda {

    private int id;

    private LocalDateTime dataHora;

    private long totalCentavos;

    private String status;

    private String nomeOperador;

    public ResumoVenda() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(
            LocalDateTime dataHora
    ) {
        this.dataHora = dataHora;
    }

    public long getTotalCentavos() {
        return totalCentavos;
    }

    public void setTotalCentavos(
            long totalCentavos
    ) {
        this.totalCentavos = totalCentavos;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(
            String status
    ) {
        this.status = status;
    }

    public String getNomeOperador() {
        return nomeOperador;
    }

    public void setNomeOperador(
            String nomeOperador
    ) {
        this.nomeOperador = nomeOperador;
    }
}