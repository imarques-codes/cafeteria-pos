package br.com.pdv;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage janelaPrincipal) throws IOException {

        FXMLLoader carregador = new FXMLLoader(
                Main.class.getResource("/fxml/login.fxml")
        );

        Scene cena = new Scene(carregador.load());

        janelaPrincipal.setTitle("Le Café | Cafeteria Premium - Login");
        janelaPrincipal.setScene(cena);
        janelaPrincipal.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}