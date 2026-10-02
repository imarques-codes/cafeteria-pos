package br.com.pdv.controlador;

import br.com.pdv.aplicacao.SessaoUsuario;
import br.com.pdv.servico.ServicoCaixa;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import br.com.pdv.utilitario.FormatadorMoeda;

public class FechamentoCaixaController {

    @FXML
    private Label rotuloOperador;

    @FXML
    private Label rotuloCaixa;

    @FXML
    private TextField campoSaldoFinal;

    private final ServicoCaixa servicoCaixa =
            new ServicoCaixa();

    @FXML
    public void initialize() {

        if (
                SessaoUsuario.estaLogado()
                        && SessaoUsuario.getUsuarioLogado() != null
        ) {

            rotuloOperador.setText(
                    SessaoUsuario
                            .getUsuarioLogado()
                            .getNome()
            );
        }

        rotuloCaixa.setText(
                "01 - Caixa principal"
        );

        FormatadorMoeda.aplicarMascara(
                campoSaldoFinal
        );

        campoSaldoFinal.requestFocus();
    }

    @FXML
    private void aoFecharCaixa() {

        String saldoInformado =
                campoSaldoFinal
                        .getText()
                        .trim();

        if (saldoInformado.isEmpty()) {

            exibirAlerta(
                    Alert.AlertType.WARNING,
                    "Saldo obrigatório",
                    "Informe o saldo final do caixa."
            );

            campoSaldoFinal.requestFocus();

            return;
        }

        try {

            long saldoFinalCentavos =
                    FormatadorMoeda.converterParaCentavos(
                            saldoInformado
                    );

            servicoCaixa.fecharCaixa(
                    "01",
                    saldoFinalCentavos
            );

            exibirAlerta(
                    Alert.AlertType.INFORMATION,
                    "Caixa fechado",
                    "O Caixa 01 foi fechado com sucesso."
            );

            SessaoUsuario.encerrar();

            abrirLogin();

        } catch (NumberFormatException erro) {

            exibirAlerta(
                    Alert.AlertType.WARNING,
                    "Valor inválido",
                    "Informe um valor válido. Exemplo: 150,00"
            );

            campoSaldoFinal.requestFocus();

        } catch (
                SQLException
                | IllegalStateException
                | IllegalArgumentException erro
        ) {

            exibirAlerta(
                    Alert.AlertType.ERROR,
                    "Não foi possível fechar o caixa",
                    erro.getMessage()
            );

        } catch (IOException erro) {

            erro.printStackTrace();

            exibirAlerta(
                    Alert.AlertType.ERROR,
                    "Erro no sistema",
                    "Não foi possível retornar para a tela de login."
            );
        }
    }


    private void abrirLogin() throws IOException {

        FXMLLoader carregador =
                new FXMLLoader(
                        getClass().getResource(
                                "/fxml/login.fxml"
                        )
                );

        Scene cenaLogin =
                new Scene(
                        carregador.load()
                );

        Stage janela =
                (Stage)
                        campoSaldoFinal
                                .getScene()
                                .getWindow();

        janela.setTitle(
                "PDV - Login"
        );

        janela.setScene(cenaLogin);
        janela.centerOnScreen();
    }

    private void exibirAlerta(
            Alert.AlertType tipo,
            String titulo,
            String mensagem
    ) {

        Alert alerta =
                new Alert(tipo);

        alerta.setTitle(
                "PDV "
        );

        alerta.setHeaderText(titulo);
        alerta.setContentText(mensagem);

        alerta.showAndWait();
    }
}