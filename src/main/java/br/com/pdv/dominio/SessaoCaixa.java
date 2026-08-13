package br.com.pdv.dominio;

import java.time.LocalDateTime;

public class SessaoCaixa {

    private int id;

    private int caixaId;
    private int usuarioAberturaId;

    private LocalDateTime dataHoraAbertura;

    private long saldoInicialCentavos;

    private Integer usuarioFechamentoId;
    private LocalDateTime dataHoraFechamento;

    private Long saldoFinalCentavos;

    private String status;

    public SessaoCaixa() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCaixaId() {
        return caixaId;
    }

    public void setCaixaId(int caixaId) {
        this.caixaId = caixaId;
    }

    public int getUsuarioAberturaId() {
        return usuarioAberturaId;
    }

    public void setUsuarioAberturaId(int usuarioAberturaId) {
        this.usuarioAberturaId = usuarioAberturaId;
    }

    public LocalDateTime getDataHoraAbertura() {
        return dataHoraAbertura;
    }

    public void setDataHoraAbertura(
            LocalDateTime dataHoraAbertura
    ) {
        this.dataHoraAbertura = dataHoraAbertura;
    }

    public long getSaldoInicialCentavos() {
        return saldoInicialCentavos;
    }

    public void setSaldoInicialCentavos(
            long saldoInicialCentavos
    ) {
        this.saldoInicialCentavos = saldoInicialCentavos;
    }

    public Integer getUsuarioFechamentoId() {
        return usuarioFechamentoId;
    }

    public void setUsuarioFechamentoId(
            Integer usuarioFechamentoId
    ) {
        this.usuarioFechamentoId = usuarioFechamentoId;
    }

    public LocalDateTime getDataHoraFechamento() {
        return dataHoraFechamento;
    }

    public void setDataHoraFechamento(
            LocalDateTime dataHoraFechamento
    ) {
        this.dataHoraFechamento = dataHoraFechamento;
    }

    public Long getSaldoFinalCentavos() {
        return saldoFinalCentavos;
    }

    public void setSaldoFinalCentavos(
            Long saldoFinalCentavos
    ) {
        this.saldoFinalCentavos = saldoFinalCentavos;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}