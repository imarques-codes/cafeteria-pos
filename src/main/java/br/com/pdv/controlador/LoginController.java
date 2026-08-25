package br.com.pdv.controlador;

import br.com.pdv.aplicacao.SessaoUsuario;
import br.com.pdv.dominio.SessaoCaixa;
import br.com.pdv.dominio.Usuario;
import br.com.pdv.servico.ServicoAutenticacao;
import br.com.pdv.servico.ServicoCaixa;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField campoUsuario;

    @FXML
    private PasswordField campoSenha;

    private final ServicoAutenticacao servicoAutenticacao =
            new ServicoAutenticacao();

    private final ServicoCaixa servicoCaixa =
            new ServicoCaixa();

    private Stage janelaPrincipal;

    @FXML
    private void aoEntrar() {

        String login =
                campoUsuario
                        .getText()
                        .trim();

        String senha =
                campoSenha
                        .getText();

        if (
                login.isEmpty()
                        || senha.isEmpty()
        ) {

            exibirAlerta(
                    Alert.AlertType.WARNING,
                    "Campos obrigatórios",
                    "Informe o usuário e a senha."
            );

            return;
        }

        try {

            Usuario usuario =
                    servicoAutenticacao.autenticar(
                            login,
                            senha
                    );

            if (usuario == null) {

                exibirAlerta(
                        Alert.AlertType.ERROR,
                        "Acesso negado",
                        "Usuário ou senha inválidos."
                );

                campoSenha.clear();
                campoSenha.requestFocus();

                return;
            }

            SessaoUsuario.iniciar(
                    usuario
            );

            abrirCarregamento();

        } catch (SQLException erro) {

            erro.printStackTrace();

            exibirAlerta(
                    Alert.AlertType.ERROR,
                    "Erro no sistema",
                    "Não foi possível acessar o banco de dados."
            );
        }
    }

    private void abrirCarregamento() {

        try {

            FXMLLoader carregador =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/fxml/carregamento.fxml"
                            )
                    );

            Scene cenaCarregamento =
                    new Scene(
                            carregador.load()
                    );

            CarregamentoController controlador =
                    carregador.getController();

            janelaPrincipal =
                    (Stage)
                            campoUsuario
                                    .getScene()
                                    .getWindow();

            janelaPrincipal.setTitle(
                    "Le Café | Cafeteria Premium - Carregando"
            );

            janelaPrincipal.setScene(
                    cenaCarregamento
            );

            janelaPrincipal.centerOnScreen();

            controlador.iniciarCarregamento(
                    this::abrirFluxoAposLogin
            );

        } catch (IOException erro) {

            erro.printStackTrace();

            exibirAlerta(
                    Alert.AlertType.ERROR,
                    "Erro no sistema",
                    "Não foi possível abrir a tela de carregamento."
            );
        }
    }

    private void abrirFluxoAposLogin() {

        try {

            SessaoCaixa sessaoAberta =
                    servicoCaixa.buscarSessaoAberta(
                            "01"
                    );

            if (sessaoAberta != null) {

                abrirPdv();

            } else {

                abrirAberturaCaixa();
            }

        } catch (SQLException erro) {

            erro.printStackTrace();

            exibirAlerta(
                    Alert.AlertType.ERROR,
                    "Erro no sistema",
                    "Não foi possível verificar a situação do caixa."
            );
        }
    }

    private void abrirAberturaCaixa() {

        try {

            FXMLLoader carregador =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/fxml/abertura-caixa.fxml"
                            )
                    );

            Scene cenaAbertura =
                    new Scene(
                            carregador.load()
                    );

            janelaPrincipal.setTitle(
                    "Le Café | Cafeteria Premium - Abertura de Caixa"
            );

            janelaPrincipal.setScene(
                    cenaAbertura
            );

            janelaPrincipal.centerOnScreen();

        } catch (IOException erro) {

            erro.printStackTrace();

            exibirAlerta(
                    Alert.AlertType.ERROR,
                    "Erro no sistema",
                    "Não foi possível abrir a tela de abertura de caixa."
            );
        }
    }

    private void abrirPdv() {

        try {

            FXMLLoader carregador =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/fxml/pdv.fxml"
                            )
                    );

            Scene cenaPdv =
                    new Scene(
                            carregador.load()
                    );

            janelaPrincipal.setTitle(
                    "Le Café | Cafeteria Premium - PDV"
            );

            janelaPrincipal.setScene(
                    cenaPdv
            );

            janelaPrincipal.centerOnScreen();

        } catch (IOException erro) {

            erro.printStackTrace();

            exibirAlerta(
                    Alert.AlertType.ERROR,
                    "Erro no sistema",
                    "Não foi possível abrir o PDV."
            );
        }
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