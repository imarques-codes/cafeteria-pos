package br.com.pdv.controlador;

import br.com.pdv.dominio.Produto;
import br.com.pdv.servico.ServicoProduto;
import br.com.pdv.utilitario.FormatadorMoeda;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

public class ProdutosController {

    @FXML
    private TableView<Produto> tabelaProdutos;

    @FXML
    private TableColumn<Produto, String> colunaCodigo;

    @FXML
    private TableColumn<Produto, String> colunaNome;

    @FXML
    private TableColumn<Produto, String> colunaPreco;

    @FXML
    private TableColumn<Produto, String> colunaEstoque;

    @FXML
    private TableColumn<Produto, String> colunaStatus;

    @FXML
    private TextField campoCodigoBarras;

    @FXML
    private TextField campoNome;

    @FXML
    private TextArea campoDescricao;

    @FXML
    private TextField campoPreco;

    @FXML
    private CheckBox checkControlaEstoque;

    @FXML
    private VBox painelEstoque;

    @FXML
    private TextField campoEstoque;

    @FXML
    private CheckBox checkAtivo;

    @FXML
    private Button botaoNovo;

    @FXML
    private Button botaoFechar;

    private final ServicoProduto servicoProduto =
            new ServicoProduto();

    private final ObservableList<Produto> produtos =
            FXCollections.observableArrayList();

    private Produto produtoSelecionado;

    @FXML
    public void initialize() {

        configurarTabela();

        tabelaProdutos.setItems(
                produtos
        );

        carregarProdutos();

        configurarControleEstoque();

        configurarSelecaoTabela();

        FormatadorMoeda.aplicarMascara(
                campoPreco
        );
    }

    private void configurarTabela() {

        colunaCodigo.setCellValueFactory(
                dados -> {

                    String codigo =
                            dados
                                    .getValue()
                                    .getCodigoBarras();

                    if (
                            codigo == null
                                    || codigo.isBlank()
                    ) {

                        codigo = "-";
                    }

                    return new SimpleStringProperty(
                            codigo
                    );
                }
        );

        colunaNome.setCellValueFactory(
                dados ->
                        new SimpleStringProperty(
                                dados
                                        .getValue()
                                        .getNome()
                        )
        );

        colunaPreco.setCellValueFactory(
                dados ->
                        new SimpleStringProperty(
                                formatarValor(
                                        dados
                                                .getValue()
                                                .getPrecoCentavos()
                                )
                        )
        );

        colunaEstoque.setCellValueFactory(
                dados -> {

                    Produto produto =
                            dados.getValue();

                    String estoque;

                    if (
                            produto.isControlaEstoque()
                    ) {

                        estoque =
                                String.valueOf(
                                        produto.getEstoqueAtual()
                                );

                    } else {

                        estoque = "-";
                    }

                    return new SimpleStringProperty(
                            estoque
                    );
                }
        );

        colunaStatus.setCellValueFactory(
                dados ->
                        new SimpleStringProperty(
                                dados
                                        .getValue()
                                        .isAtivo()
                                        ? "Ativo"
                                        : "Inativo"
                        )
        );
    }

    private void configurarControleEstoque() {

        painelEstoque.setDisable(
                !checkControlaEstoque.isSelected()
        );

        checkControlaEstoque
                .selectedProperty()
                .addListener(
                        (
                                observavel,
                                valorAntigo,
                                controlaEstoque
                        ) -> {

                            painelEstoque.setDisable(
                                    !controlaEstoque
                            );

                            if (!controlaEstoque) {

                                campoEstoque.setText(
                                        "0"
                                );
                            }
                        }
                );
    }

    private void configurarSelecaoTabela() {

        tabelaProdutos
                .getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (
                                observavel,
                                produtoAnterior,
                                produtoAtual
                        ) -> {

                            if (produtoAtual != null) {

                                carregarProdutoNoFormulario(
                                        produtoAtual
                                );
                            }
                        }
                );
    }

    private void carregarProdutoNoFormulario(
            Produto produto
    ) {

        produtoSelecionado =
                produto;

        if (
                produto.getCodigoBarras()
                        == null
        ) {

            campoCodigoBarras.clear();

        } else {

            campoCodigoBarras.setText(
                    produto.getCodigoBarras()
            );
        }

        campoNome.setText(
                produto.getNome()
        );

        if (
                produto.getDescricao()
                        == null
        ) {

            campoDescricao.clear();

        } else {

            campoDescricao.setText(
                    produto.getDescricao()
            );
        }

        FormatadorMoeda.definirValor(
                campoPreco,
                produto.getPrecoCentavos()
        );

        checkControlaEstoque.setSelected(
                produto.isControlaEstoque()
        );

        campoEstoque.setText(
                String.valueOf(
                        produto.getEstoqueAtual()
                )
        );

        checkAtivo.setSelected(
                produto.isAtivo()
        );
    }

    @FXML
    private void aoNovoProduto() {

        produtoSelecionado = null;

        tabelaProdutos
                .getSelectionModel()
                .clearSelection();

        campoCodigoBarras.clear();

        campoNome.clear();

        campoDescricao.clear();

        FormatadorMoeda.definirValor(
                campoPreco,
                0
        );

        checkControlaEstoque.setSelected(
                false
        );

        campoEstoque.setText(
                "0"
        );

        checkAtivo.setSelected(
                true
        );

        campoNome.requestFocus();
    }

    @FXML
    private void aoCancelar() {

        produtoSelecionado = null;

        tabelaProdutos
                .getSelectionModel()
                .clearSelection();

        campoCodigoBarras.clear();

        campoNome.clear();

        campoDescricao.clear();

        FormatadorMoeda.definirValor(
                campoPreco,
                0
        );

        checkControlaEstoque.setSelected(
                false
        );

        campoEstoque.setText(
                "0"
        );

        checkAtivo.setSelected(
                true
        );

        campoNome.requestFocus();
    }

    @FXML
    private void aoSalvarProduto() {

        try {

            Produto produto =
                    new Produto();

            if (produtoSelecionado != null) {

                produto.setId(
                        produtoSelecionado.getId()
                );
            }

            produto.setCodigoBarras(
                    campoCodigoBarras
                            .getText()
                            .trim()
            );

            produto.setNome(
                    campoNome
                            .getText()
                            .trim()
            );

            produto.setDescricao(
                    campoDescricao
                            .getText()
                            .trim()
            );

            long precoCentavos =
                    FormatadorMoeda
                            .converterParaCentavos(
                                    campoPreco.getText()
                            );

            produto.setPrecoCentavos(
                    precoCentavos
            );

            produto.setControlaEstoque(
                    checkControlaEstoque.isSelected()
            );

            if (
                    checkControlaEstoque.isSelected()
            ) {

                String textoEstoque =
                        campoEstoque
                                .getText()
                                .trim();

                int estoque =
                        Integer.parseInt(
                                textoEstoque
                        );

                produto.setEstoqueAtual(
                        estoque
                );

            } else {

                produto.setEstoqueAtual(
                        0
                );
            }

            produto.setAtivo(
                    checkAtivo.isSelected()
            );

            boolean editando =
                    produtoSelecionado != null;

            if (editando) {

                servicoProduto.atualizar(
                        produto
                );

            } else {

                servicoProduto.cadastrar(
                        produto
                );
            }

            carregarProdutos();

            aoNovoProduto();

            exibirAlerta(
                    Alert.AlertType.INFORMATION,
                    editando
                            ? "Produto atualizado"
                            : "Produto cadastrado",
                    editando
                            ? "Produto atualizado com sucesso."
                            : "Produto cadastrado com sucesso."
            );

        } catch (NumberFormatException erro) {

            exibirAlerta(
                    Alert.AlertType.WARNING,
                    "Estoque inválido",
                    "Informe o estoque utilizando somente números inteiros."
            );

        } catch (IllegalStateException erro) {

            exibirAlerta(
                    Alert.AlertType.WARNING,
                    "Dados inválidos",
                    erro.getMessage()
            );

        } catch (SQLException erro) {

            erro.printStackTrace();

            exibirAlerta(
                    Alert.AlertType.ERROR,
                    "Erro no sistema",
                    "Não foi possível salvar o produto."
            );
        }
    }

    @FXML
    private void aoFechar() {

        Stage janela =
                (Stage)
                        botaoFechar
                                .getScene()
                                .getWindow();

        janela.close();
    }

    private void carregarProdutos() {

        try {

            produtos.setAll(
                    servicoProduto
                            .listarTodos()
            );

        } catch (SQLException erro) {

            erro.printStackTrace();

            exibirAlerta(
                    Alert.AlertType.ERROR,
                    "Erro no sistema",
                    "Não foi possível carregar os produtos."
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
                "Produtos"
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