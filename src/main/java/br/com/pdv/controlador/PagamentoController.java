package br.com.pdv.controlador;

import br.com.pdv.dominio.ItemVenda;
import br.com.pdv.dominio.PagamentoVenda;
import br.com.pdv.dominio.Venda;
import br.com.pdv.servico.ServicoVenda;
import br.com.pdv.utilitario.FormatadorMoeda;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PagamentoController {

    @FXML
    private Label rotuloTotalVenda;

    @FXML
    private TextField campoValorRecebido;

    @FXML
    private Label rotuloTroco;

    @FXML
    private Button botaoDinheiro;

    @FXML
    private Button botaoPix;

    @FXML
    private Button botaoCredito;

    @FXML
    private Button botaoDebito;

    @FXML
    private Button botaoConfirmar;

    @FXML
    private Button botaoCancelar;

    private final ServicoVenda servicoVenda =
            new ServicoVenda();

    private List<ItemVenda> itensVenda =
            new ArrayList<>();

    private String formaPagamento;

    private long totalVendaCentavos;

    private Runnable aoVendaFinalizada;

    @FXML
    public void initialize() {

        FormatadorMoeda.aplicarMascara(
                campoValorRecebido
        );

        campoValorRecebido.setDisable(true);

        botaoConfirmar.setDisable(true);

        campoValorRecebido
                .textProperty()
                .addListener(
                        (observavel, valorAntigo, valorNovo) ->
                                atualizarTroco()
                );
    }

    public void configurarVenda(
            List<ItemVenda> itens,
            Runnable aoVendaFinalizada
    ) {

        this.itensVenda =
                new ArrayList<>(itens);

        this.aoVendaFinalizada =
                aoVendaFinalizada;

        totalVendaCentavos = 0;

        for (ItemVenda item : itensVenda) {

            totalVendaCentavos +=
                    item.getTotalCentavos();
        }

        rotuloTotalVenda.setText(
                formatarValor(
                        totalVendaCentavos
                )
        );
    }

    @FXML
    private void aoSelecionarDinheiro() {

        formaPagamento =
                "DINHEIRO";

        campoValorRecebido.setDisable(false);
        campoValorRecebido.requestFocus();

        botaoConfirmar.setDisable(false);

        atualizarSelecao(
                botaoDinheiro
        );

        atualizarTroco();
    }

    @FXML
    private void aoSelecionarPix() {

        selecionarPagamentoSemTroco(
                "PIX",
                botaoPix
        );
    }

    @FXML
    private void aoSelecionarCredito() {

        selecionarPagamentoSemTroco(
                "CARTAO_CREDITO",
                botaoCredito
        );
    }

    @FXML
    private void aoSelecionarDebito() {

        selecionarPagamentoSemTroco(
                "CARTAO_DEBITO",
                botaoDebito
        );
    }

    private void selecionarPagamentoSemTroco(
            String forma,
            Button botao
    ) {

        formaPagamento =
                forma;

        campoValorRecebido.setDisable(true);

        rotuloTroco.setText(
                "R$ 0,00"
        );

        botaoConfirmar.setDisable(false);

        atualizarSelecao(
                botao
        );
    }

    private void atualizarSelecao(
            Button botaoSelecionado
    ) {

        botaoDinheiro
                .getStyleClass()
                .remove("botao-pagamento-selecionado");

        botaoPix
                .getStyleClass()
                .remove("botao-pagamento-selecionado");

        botaoCredito
                .getStyleClass()
                .remove("botao-pagamento-selecionado");

        botaoDebito
                .getStyleClass()
                .remove("botao-pagamento-selecionado");

        if (
                !botaoSelecionado
                        .getStyleClass()
                        .contains(
                                "botao-pagamento-selecionado"
                        )
        ) {

            botaoSelecionado
                    .getStyleClass()
                    .add(
                            "botao-pagamento-selecionado"
                    );
        }
    }

    private void atualizarTroco() {

        if (
                !"DINHEIRO".equals(
                        formaPagamento
                )
        ) {

            rotuloTroco.setText(
                    "R$ 0,00"
            );

            return;
        }

        long valorRecebido =
                FormatadorMoeda
                        .converterParaCentavos(
                                campoValorRecebido
                                        .getText()
                        );

        long troco =
                valorRecebido
                        - totalVendaCentavos;

        if (troco < 0) {

            rotuloTroco.setText(
                    "R$ 0,00"
            );

            return;
        }

        rotuloTroco.setText(
                formatarValor(
                        troco
                )
        );
    }

    @FXML
    private void aoConfirmarPagamento() {

        if (formaPagamento == null) {

            exibirAlerta(
                    Alert.AlertType.WARNING,
                    "Forma de pagamento",
                    "Selecione uma forma de pagamento."
            );

            return;
        }

        PagamentoVenda pagamento =
                new PagamentoVenda();

        pagamento.setFormaPagamento(
                formaPagamento
        );

        pagamento.setValorCentavos(
                totalVendaCentavos
        );

        if (
                "DINHEIRO".equals(
                        formaPagamento
                )
        ) {

            long valorRecebido =
                    FormatadorMoeda
                            .converterParaCentavos(
                                    campoValorRecebido
                                            .getText()
                            );

            if (
                    valorRecebido
                            < totalVendaCentavos
            ) {

                exibirAlerta(
                        Alert.AlertType.WARNING,
                        "Valor insuficiente",
                        "O valor recebido é menor que o total da venda."
                );

                campoValorRecebido.requestFocus();

                return;
            }

            long troco =
                    valorRecebido
                            - totalVendaCentavos;

            pagamento.setValorRecebidoCentavos(
                    valorRecebido
            );

            pagamento.setTrocoCentavos(
                    troco
            );

        } else {

            pagamento.setValorRecebidoCentavos(
                    null
            );

            pagamento.setTrocoCentavos(
                    0
            );
        }

        try {

            Venda venda =
                    servicoVenda.finalizarVenda(
                            itensVenda,
                            List.of(pagamento)
                    );

            exibirAlerta(
                    Alert.AlertType.INFORMATION,
                    "Venda finalizada",
                    "Venda nº "
                            + venda.getId()
                            + " finalizada com sucesso."
            );

            fecharJanela();

            if (aoVendaFinalizada != null) {

                aoVendaFinalizada.run();
            }

        } catch (
                SQLException
                | IllegalStateException erro
        ) {

            erro.printStackTrace();

            exibirAlerta(
                    Alert.AlertType.ERROR,
                    "Não foi possível finalizar a venda",
                    erro.getMessage()
            );
        }
    }

    @FXML
    private void aoCancelar() {

        fecharJanela();
    }

    private void fecharJanela() {

        Stage janela =
                (Stage)
                        botaoCancelar
                                .getScene()
                                .getWindow();

        janela.close();
    }

    private String formatarValor(
            long valorCentavos
    ) {

        long reais =
                valorCentavos / 100;

        long centavos =
                Math.abs(
                        valorCentavos % 100
                );

        return String.format(
                "R$ %,d,%02d",
                reais,
                centavos
        ).replace(",", ".");
    }

    private void exibirAlerta(
            Alert.AlertType tipo,
            String titulo,
            String mensagem
    ) {

        Alert alerta =
                new Alert(tipo);

        alerta.setTitle(
                "Le Café | PDV"
        );

        alerta.setHeaderText(
                titulo
        );

        alerta.setContentText(
                mensagem
        );

        alerta.showAndWait();
    }
}