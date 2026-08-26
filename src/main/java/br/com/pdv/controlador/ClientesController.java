package br.com.pdv.controlador;

import br.com.pdv.dominio.Cliente;
import br.com.pdv.servico.ServicoCliente;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import br.com.pdv.utilitario.FormatadorCpf;
import br.com.pdv.utilitario.FormatadorTelefone;
import java.sql.SQLException;

public class ClientesController {

    @FXML
    private TableView<Cliente> tabelaClientes;

    @FXML
    private TableColumn<Cliente, String> colunaNome;

    @FXML
    private TableColumn<Cliente, String> colunaCpf;

    @FXML
    private TableColumn<Cliente, String> colunaTelefone;

    @FXML
    private TableColumn<Cliente, String> colunaEmail;

    @FXML
    private TableColumn<Cliente, String> colunaStatus;

    @FXML
    private Button botaoFechar;

    @FXML
    private TextField campoNome;

    @FXML
    private TextField campoCpf;

    @FXML
    private TextField campoTelefone;

    @FXML
    private TextField campoEmail;

    @FXML
    private CheckBox checkAtivo;

    @FXML
    private Button botaoNovo;

    private Cliente clienteSelecionado;

    private final ServicoCliente servicoCliente =
            new ServicoCliente();

    private final ObservableList<Cliente> clientes =
            FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        configurarTabela();

        tabelaClientes.setItems(
                clientes
        );

        carregarClientes();

        configurarSelecaoTabela();

        FormatadorCpf.aplicarMascara(
                campoCpf
        );

        FormatadorTelefone.aplicarMascara(
                campoTelefone
        );
    }

    private void configurarSelecaoTabela() {

        tabelaClientes
                .getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (
                                observavel,
                                clienteAnterior,
                                clienteAtual
                        ) -> {

                            if (clienteAtual != null) {

                                carregarClienteNoFormulario(
                                        clienteAtual
                                );
                            }
                        }
                );
    }

    private void carregarClienteNoFormulario(
            Cliente cliente
    ) {

        clienteSelecionado =
                cliente;

        campoNome.setText(
                cliente.getNome()
        );

        if (cliente.getCpf() == null) {

            campoCpf.clear();

        } else {

            FormatadorCpf.definirValor(
                    campoCpf,
                    cliente.getCpf()
            );
        }

        if (cliente.getTelefone() == null) {

            campoTelefone.clear();

        } else {

            FormatadorTelefone.definirValor(
                    campoTelefone,
                    cliente.getTelefone()
            );
        }

        if (cliente.getEmail() == null) {

            campoEmail.clear();

        } else {

            campoEmail.setText(
                    cliente.getEmail()
            );
        }

        checkAtivo.setSelected(
                cliente.isAtivo()
        );
    }

    @FXML
    private void aoNovoCliente() {

        clienteSelecionado = null;

        tabelaClientes
                .getSelectionModel()
                .clearSelection();

        campoNome.clear();

        FormatadorCpf.definirValor(
                campoCpf,
                ""
        );

        FormatadorTelefone.definirValor(
                campoTelefone,
                ""
        );

        campoEmail.clear();

        checkAtivo.setSelected(
                true
        );

        campoNome.requestFocus();
    }

    @FXML
    private void aoSalvarCliente() {

        try {

            Cliente cliente =
                    new Cliente();

            if (clienteSelecionado != null) {

                cliente.setId(
                        clienteSelecionado.getId()
                );
            }

            cliente.setNome(
                    campoNome
                            .getText()
                            .trim()
            );

            cliente.setCpf(
                    FormatadorCpf
                            .obterSomenteNumeros(
                                    campoCpf
                            )
            );

            cliente.setTelefone(
                    FormatadorTelefone
                            .obterSomenteNumeros(
                                    campoTelefone
                            )
            );

            cliente.setEmail(
                    campoEmail
                            .getText()
                            .trim()
            );

            cliente.setAtivo(
                    checkAtivo.isSelected()
            );

            boolean editando =
                    clienteSelecionado != null;

            if (editando) {

                servicoCliente.atualizar(
                        cliente
                );

            } else {

                servicoCliente.cadastrar(
                        cliente
                );
            }

            carregarClientes();

            aoNovoCliente();

            exibirAlerta(
                    Alert.AlertType.INFORMATION,
                    editando
                            ? "Cliente atualizado"
                            : "Cliente cadastrado",
                    editando
                            ? "Cliente atualizado com sucesso."
                            : "Cliente cadastrado com sucesso."
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
                    "Não foi possível salvar o cliente."
            );
        }
    }

    private void configurarTabela() {

        colunaNome.setCellValueFactory(
                dados ->
                        new SimpleStringProperty(
                                dados
                                        .getValue()
                                        .getNome()
                        )
        );

        colunaCpf.setCellValueFactory(
                dados -> {

                    String cpf =
                            dados
                                    .getValue()
                                    .getCpf();

                    if (
                            cpf == null
                                    || cpf.isBlank()
                    ) {

                        cpf = "-";
                    }

                    return new SimpleStringProperty(
                            cpf
                    );
                }
        );

        colunaTelefone.setCellValueFactory(
                dados -> {

                    String telefone =
                            dados
                                    .getValue()
                                    .getTelefone();

                    if (
                            telefone == null
                                    || telefone.isBlank()
                    ) {

                        telefone = "-";
                    }

                    return new SimpleStringProperty(
                            telefone
                    );
                }
        );

        colunaEmail.setCellValueFactory(
                dados -> {

                    String email =
                            dados
                                    .getValue()
                                    .getEmail();

                    if (
                            email == null
                                    || email.isBlank()
                    ) {

                        email = "-";
                    }

                    return new SimpleStringProperty(
                            email
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

    private void carregarClientes() {

        try {

            clientes.setAll(
                    servicoCliente
                            .listarTodos()
            );

        } catch (SQLException erro) {

            erro.printStackTrace();

            exibirAlerta(
                    Alert.AlertType.ERROR,
                    "Erro no sistema",
                    "Não foi possível carregar os clientes."
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

    private void exibirAlerta(
            Alert.AlertType tipo,
            String titulo,
            String mensagem
    ) {

        Alert alerta =
                new Alert(tipo);

        alerta.setTitle(
                "Le Café | Clientes"
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