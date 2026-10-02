package br.com.pdv;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import br.com.pdv.infraestrutura.InicializadorBanco;
import java.sql.SQLException;
import java.io.IOException;
import br.com.pdv.aplicacao.InicializadorAdministrador;

public class Main extends Application {

    @Override
    public void start(Stage janelaPrincipal)
            throws IOException, SQLException {

        InicializadorBanco.inicializar();
        InicializadorBanco.inicializar();
        InicializadorAdministrador.criarSeNecessario();

        FXMLLoader carregador = new FXMLLoader(
                Main.class.getResource("/fxml/login.fxml")
        );

        Scene cena = new Scene(carregador.load());

        janelaPrincipal.setTitle("PDV - Login");
        janelaPrincipal.setScene(cena);
        janelaPrincipal.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}