package br.com.pdv.controlador;

import br.com.pdv.aplicacao.SessaoUsuario;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class PdvController {

    @FXML
    private Label rotuloOperador;

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
    }

    @FXML
    private void aoFecharCaixa() {

        try {

            var recurso = getClass().getResource(
                    "/fxml/fechamento-caixa.fxml"
            );

            if (recurso == null) {

                Alert alerta = new Alert(
                        Alert.AlertType.ERROR
                );

                alerta.setTitle("Le Café | PDV");
                alerta.setHeaderText("Arquivo não encontrado");
                alerta.setContentText(
                        "Não foi possível localizar fechamento-caixa.fxml."
                );

                alerta.showAndWait();

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

            Alert alerta =
                    new Alert(
                            Alert.AlertType.ERROR
                    );

            alerta.setTitle("Le Café | PDV");
            alerta.setHeaderText("Erro no sistema");
            alerta.setContentText(
                    "Não foi possível abrir a tela de fechamento de caixa."
            );

            alerta.showAndWait();
        }
    }
}