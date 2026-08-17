package br.com.pdv.repositorio;

import br.com.pdv.dominio.Caixa;
import br.com.pdv.infraestrutura.ConexaoBanco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CaixaRepositorio {

    public Caixa buscarPorCodigo(String codigo) throws SQLException {

        String sql = """
                SELECT
                    id,
                    codigo,
                    descricao,
                    ativo
                FROM caixa
                WHERE codigo = ?
                LIMIT 1;
                """;

        try (
                Connection conexao = ConexaoBanco.conectar();
                PreparedStatement comando = conexao.prepareStatement(sql)
        ) {

            comando.setString(1, codigo);

            try (ResultSet resultado = comando.executeQuery()) {

                if (resultado.next()) {

                    Caixa caixa = new Caixa();

                    caixa.setId(
                            resultado.getInt("id")
                    );

                    caixa.setCodigo(
                            resultado.getString("codigo")
                    );

                    caixa.setDescricao(
                            resultado.getString("descricao")
                    );

                    caixa.setAtivo(
                            resultado.getInt("ativo") == 1
                    );

                    return caixa;
                }
            }
        }

        return null;
    }
}