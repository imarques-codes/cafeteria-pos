package br.com.pdv.controlador;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField campoUsuario;

    @FXML
    private PasswordField campoSenha;

    @FXML
    private void aoEntrar() {

        String usuario = campoUsuario.getText().trim();
        String senha = campoSenha.getText();

        if (usuario.isEmpty() || senha.isEmpty()) {

            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Le Café | PDV");
            alerta.setHeaderText("Campos obrigatórios");
            alerta.setContentText("Informe o usuário e a senha.");
            alerta.showAndWait();

            return;
        }

        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Le Café | PDV");
        alerta.setHeaderText("Teste de autenticação");
        alerta.setContentText(
                "Login recebido para o usuário: " + usuario
        );
        alerta.showAndWait();
    }
}