package br.com.pdv.repositorio;

import br.com.pdv.dominio.ResumoVenda;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import br.com.pdv.dominio.ItemVenda;
import br.com.pdv.dominio.PagamentoVenda;
import br.com.pdv.dominio.Venda;
import br.com.pdv.infraestrutura.ConexaoBanco;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VendaRepositorio {

    public int salvar(
            Venda venda
    ) throws SQLException {

        String sqlVenda = """
                INSERT INTO venda (
                    sessao_caixa_id,
                    usuario_id,
                    total_centavos,
                    status
                )
                VALUES (?, ?, ?, ?);
                """;

        String sqlItemVenda = """
                INSERT INTO item_venda (
                    venda_id,
                    produto_id,
                    quantidade,
                    preco_unitario_centavos,
                    total_centavos
                )
                VALUES (?, ?, ?, ?, ?);
                """;

        String sqlPagamentoVenda = """
                INSERT INTO pagamento_venda (
                    venda_id,
                    forma_pagamento,
                    valor_centavos,
                    valor_recebido_centavos,
                    troco_centavos
                )
                VALUES (?, ?, ?, ?, ?);
                """;

        Connection conexao =
                ConexaoBanco.conectar();

        try {

            conexao.setAutoCommit(false);

            int vendaId;

            try (
                    PreparedStatement comandoVenda =
                            conexao.prepareStatement(
                                    sqlVenda
                            )
            ) {

                comandoVenda.setInt(
                        1,
                        venda.getSessaoCaixaId()
                );

                comandoVenda.setInt(
                        2,
                        venda.getUsuarioId()
                );

                comandoVenda.setLong(
                        3,
                        venda.getTotalCentavos()
                );

                comandoVenda.setString(
                        4,
                        venda.getStatus()
                );

                comandoVenda.executeUpdate();
            }

            try (
                    PreparedStatement comandoId =
                            conexao.prepareStatement(
                                    "SELECT last_insert_rowid();"
                            );

                    ResultSet resultado =
                            comandoId.executeQuery()
            ) {

                if (!resultado.next()) {

                    throw new SQLException(
                            "Não foi possível obter o ID da venda."
                    );
                }

                vendaId =
                        resultado.getInt(1);
            }

            try (
                    PreparedStatement comandoItem =
                            conexao.prepareStatement(
                                    sqlItemVenda
                            )
            ) {

                for (
                        ItemVenda item :
                        venda.getItens()
                ) {

                    comandoItem.setInt(
                            1,
                            vendaId
                    );

                    comandoItem.setInt(
                            2,
                            item
                                    .getProduto()
                                    .getId()
                    );

                    comandoItem.setInt(
                            3,
                            item.getQuantidade()
                    );

                    comandoItem.setLong(
                            4,
                            item.getPrecoUnitarioCentavos()
                    );

                    comandoItem.setLong(
                            5,
                            item.getTotalCentavos()
                    );

                    comandoItem.addBatch();
                }

                comandoItem.executeBatch();
            }
            baixarEstoque(
                    conexao,
                    venda
            );

            try (
                    PreparedStatement comandoPagamento =
                            conexao.prepareStatement(
                                    sqlPagamentoVenda
                            )
            ) {

                for (
                        PagamentoVenda pagamento :
                        venda.getPagamentos()
                ) {

                    comandoPagamento.setInt(
                            1,
                            vendaId
                    );

                    comandoPagamento.setString(
                            2,
                            pagamento.getFormaPagamento()
                    );

                    comandoPagamento.setLong(
                            3,
                            pagamento.getValorCentavos()
                    );

                    if (
                            pagamento.getValorRecebidoCentavos()
                                    == null
                    ) {

                        comandoPagamento.setNull(
                                4,
                                java.sql.Types.INTEGER
                        );

                    } else {

                        comandoPagamento.setLong(
                                4,
                                pagamento.getValorRecebidoCentavos()
                        );
                    }

                    comandoPagamento.setLong(
                            5,
                            pagamento.getTrocoCentavos()
                    );

                    comandoPagamento.addBatch();
                }

                comandoPagamento.executeBatch();
            }

            conexao.commit();

            venda.setId(
                    vendaId
            );

            return vendaId;

        } catch (SQLException erro) {

            try {

                conexao.rollback();

            } catch (SQLException erroRollback) {

                erro.addSuppressed(
                        erroRollback
                );
            }

            throw erro;

        } finally {

            try {

                conexao.setAutoCommit(true);

            } catch (SQLException ignored) {
            }

            conexao.close();
        }
        }
        public List<ResumoVenda> listarVendas()
        throws SQLException {

            // Aqui eu busco somente os dados que preciso para montar a tela de histórico.
            String sql = """
            SELECT
                v.id,
                v.data_hora,
                v.total_centavos,
                v.status,
                u.nome AS nome_operador
            FROM venda v
            INNER JOIN usuario u
                ON u.id = v.usuario_id
            ORDER BY
                v.data_hora DESC,
                v.id DESC;
            """;

            List<ResumoVenda> vendas =
                    new ArrayList<>();

            DateTimeFormatter formatoDataHora =
                    DateTimeFormatter.ofPattern(
                            "yyyy-MM-dd HH:mm:ss"
                    );

            try (
                    Connection conexao =
                            ConexaoBanco.conectar();

                    PreparedStatement comando =
                            conexao.prepareStatement(sql);

                    ResultSet resultado =
                            comando.executeQuery()
            ) {

                while (resultado.next()) {

                    ResumoVenda venda =
                            new ResumoVenda();

                    venda.setId(
                            resultado.getInt("id")
                    );

                    venda.setDataHora(
                            LocalDateTime.parse(
                                    resultado.getString(
                                            "data_hora"
                                    ),
                                    formatoDataHora
                            )
                    );

                    venda.setTotalCentavos(
                            resultado.getLong(
                                    "total_centavos"
                            )
                    );

                    venda.setStatus(
                            resultado.getString(
                                    "status"
                            )
                    );

                    venda.setNomeOperador(
                            resultado.getString(
                                    "nome_operador"
                            )
                    );

                    vendas.add(venda);
                }
            }

            return vendas;
        }

        private void baixarEstoque(
            Connection conexao,
            Venda venda
    ) throws SQLException {

        String sql = """
                UPDATE produto
                SET estoque_atual = estoque_atual - ?
                WHERE id = ?
                  AND controla_estoque = 1
                  AND estoque_atual >= ?;
                """;

        try (
                PreparedStatement comando =
                        conexao.prepareStatement(sql)
        ) {

            for (
                    ItemVenda item :
                    venda.getItens()
            ) {

                if (
                        !item
                                .getProduto()
                                .isControlaEstoque()
                ) {

                    continue;
                }

                int quantidade =
                        item.getQuantidade();

                comando.setInt(
                        1,
                        quantidade
                );

                comando.setInt(
                        2,
                        item
                                .getProduto()
                                .getId()
                );

                comando.setInt(
                        3,
                        quantidade
                );

                int linhasAlteradas =
                        comando.executeUpdate();

                if (
                        linhasAlteradas != 1
                ) {

                    throw new SQLException(
                            "Estoque insuficiente para o produto: "
                                    + item
                                    .getProduto()
                                    .getNome()
                    );
                }
            }
        }
    }
}