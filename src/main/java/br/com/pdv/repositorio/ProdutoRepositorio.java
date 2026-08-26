package br.com.pdv.repositorio;

import br.com.pdv.dominio.Produto;
import br.com.pdv.infraestrutura.ConexaoBanco;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProdutoRepositorio {

    public List<Produto> listarTodos()
            throws SQLException {

        String sql = """
            SELECT
                id,
                codigo_barras,
                nome,
                descricao,
                preco_centavos,
                controla_estoque,
                estoque_atual,
                ativo
            FROM produto
            ORDER BY nome;
            """;

        List<Produto> produtos =
                new ArrayList<>();

        try (
                Connection conexao =
                        ConexaoBanco.conectar();

                PreparedStatement comando =
                        conexao.prepareStatement(sql);

                ResultSet resultado =
                        comando.executeQuery()
        ) {

            while (resultado.next()) {

                produtos.add(
                        criarProduto(resultado)
                );
            }
        }

        return produtos;
    }

    public boolean existeCodigoBarras(
            String codigoBarras
    ) throws SQLException {

        String sql = """
            SELECT 1
            FROM produto
            WHERE codigo_barras = ?
            LIMIT 1;
            """;

        try (
                Connection conexao =
                        ConexaoBanco.conectar();

                PreparedStatement comando =
                        conexao.prepareStatement(sql)
        ) {

            comando.setString(
                    1,
                    codigoBarras
            );

            try (
                    ResultSet resultado =
                            comando.executeQuery()
            ) {

                return resultado.next();
            }
        }
    }

    public boolean existeCodigoBarrasEmOutroProduto(
            String codigoBarras,
            int produtoId
    ) throws SQLException {

        String sql = """
            SELECT 1
            FROM produto
            WHERE codigo_barras = ?
              AND id <> ?
            LIMIT 1;
            """;

        try (
                Connection conexao =
                        ConexaoBanco.conectar();

                PreparedStatement comando =
                        conexao.prepareStatement(sql)
        ) {

            comando.setString(
                    1,
                    codigoBarras
            );

            comando.setInt(
                    2,
                    produtoId
            );

            try (
                    ResultSet resultado =
                            comando.executeQuery()
            ) {

                return resultado.next();
            }
        }
    }

    public Produto buscarPorCodigoBarras(
            String codigoBarras
    ) throws SQLException {

        String sql = """
                SELECT
                    id,
                    codigo_barras,
                    nome,
                    descricao,
                    preco_centavos,
                    controla_estoque,
                    estoque_atual,
                    ativo
                FROM produto
                WHERE codigo_barras = ?
                LIMIT 1;
                """;

        try (
                Connection conexao = ConexaoBanco.conectar();
                PreparedStatement comando =
                        conexao.prepareStatement(sql)
        ) {

            comando.setString(
                    1,
                    codigoBarras
            );

            try (
                    ResultSet resultado =
                            comando.executeQuery()
            ) {

                if (resultado.next()) {
                    return criarProduto(resultado);
                }
            }
        }

        return null;
    }

    public Produto buscarPorId(
            int id
    ) throws SQLException {

        String sql = """
                SELECT
                    id,
                    codigo_barras,
                    nome,
                    descricao,
                    preco_centavos,
                    controla_estoque,
                    estoque_atual,
                    ativo
                FROM produto
                WHERE id = ?
                LIMIT 1;
                """;

        try (
                Connection conexao = ConexaoBanco.conectar();
                PreparedStatement comando =
                        conexao.prepareStatement(sql)
        ) {

            comando.setInt(
                    1,
                    id
            );

            try (
                    ResultSet resultado =
                            comando.executeQuery()
            ) {

                if (resultado.next()) {
                    return criarProduto(resultado);
                }
            }
        }

        return null;
    }

    public void salvar(
            Produto produto
    ) throws SQLException {

        String sql = """
            INSERT INTO produto (
                codigo_barras,
                nome,
                descricao,
                preco_centavos,
                controla_estoque,
                estoque_atual,
                ativo
            )
            VALUES (?, ?, ?, ?, ?, ?, ?);
            """;

        try (
                Connection conexao = ConexaoBanco.conectar();
                PreparedStatement comando =
                        conexao.prepareStatement(sql)
        ) {

            comando.setString(
                    1,
                    produto.getCodigoBarras()
            );

            comando.setString(
                    2,
                    produto.getNome()
            );

            comando.setString(
                    3,
                    produto.getDescricao()
            );

            comando.setLong(
                    4,
                    produto.getPrecoCentavos()
            );

            comando.setInt(
                    5,
                    produto.isControlaEstoque() ? 1 : 0
            );

            comando.setInt(
                    6,
                    produto.getEstoqueAtual()
            );

            comando.setInt(
                    7,
                    produto.isAtivo() ? 1 : 0
            );

            comando.executeUpdate();
        }
    }

    public void atualizar(
            Produto produto
    ) throws SQLException {

        String sql = """
            UPDATE produto
            SET
                codigo_barras = ?,
                nome = ?,
                descricao = ?,
                preco_centavos = ?,
                controla_estoque = ?,
                estoque_atual = ?,
                ativo = ?
            WHERE id = ?;
            """;

        try (
                Connection conexao =
                        ConexaoBanco.conectar();

                PreparedStatement comando =
                        conexao.prepareStatement(sql)
        ) {

            comando.setString(
                    1,
                    produto.getCodigoBarras()
            );

            comando.setString(
                    2,
                    produto.getNome()
            );

            comando.setString(
                    3,
                    produto.getDescricao()
            );

            comando.setLong(
                    4,
                    produto.getPrecoCentavos()
            );

            comando.setInt(
                    5,
                    produto.isControlaEstoque()
                            ? 1
                            : 0
            );

            comando.setInt(
                    6,
                    produto.getEstoqueAtual()
            );

            comando.setInt(
                    7,
                    produto.isAtivo()
                            ? 1
                            : 0
            );

            comando.setInt(
                    8,
                    produto.getId()
            );

            int linhasAlteradas =
                    comando.executeUpdate();

            if (linhasAlteradas != 1) {

                throw new SQLException(
                        "Não foi possível atualizar o produto."
                );
            }
        }
    }

    private Produto criarProduto(
            ResultSet resultado
    ) throws SQLException {

        Produto produto = new Produto();

        produto.setId(
                resultado.getInt("id")
        );

        produto.setCodigoBarras(
                resultado.getString("codigo_barras")
        );

        produto.setNome(
                resultado.getString("nome")
        );

        produto.setDescricao(
                resultado.getString("descricao")
        );

        produto.setPrecoCentavos(
                resultado.getLong("preco_centavos")
        );

        produto.setControlaEstoque(
                resultado.getInt("controla_estoque") == 1
        );

        produto.setEstoqueAtual(
                resultado.getInt("estoque_atual")
        );

        produto.setAtivo(
                resultado.getInt("ativo") == 1
        );

        return produto;
    }
}