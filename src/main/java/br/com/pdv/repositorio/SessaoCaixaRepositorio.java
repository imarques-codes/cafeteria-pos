package br.com.pdv.repositorio;

import br.com.pdv.dominio.SessaoCaixa;
import br.com.pdv.infraestrutura.ConexaoBanco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SessaoCaixaRepositorio {

    public SessaoCaixa buscarSessaoAbertaPorCaixa(
            int caixaId
    ) throws SQLException {

        String sql = """
                SELECT
                    id,
                    caixa_id,
                    usuario_abertura_id,
                    data_hora_abertura,
                    saldo_inicial_centavos,
                    usuario_fechamento_id,
                    data_hora_fechamento,
                    saldo_final_centavos,
                    status
                FROM sessao_caixa
                WHERE caixa_id = ?
                  AND status = 'ABERTO'
                LIMIT 1;
                """;

        try (
                Connection conexao = ConexaoBanco.conectar();
                PreparedStatement comando = conexao.prepareStatement(sql)
        ) {

            comando.setInt(1, caixaId);

            try (ResultSet resultado = comando.executeQuery()) {

                if (resultado.next()) {

                    SessaoCaixa sessao = new SessaoCaixa();

                    sessao.setId(
                            resultado.getInt("id")
                    );

                    sessao.setCaixaId(
                            resultado.getInt("caixa_id")
                    );

                    sessao.setUsuarioAberturaId(
                            resultado.getInt("usuario_abertura_id")
                    );

                    sessao.setDataHoraAbertura(
                            resultado
                                    .getTimestamp("data_hora_abertura")
                                    .toLocalDateTime()
                    );

                    sessao.setSaldoInicialCentavos(
                            resultado.getLong(
                                    "saldo_inicial_centavos"
                            )
                    );

                    sessao.setStatus(
                            resultado.getString("status")
                    );

                    return sessao;
                }
            }
        }

        return null;
    }

    public void abrirSessao(
            SessaoCaixa sessao
    ) throws SQLException {

        String sql = """
                INSERT INTO sessao_caixa (
                    caixa_id,
                    usuario_abertura_id,
                    saldo_inicial_centavos,
                    status
                )
                VALUES (?, ?, ?, 'ABERTO');
                """;

        try (
                Connection conexao = ConexaoBanco.conectar();
                PreparedStatement comando = conexao.prepareStatement(sql)
        ) {

            comando.setInt(
                    1,
                    sessao.getCaixaId()
            );

            comando.setInt(
                    2,
                    sessao.getUsuarioAberturaId()
            );

            comando.setLong(
                    3,
                    sessao.getSaldoInicialCentavos()
            );

            comando.executeUpdate();
        }
    }
    public boolean fecharSessao(
            int sessaoId,
            int usuarioFechamentoId,
            long saldoFinalCentavos
    ) throws SQLException {

        String sql = """
            UPDATE sessao_caixa
            SET
                usuario_fechamento_id = ?,
                data_hora_fechamento = CURRENT_TIMESTAMP,
                saldo_final_centavos = ?,
                status = 'FECHADO'
            WHERE id = ?
              AND status = 'ABERTO';
            """;

        try (
                Connection conexao = ConexaoBanco.conectar();
                PreparedStatement comando = conexao.prepareStatement(sql)
        ) {

            comando.setInt(
                    1,
                    usuarioFechamentoId
            );

            comando.setLong(
                    2,
                    saldoFinalCentavos
            );

            comando.setInt(
                    3,
                    sessaoId
            );

            int linhasAlteradas =
                    comando.executeUpdate();

            return linhasAlteradas == 1;
        }
    }

}