package br.com.pdv.controlador;

import br.com.pdv.aplicacao.SessaoUsuario;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

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
}