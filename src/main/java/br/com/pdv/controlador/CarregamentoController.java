package br.com.pdv.controlador;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.util.Duration;

public class CarregamentoController {

    @FXML
    private ProgressBar barraProgresso;

    @FXML
    private Label rotuloPercentual;

    private Runnable aoConcluir;

    @FXML
    public void initialize() {

        barraProgresso.setProgress(0);
        rotuloPercentual.setText("0%");
    }

    public void iniciarCarregamento(
            Runnable aoConcluir
    ) {

        this.aoConcluir = aoConcluir;

        Timeline timeline = new Timeline();

        for (int i = 0; i <= 100; i += 5) {

            final int percentual = i;

            timeline.getKeyFrames().add(
                    new KeyFrame(
                            Duration.millis(i * 18),
                            evento -> atualizarTela(percentual)
                    )
            );
        }

        timeline.setOnFinished(evento -> {

            if (this.aoConcluir != null) {
                this.aoConcluir.run();
            }
        });

        timeline.play();
    }

    private void atualizarTela(
            int percentual
    ) {

        barraProgresso.setProgress(
                percentual / 100.0
        );

        rotuloPercentual.setText(
                percentual + "%"
        );
    }
}