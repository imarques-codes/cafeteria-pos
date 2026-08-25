package br.com.pdv.dominio;

import java.time.LocalDateTime;

public class PagamentoVenda {

    private int id;
    private int vendaId;

    private String formaPagamento;

    private long valorCentavos;

    private Long valorRecebidoCentavos;

    private long trocoCentavos;

    private LocalDateTime dataHora;

    public PagamentoVenda() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVendaId() {
        return vendaId;
    }

    public void setVendaId(int vendaId) {
        this.vendaId = vendaId;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(
            String formaPagamento
    ) {
        this.formaPagamento = formaPagamento;
    }

    public long getValorCentavos() {
        return valorCentavos;
    }

    public void setValorCentavos(
            long valorCentavos
    ) {
        this.valorCentavos = valorCentavos;
    }

    public Long getValorRecebidoCentavos() {
        return valorRecebidoCentavos;
    }

    public void setValorRecebidoCentavos(
            Long valorRecebidoCentavos
    ) {
        this.valorRecebidoCentavos =
                valorRecebidoCentavos;
    }

    public long getTrocoCentavos() {
        return trocoCentavos;
    }

    public void setTrocoCentavos(
            long trocoCentavos
    ) {
        this.trocoCentavos = trocoCentavos;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(
            LocalDateTime dataHora
    ) {
        this.dataHora = dataHora;
    }
}