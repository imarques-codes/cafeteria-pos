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
        String sqlCaixa = """
        CREATE TABLE IF NOT EXISTS caixa (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            codigo TEXT NOT NULL UNIQUE,
            descricao TEXT,
            ativo INTEGER NOT NULL DEFAULT 1
        );
        """;

        String sqlSessaoCaixa = """
        CREATE TABLE IF NOT EXISTS sessao_caixa (
            id INTEGER PRIMARY KEY AUTOINCREMENT,

            caixa_id INTEGER NOT NULL,
            usuario_abertura_id INTEGER NOT NULL,

            data_hora_abertura TEXT NOT NULL
                DEFAULT CURRENT_TIMESTAMP,

            saldo_inicial_centavos INTEGER NOT NULL,

            usuario_fechamento_id INTEGER,
            data_hora_fechamento TEXT,
            saldo_final_centavos INTEGER,

            status TEXT NOT NULL DEFAULT 'ABERTO'
                CHECK (status IN ('ABERTO', 'FECHADO')),

            FOREIGN KEY (caixa_id)
                REFERENCES caixa(id),

            FOREIGN KEY (usuario_abertura_id)
                REFERENCES usuario(id),

            FOREIGN KEY (usuario_fechamento_id)
                REFERENCES usuario(id)
        );
        """;

        String sqlIndiceSessaoAberta = """
        CREATE UNIQUE INDEX IF NOT EXISTS
        idx_sessao_caixa_aberta

        ON sessao_caixa(caixa_id)

        WHERE status = 'ABERTO';
        """;

        String sqlCaixaInicial = """
        INSERT OR IGNORE INTO caixa (
            codigo,
            descricao,
            ativo
        )
        VALUES (
            '01',
            'Caixa principal',
            1
        );
        """;

        try (
                Connection conexao = ConexaoBanco.conectar();
                Statement comando = conexao.createStatement()
        ) {

            comando.execute(sqlUsuario);

            comando.execute(sqlCaixa);

            comando.execute(sqlSessaoCaixa);

            comando.execute(sqlIndiceSessaoAberta);

            comando.execute(sqlCaixaInicial);
        }
    }
}