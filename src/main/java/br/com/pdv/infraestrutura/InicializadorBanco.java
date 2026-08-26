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

        String sqlProduto = """
                CREATE TABLE IF NOT EXISTS produto (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,

                    codigo_barras TEXT UNIQUE,

                    nome TEXT NOT NULL,

                    descricao TEXT,

                    preco_centavos INTEGER NOT NULL,

                    controla_estoque INTEGER NOT NULL DEFAULT 1,

                    estoque_atual INTEGER NOT NULL DEFAULT 0,

                    ativo INTEGER NOT NULL DEFAULT 1,

                    data_criacao TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP
                );
                """;

        String sqlCliente = """
        CREATE TABLE IF NOT EXISTS cliente (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            nome TEXT NOT NULL,
            cpf TEXT UNIQUE,
            telefone TEXT,
            email TEXT,
            data_cadastro TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
            ativo INTEGER NOT NULL DEFAULT 1
                CHECK (ativo IN (0, 1))
        );
        """;

        String sqlVenda = """
        CREATE TABLE IF NOT EXISTS venda (
            id INTEGER PRIMARY KEY AUTOINCREMENT,

            sessao_caixa_id INTEGER NOT NULL,
            usuario_id INTEGER NOT NULL,

            data_hora TEXT NOT NULL
                DEFAULT CURRENT_TIMESTAMP,

            total_centavos INTEGER NOT NULL,

            status TEXT NOT NULL DEFAULT 'FINALIZADA'
                CHECK (
                    status IN (
                        'FINALIZADA',
                        'CANCELADA'
                    )
                ),

            FOREIGN KEY (sessao_caixa_id)
                REFERENCES sessao_caixa(id),

            FOREIGN KEY (usuario_id)
                REFERENCES usuario(id)
        );
        """;

        String sqlItemVenda = """
        CREATE TABLE IF NOT EXISTS item_venda (
            id INTEGER PRIMARY KEY AUTOINCREMENT,

            venda_id INTEGER NOT NULL,
            produto_id INTEGER NOT NULL,

            quantidade INTEGER NOT NULL,
            preco_unitario_centavos INTEGER NOT NULL,
            total_centavos INTEGER NOT NULL,

            FOREIGN KEY (venda_id)
                REFERENCES venda(id),

            FOREIGN KEY (produto_id)
                REFERENCES produto(id)
        );
        """;

        String sqlPagamentoVenda = """
        CREATE TABLE IF NOT EXISTS pagamento_venda (
            id INTEGER PRIMARY KEY AUTOINCREMENT,

            venda_id INTEGER NOT NULL,

            forma_pagamento TEXT NOT NULL
                CHECK (
                    forma_pagamento IN (
                        'DINHEIRO',
                        'PIX',
                        'CARTAO_CREDITO',
                        'CARTAO_DEBITO'
                    )
                ),

            valor_centavos INTEGER NOT NULL
                CHECK (valor_centavos > 0),

            valor_recebido_centavos INTEGER,

            troco_centavos INTEGER NOT NULL DEFAULT 0,

            data_hora TEXT NOT NULL
                DEFAULT CURRENT_TIMESTAMP,

            FOREIGN KEY (venda_id)
                REFERENCES venda(id)
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

            comando.execute(sqlProduto);

            comando.execute(sqlVenda);

            comando.execute(sqlItemVenda);

            comando.execute(sqlPagamentoVenda);

            comando.execute(sqlCliente);
        }
    }
}