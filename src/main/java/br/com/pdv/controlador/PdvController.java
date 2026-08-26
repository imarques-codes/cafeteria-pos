package br.com.pdv.controlador;
import br.com.pdv.aplicacao.SessaoUsuario;
import br.com.pdv.dominio.ItemVenda;
import br.com.pdv.dominio.Produto;
import br.com.pdv.repositorio.ProdutoRepositorio;
import javafx.application.Platform;
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
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import javafx.stage.Modality;

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

        Platform.runLater(() -> {

            campoCodigo
                    .getScene()
                    .addEventFilter(
                            KeyEvent.KEY_PRESSED,
                            evento -> {

                                if (evento.getCode() == KeyCode.F2) {

                                    evento.consume();

                                    aoAbrirProdutos();

                                } else if (
                                        evento.getCode() == KeyCode.F4
                                ) {

                                    evento.consume();

                                    aoAlterarQuantidade();

                                } else if (
                                        evento.getCode() == KeyCode.F5
                                ) {

                                    evento.consume();

                                    aoAbrirClientes();

                                } else if (
                                        evento.getCode() == KeyCode.DELETE
                                                && tabelaItens.isFocused()
                                ) {

                                } else if (
                                        evento.getCode() == KeyCode.DELETE
                                                && tabelaItens.isFocused()
                                ) {

                                    evento.consume();

                                    aoRemoverItem();

                                } else if (
                                        evento.getCode() == KeyCode.F10
                                ) {

                                    evento.consume();

                                    aoFinalizarVenda();
                                }

                                if (evento.getCode() == KeyCode.F4) {

                                    evento.consume();

                                    aoAlterarQuantidade();

                                } else if (
                                        evento.getCode() == KeyCode.DELETE
                                                && tabelaItens.isFocused()
                                ) {

                                    evento.consume();

                                    aoRemoverItem();

                                } else if (
                                        evento.getCode() == KeyCode.F10
                                ) {

                                    evento.consume();

                                    aoFinalizarVenda();
                                }
                            }
                    );
        });
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

            int quantidadeAtual =
                    obterQuantidadeNoCarrinho(
                            produto
                    );

            int novaQuantidade =
                    quantidadeAtual + 1;

            if (
                    produto.isControlaEstoque()
                            && novaQuantidade
                            > produto.getEstoqueAtual()
            ) {

                exibirAlerta(
                        Alert.AlertType.WARNING,
                        "Estoque insuficiente",
                        "Estoque disponível para "
                                + produto.getNome()
                                + ": "
                                + produto.getEstoqueAtual()
                                + " unidade(s)."
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

    private int obterQuantidadeNoCarrinho(
            Produto produto
    ) {

        for (ItemVenda item : itensVenda) {

            if (
                    item.getProduto().getId()
                            == produto.getId()
            ) {

                return item.getQuantidade();
            }
        }

        return 0;
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

    private void aoAlterarQuantidade() {

        ItemVenda itemSelecionado =
                tabelaItens
                        .getSelectionModel()
                        .getSelectedItem();

        if (itemSelecionado == null) {

            exibirAlerta(
                    Alert.AlertType.WARNING,
                    "Item não selecionado",
                    "Selecione um produto na tabela antes de alterar a quantidade."
            );

            return;
        }

        TextInputDialog dialogo =
                new TextInputDialog(
                        String.valueOf(
                                itemSelecionado.getQuantidade()
                        )
                );

        dialogo.setTitle(
                "Le Café | PDV"
        );

        dialogo.setHeaderText(
                "Alterar quantidade"
        );

        dialogo.setContentText(
                "Nova quantidade:"
        );

        dialogo.showAndWait().ifPresent(
                valor -> {

                    try {

                        int novaQuantidade =
                                Integer.parseInt(
                                        valor.trim()
                                );

                        if (novaQuantidade <= 0) {

                            exibirAlerta(
                                    Alert.AlertType.WARNING,
                                    "Quantidade inválida",
                                    "A quantidade deve ser maior que zero."
                            );

                            return;
                        }

                        Produto produto =
                                itemSelecionado.getProduto();

                        if (
                                produto.isControlaEstoque()
                                        && novaQuantidade
                                        > produto.getEstoqueAtual()
                        ) {

                            exibirAlerta(
                                    Alert.AlertType.WARNING,
                                    "Estoque insuficiente",
                                    "Estoque disponível para "
                                            + produto.getNome()
                                            + ": "
                                            + produto.getEstoqueAtual()
                                            + " unidade(s)."
                            );

                            campoCodigo.requestFocus();

                            return;
                        }

                        itemSelecionado.setQuantidade(
                                novaQuantidade
                        );

                        tabelaItens.refresh();

                        rotuloValorUnitario.setText(
                                formatarValor(
                                        itemSelecionado
                                                .getPrecoUnitarioCentavos()
                                )
                        );

                        rotuloTotalItem.setText(
                                formatarValor(
                                        itemSelecionado
                                                .getTotalCentavos()
                                )
                        );

                        atualizarSubtotal();

                        campoCodigo.requestFocus();

                    } catch (NumberFormatException erro) {

                        exibirAlerta(
                                Alert.AlertType.WARNING,
                                "Quantidade inválida",
                                "Informe somente um número inteiro."
                        );
                    }
                }
        );
    }

    private void aoRemoverItem() {

        ItemVenda itemSelecionado =
                tabelaItens
                        .getSelectionModel()
                        .getSelectedItem();

        if (itemSelecionado == null) {

            exibirAlerta(
                    Alert.AlertType.WARNING,
                    "Item não selecionado",
                    "Selecione um produto na tabela antes de remover uma quantidade."
            );

            return;
        }

        TextInputDialog dialogo =
                new TextInputDialog("1");

        dialogo.setTitle(
                "Le Café | PDV"
        );

        dialogo.setHeaderText(
                "Remover quantidade"
        );

        dialogo.setContentText(
                "Quantidade a remover de "
                        + itemSelecionado
                        .getProduto()
                        .getNome()
                        + " (atual: "
                        + itemSelecionado.getQuantidade()
                        + "):"
        );

        dialogo.showAndWait().ifPresent(
                valor -> {

                    try {

                        int quantidadeRemover =
                                Integer.parseInt(
                                        valor.trim()
                                );

                        if (quantidadeRemover <= 0) {

                            exibirAlerta(
                                    Alert.AlertType.WARNING,
                                    "Quantidade inválida",
                                    "A quantidade a remover deve ser maior que zero."
                            );

                            return;
                        }

                        int quantidadeAtual =
                                itemSelecionado
                                        .getQuantidade();

                        if (quantidadeRemover > quantidadeAtual) {

                            exibirAlerta(
                                    Alert.AlertType.WARNING,
                                    "Quantidade inválida",
                                    "Não é possível remover mais unidades do que existem no item."
                            );

                            return;
                        }

                        if (quantidadeRemover == quantidadeAtual) {

                            itensVenda.remove(
                                    itemSelecionado
                            );

                            rotuloValorUnitario.setText(
                                    "R$ 0,00"
                            );

                            rotuloTotalItem.setText(
                                    "R$ 0,00"
                            );

                        } else {

                            int novaQuantidade =
                                    quantidadeAtual
                                            - quantidadeRemover;

                            itemSelecionado.setQuantidade(
                                    novaQuantidade
                            );

                            rotuloValorUnitario.setText(
                                    formatarValor(
                                            itemSelecionado
                                                    .getPrecoUnitarioCentavos()
                                    )
                            );

                            rotuloTotalItem.setText(
                                    formatarValor(
                                            itemSelecionado
                                                    .getTotalCentavos()
                                    )
                            );
                        }

                        tabelaItens
                                .getSelectionModel()
                                .clearSelection();

                        tabelaItens.refresh();

                        atualizarSubtotal();

                        campoCodigo.requestFocus();

                    } catch (NumberFormatException erro) {

                        exibirAlerta(
                                Alert.AlertType.WARNING,
                                "Quantidade inválida",
                                "Informe somente um número inteiro."
                        );
                    }
                }
        );
    }

    private void aoAbrirProdutos() {

        try {

            var recurso =
                    getClass().getResource(
                            "/fxml/produtos.fxml"
                    );

            if (recurso == null) {

                exibirAlerta(
                        Alert.AlertType.ERROR,
                        "Arquivo não encontrado",
                        "Não foi possível localizar produtos.fxml."
                );

                return;
            }

            FXMLLoader carregador =
                    new FXMLLoader(recurso);

            Scene cenaProdutos =
                    new Scene(
                            carregador.load()
                    );

            Stage janelaProdutos =
                    new Stage();

            janelaProdutos.setTitle(
                    "Le Café | Cafeteria Premium - Produtos"
            );

            janelaProdutos.setScene(
                    cenaProdutos
            );

            janelaProdutos.initOwner(
                    tabelaItens
                            .getScene()
                            .getWindow()
            );

            janelaProdutos.initModality(
                    Modality.WINDOW_MODAL
            );

            janelaProdutos.setResizable(
                    false
            );

            janelaProdutos.centerOnScreen();

            janelaProdutos.showAndWait();

            campoCodigo.requestFocus();

        } catch (IOException erro) {

            erro.printStackTrace();

            exibirAlerta(
                    Alert.AlertType.ERROR,
                    "Erro no sistema",
                    "Não foi possível abrir a tela de produtos."
            );
        }
    }

    private void aoAbrirClientes() {

        try {

            var recurso =
                    getClass().getResource(
                            "/fxml/clientes.fxml"
                    );

            if (recurso == null) {

                exibirAlerta(
                        Alert.AlertType.ERROR,
                        "Arquivo não encontrado",
                        "Não foi possível localizar clientes.fxml."
                );

                return;
            }

            FXMLLoader carregador =
                    new FXMLLoader(recurso);

            Scene cenaClientes =
                    new Scene(
                            carregador.load()
                    );

            Stage janelaClientes =
                    new Stage();

            janelaClientes.setTitle(
                    "Le Café | Cafeteria Premium - Clientes"
            );

            janelaClientes.setScene(
                    cenaClientes
            );

            janelaClientes.initOwner(
                    tabelaItens
                            .getScene()
                            .getWindow()
            );

            janelaClientes.initModality(
                    Modality.WINDOW_MODAL
            );

            janelaClientes.setResizable(
                    false
            );

            janelaClientes.centerOnScreen();

            janelaClientes.showAndWait();

            campoCodigo.requestFocus();

        } catch (IOException erro) {

            erro.printStackTrace();

            exibirAlerta(
                    Alert.AlertType.ERROR,
                    "Erro no sistema",
                    "Não foi possível abrir a tela de clientes."
            );
        }
    }

    private void aoFinalizarVenda() {

        if (itensVenda.isEmpty()) {

            exibirAlerta(
                    Alert.AlertType.WARNING,
                    "Venda vazia",
                    "Adicione pelo menos um produto antes de finalizar a venda."
            );

            campoCodigo.requestFocus();

            return;
        }

        try {

            var recurso =
                    getClass().getResource(
                            "/fxml/pagamento.fxml"
                    );

            if (recurso == null) {

                exibirAlerta(
                        Alert.AlertType.ERROR,
                        "Arquivo não encontrado",
                        "Não foi possível localizar pagamento.fxml."
                );

                return;
            }

            FXMLLoader carregador =
                    new FXMLLoader(recurso);

            Scene cenaPagamento =
                    new Scene(
                            carregador.load()
                    );

            PagamentoController controlador =
                    carregador.getController();

            controlador.configurarVenda(
                    itensVenda,
                    this::limparVenda
            );

            Stage janelaPagamento =
                    new Stage();

            janelaPagamento.setTitle(
                    "Le Café | Cafeteria Premium - Pagamento"
            );

            janelaPagamento.setScene(
                    cenaPagamento
            );

            janelaPagamento.initOwner(
                    tabelaItens
                            .getScene()
                            .getWindow()
            );

            janelaPagamento.initModality(
                    Modality.WINDOW_MODAL
            );

            janelaPagamento.setResizable(
                    false
            );

            janelaPagamento.centerOnScreen();

            janelaPagamento.showAndWait();

            campoCodigo.requestFocus();

        } catch (IOException erro) {

            erro.printStackTrace();

            exibirAlerta(
                    Alert.AlertType.ERROR,
                    "Erro no sistema",
                    "Não foi possível abrir a tela de pagamento."
            );
        }
    }

    private void limparVenda() {

        itensVenda.clear();

        tabelaItens.refresh();

        rotuloValorUnitario.setText(
                "R$ 0,00"
        );

        rotuloTotalItem.setText(
                "R$ 0,00"
        );

        rotuloSubtotal.setText(
                "R$ 0,00"
        );

        campoCodigo.clear();
        campoCodigo.requestFocus();
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