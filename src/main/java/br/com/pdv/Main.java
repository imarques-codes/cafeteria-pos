package br.com.pdv;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage janelaPrincipal) {

        Label mensagem = new Label("PDV Cafeteria iniciado corretamente!");

        StackPane painelPrincipal = new StackPane(mensagem);

        Scene cena = new Scene(painelPrincipal, 600, 400);

        janelaPrincipal.setTitle("PDV Cafeteria");
        janelaPrincipal.setScene(cena);
        janelaPrincipal.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}