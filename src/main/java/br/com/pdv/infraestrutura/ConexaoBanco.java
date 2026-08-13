package br.com.pdv.infraestrutura;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBanco {

    private static final String URL =
            "jdbc:sqlite:pdv-cafeteria.db";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
