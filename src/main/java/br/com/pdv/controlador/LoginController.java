package br.com.pdv.controlador;

import br.com.pdv.dominio.Usuario;
import br.com.pdv.servico.ServicoAutenticacao;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField campoUsuario;

    @FXML
    private PasswordField campoSenha;

    private final ServicoAutenticacao servicoAutenticacao =
            new ServicoAutenticacao();

    @FXML
    private void aoEntrar() {

        String login = campoUsuario.getText().trim();
        String senha = campoSenha.getText();

        if (login.isEmpty() || senha.isEmpty()) {

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

            exibirAlerta(
                    Alert.AlertType.INFORMATION,
                    "Login realizado",
                    "Bem-vindo(a), " + usuario.getNome() + "!"
            );

        } catch (SQLException erro) {

            erro.printStackTrace();

            exibirAlerta(
                    Alert.AlertType.ERROR,
                    "Erro no sistema",
                    "Não foi possível acessar o banco de dados."
            );
        }
    }

    private void exibirAlerta(
            Alert.AlertType tipo,
            String titulo,
            String mensagem
    ) {

        Alert alerta = new Alert(tipo);

        alerta.setTitle("Le Café | PDV");
        alerta.setHeaderText(titulo);
        alerta.setContentText(mensagem);

        alerta.showAndWait();
    }
}