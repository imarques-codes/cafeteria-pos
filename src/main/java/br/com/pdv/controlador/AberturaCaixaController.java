package br.com.pdv.controlador;
import br.com.pdv.utilitario.FormatadorMoeda;
import br.com.pdv.aplicacao.SessaoUsuario;
import br.com.pdv.dominio.SessaoCaixa;
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

public class AberturaCaixaController {

    @FXML
    private Label rotuloOperador;

    @FXML
    private Label rotuloCaixa;

    @FXML
    private TextField campoSaldoInicial;

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
                campoSaldoInicial
        );

        campoSaldoInicial.requestFocus();
    }

    @FXML
    private void aoAbrirCaixa() {

        String saldoInformado =
                campoSaldoInicial
                        .getText()
                        .trim();

        if (saldoInformado.isEmpty()) {

            exibirAlerta(
                    Alert.AlertType.WARNING,
                    "Saldo obrigatório",
                    "Informe o saldo inicial do caixa."
            );

            campoSaldoInicial.requestFocus();

            return;
        }

        try {

            long saldoCentavos =
                    FormatadorMoeda.converterParaCentavos(
                            saldoInformado
                    );

            SessaoCaixa sessao =
                    servicoCaixa.abrirCaixa(
                            "01",
                            saldoCentavos
                    );

            if (sessao != null) {
                abrirPdv();
            }

        } catch (NumberFormatException erro) {

            exibirAlerta(
                    Alert.AlertType.WARNING,
                    "Valor inválido",
                    "Informe um valor válido. Exemplo: 150,00"
            );

            campoSaldoInicial.requestFocus();

        } catch (IOException erro) {

            erro.printStackTrace();

            exibirAlerta(
                    Alert.AlertType.ERROR,
                    "Erro no sistema",
                    "Não foi possível abrir a tela do PDV."
            );

        } catch (
                SQLException
                | IllegalStateException
                | IllegalArgumentException erro
        ) {

            exibirAlerta(
                    Alert.AlertType.ERROR,
                    "Não foi possível abrir o caixa",
                    erro.getMessage()
            );
        }
    }


    private void abrirPdv() throws IOException {

        FXMLLoader carregador =
                new FXMLLoader(
                        getClass()
                                .getResource(
                                        "/fxml/pdv.fxml"
                                )
                );

        Scene cenaPdv =
                new Scene(
                        carregador.load()
                );

        Stage janela =
                (Stage)
                        campoSaldoInicial
                                .getScene()
                                .getWindow();

        janela.setTitle(
                "Le Café | Cafeteria Premium - PDV"
        );

        janela.setScene(cenaPdv);
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
                "Le Café | PDV"
        );

        alerta.setHeaderText(titulo);
        alerta.setContentText(mensagem);

        alerta.showAndWait();
    }
}