package br.com.pdv.controlador;

import br.com.pdv.aplicacao.SessaoUsuario;
import br.com.pdv.dominio.ItemVenda;
import br.com.pdv.dominio.Produto;
import br.com.pdv.repositorio.ProdutoRepositorio;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class PdvController {

    @FXML
    private Label rotuloOperador;

    @FXML
    private TextField campoCodigo;

    @FXML
    private Label rotuloValorUnitario;

    @FXML
    private Label rotuloTotalItem;

    @FXML
    private Label rotuloSubtotal;

    @FXML
    private TableView<ItemVenda> tabelaItens;

    @FXML
    private TableColumn<ItemVenda, Number> colunaItem;

    @FXML
    private TableColumn<ItemVenda, String> colunaCodigo;

    @FXML
    private TableColumn<ItemVenda, String> colunaDescricao;

    @FXML
    private TableColumn<ItemVenda, Number> colunaQuantidade;

    @FXML
    private TableColumn<ItemVenda, String> colunaValorUnitario;

    @FXML
    private TableColumn<ItemVenda, String> colunaTotal;

    private final ProdutoRepositorio produtoRepositorio =
            new ProdutoRepositorio();

    private final ObservableList<ItemVenda> itensVenda =
            FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        if (
                SessaoUsuario.estaLogado()
                        && SessaoUsuario.getUsuarioLogado() != null
        ) {

            rotuloOperador.setText(
                    "Operador: "
                            + SessaoUsuario
                            .getUsuarioLogado()
                            .getNome()
            );
        }

        configurarTabela();

        tabelaItens.setItems(itensVenda);

        campoCodigo.requestFocus();
    }

    private void configurarTabela() {

        colunaItem.setCellValueFactory(
                dados -> new SimpleIntegerProperty(
                        itensVenda.indexOf(
                                dados.getValue()
                        ) + 1
                )
        );

        colunaCodigo.setCellValueFactory(
                dados -> new SimpleStringProperty(
                        dados
                                .getValue()
                                .getProduto()
                                .getCodigoBarras()
                )
        );

        colunaDescricao.setCellValueFactory(
                dados -> new SimpleStringProperty(
                        dados
                                .getValue()
                                .getProduto()
                                .getNome()
                )
        );

        colunaQuantidade.setCellValueFactory(
                dados -> new SimpleIntegerProperty(
                        dados
                                .getValue()
                                .getQuantidade()
                )
        );

        colunaValorUnitario.setCellValueFactory(
                dados -> new SimpleStringProperty(
                        formatarValor(
                                dados
                                        .getValue()
                                        .getPrecoUnitarioCentavos()
                        )
                )
        );

        colunaTotal.setCellValueFactory(
                dados -> new SimpleStringProperty(
                        formatarValor(
                                dados
                                        .getValue()
                                        .getTotalCentavos()
                        )
                )
        );
    }

    @FXML
    private void aoBuscarProduto() {

        String codigo =
                campoCodigo
                        .getText()
                        .trim();

        if (codigo.isEmpty()) {
            return;
        }

        try {

            Produto produto =
                    produtoRepositorio
                            .buscarPorCodigoBarras(
                                    codigo
                            );

            if (
                    produto == null
                            || !produto.isAtivo()
            ) {

                exibirAlerta(
                        Alert.AlertType.WARNING,
                        "Produto não encontrado",
                        "Nenhum produto ativo foi encontrado com o código informado."
                );

                campoCodigo.clear();
                campoCodigo.requestFocus();

                return;
            }

            rotuloValorUnitario.setText(
                    formatarValor(
                            produto.getPrecoCentavos()
                    )
            );

            ItemVenda itemAtual =
                    adicionarProdutoNaVenda(
                            produto
                    );

            rotuloTotalItem.setText(
                    formatarValor(
                            itemAtual.getTotalCentavos()
                    )
            );

            atualizarSubtotal();

            campoCodigo.clear();
            campoCodigo.requestFocus();

        } catch (SQLException erro) {

            erro.printStackTrace();

            exibirAlerta(
                    Alert.AlertType.ERROR,
                    "Erro no sistema",
                    "Não foi possível consultar o produto."
            );
        }
    }

    private ItemVenda adicionarProdutoNaVenda(
            Produto produto
    ) {

        for (ItemVenda item : itensVenda) {

            if (
                    item.getProduto().getId()
                            == produto.getId()
            ) {

                item.setQuantidade(
                        item.getQuantidade() + 1
                );

                tabelaItens.refresh();

                return item;
            }
        }

        ItemVenda novoItem =
                new ItemVenda(
                        produto,
                        1,
                        produto.getPrecoCentavos()
                );

        itensVenda.add(
                novoItem
        );

        return novoItem;
    }

    private void atualizarSubtotal() {

        long subtotalCentavos = 0;

        for (ItemVenda item : itensVenda) {

            subtotalCentavos +=
                    item.getTotalCentavos();
        }

        rotuloSubtotal.setText(
                formatarValor(
                        subtotalCentavos
                )
        );
    }

    @FXML
    private void aoFecharCaixa() {

        try {

            var recurso =
                    getClass().getResource(
                            "/fxml/fechamento-caixa.fxml"
                    );

            if (recurso == null) {

                exibirAlerta(
                        Alert.AlertType.ERROR,
                        "Arquivo não encontrado",
                        "Não foi possível localizar fechamento-caixa.fxml."
                );

                return;
            }

            FXMLLoader carregador =
                    new FXMLLoader(recurso);

            Scene cenaFechamento =
                    new Scene(
                            carregador.load()
                    );

            Stage janela =
                    (Stage)
                            rotuloOperador
                                    .getScene()
                                    .getWindow();

            janela.setTitle(
                    "Le Café | Cafeteria Premium - Fechamento de Caixa"
            );

            janela.setScene(cenaFechamento);
            janela.centerOnScreen();

        } catch (IOException erro) {

            erro.printStackTrace();

            exibirAlerta(
                    Alert.AlertType.ERROR,
                    "Erro no sistema",
                    "Não foi possível abrir a tela de fechamento de caixa."
            );
        }
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
                "R$ %d,%02d",
                reais,
                centavos
        );
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

        alerta.setHeaderText(titulo);
        alerta.setContentText(mensagem);

        alerta.showAndWait();
    }
}