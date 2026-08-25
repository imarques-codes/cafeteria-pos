package br.com.pdv.repositorio;

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
}