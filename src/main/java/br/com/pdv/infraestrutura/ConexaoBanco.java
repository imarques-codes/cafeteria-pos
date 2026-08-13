package br.com.pdv.infraestrutura;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexaoBanco {

    private static final String URL =
            "jdbc:sqlite:pdv-cafeteria.db";

    public static Connection conectar() throws SQLException {

        Connection conexao =
                DriverManager.getConnection(URL);

        try (Statement comando = conexao.createStatement()) {
            comando.execute("PRAGMA foreign_keys = ON;");
        }

        return conexao;
    }
}