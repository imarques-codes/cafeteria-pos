package br.com.pdv.controlador;

import br.com.pdv.aplicacao.SessaoUsuario;
import br.com.pdv.dominio.Produto;
import br.com.pdv.repositorio.ProdutoRepositorio;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
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

    private final ProdutoRepositorio produtoRepositorio =
            new ProdutoRepositorio();

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

        campoCodigo.requestFocus();
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