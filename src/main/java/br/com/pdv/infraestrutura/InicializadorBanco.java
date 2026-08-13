package br.com.pdv.infraestrutura;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class InicializadorBanco {

    public static void inicializar() throws SQLException {

        String sqlUsuario = """
                CREATE TABLE IF NOT EXISTS usuario (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome TEXT NOT NULL,
                    login TEXT NOT NULL UNIQUE,
                    senha_hash TEXT NOT NULL,
                    perfil TEXT NOT NULL,
                    ativo INTEGER NOT NULL DEFAULT 1,
                    data_criacao TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
                    ultimo_acesso TEXT
                );
                """;

        try (
                Connection conexao = ConexaoBanco.conectar();
                Statement comando = conexao.createStatement()
        ) {
            comando.execute(sqlUsuario);
        }
    }
}