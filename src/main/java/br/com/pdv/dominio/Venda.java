package br.com.pdv.dominio;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Venda {

    private int id;

    private int sessaoCaixaId;

    private int usuarioId;

    private LocalDateTime dataHora;

    private String status;

    private final List<ItemVenda> itens =
            new ArrayList<>();

    private final List<PagamentoVenda> pagamentos =
            new ArrayList<>();

    public Venda() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSessaoCaixaId() {
        return sessaoCaixaId;
    }

    public void setSessaoCaixaId(
            int sessaoCaixaId
    ) {
        this.sessaoCaixaId = sessaoCaixaId;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(
            int usuarioId
    ) {
        this.usuarioId = usuarioId;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(
            LocalDateTime dataHora
    ) {
        this.dataHora = dataHora;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(
            String status
    ) {
        this.status = status;
    }

    public List<ItemVenda> getItens() {
        return itens;
    }

    public void adicionarItem(
            ItemVenda item
    ) {
        itens.add(item);
    }

    public List<PagamentoVenda> getPagamentos() {
        return pagamentos;
    }

    public void adicionarPagamento(
            PagamentoVenda pagamento
    ) {
        pagamentos.add(pagamento);
    }

    public long getTotalCentavos() {

        long totalCentavos = 0;

        for (ItemVenda item : itens) {

            totalCentavos +=
                    item.getTotalCentavos();
        }

        return totalCentavos;
    }

    public long getTotalPagamentosCentavos() {

        long totalPagamentos = 0;

        for (PagamentoVenda pagamento : pagamentos) {

            totalPagamentos +=
                    pagamento.getValorCentavos();
        }

        return totalPagamentos;
    }
}