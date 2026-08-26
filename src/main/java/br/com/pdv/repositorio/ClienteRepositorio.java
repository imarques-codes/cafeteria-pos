package br.com.pdv.repositorio;

import br.com.pdv.dominio.Cliente;
import br.com.pdv.infraestrutura.ConexaoBanco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ClienteRepositorio {

    public void salvar(
            Cliente cliente
    ) throws SQLException {

        String sql = """
                INSERT INTO cliente (
                    nome,
                    cpf,
                    telefone,
                    email,
                    ativo
                )
                VALUES (?, ?, ?, ?, ?);
                """;

        try (
                Connection conexao =
                        ConexaoBanco.conectar();

                PreparedStatement comando =
                        conexao.prepareStatement(sql)
        ) {

            comando.setString(
                    1,
                    cliente.getNome()
            );

            if (cliente.getCpf() == null) {

                comando.setNull(
                        2,
                        java.sql.Types.VARCHAR
                );

            } else {

                comando.setString(
                        2,
                        cliente.getCpf()
                );
            }

            if (cliente.getTelefone() == null) {

                comando.setNull(
                        3,
                        java.sql.Types.VARCHAR
                );

            } else {

                comando.setString(
                        3,
                        cliente.getTelefone()
                );
            }

            if (cliente.getEmail() == null) {

                comando.setNull(
                        4,
                        java.sql.Types.VARCHAR
                );

            } else {

                comando.setString(
                        4,
                        cliente.getEmail()
                );
            }

            comando.setInt(
                    5,
                    cliente.isAtivo()
                            ? 1
                            : 0
            );

            comando.executeUpdate();
        }
    }

    public List<Cliente> listarTodos()
            throws SQLException {

        String sql = """
                SELECT
                    id,
                    nome,
                    cpf,
                    telefone,
                    email,
                    data_cadastro,
                    ativo
                FROM cliente
                ORDER BY nome;
                """;

        List<Cliente> clientes =
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

                clientes.add(
                        criarCliente(resultado)
                );
            }
        }

        return clientes;
    }

    public boolean existeCpf(
            String cpf
    ) throws SQLException {

        String sql = """
                SELECT 1
                FROM cliente
                WHERE cpf = ?
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
                    cpf
            );

            try (
                    ResultSet resultado =
                            comando.executeQuery()
            ) {

                return resultado.next();
            }
        }
    }

    public boolean existeCpfEmOutroCliente(
            String cpf,
            int clienteId
    ) throws SQLException {

        String sql = """
                SELECT 1
                FROM cliente
                WHERE cpf = ?
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
                    cpf
            );

            comando.setInt(
                    2,
                    clienteId
            );

            try (
                    ResultSet resultado =
                            comando.executeQuery()
            ) {

                return resultado.next();
            }
        }
    }

    public void atualizar(
            Cliente cliente
    ) throws SQLException {

        String sql = """
                UPDATE cliente
                SET
                    nome = ?,
                    cpf = ?,
                    telefone = ?,
                    email = ?,
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
                    cliente.getNome()
            );

            if (cliente.getCpf() == null) {

                comando.setNull(
                        2,
                        java.sql.Types.VARCHAR
                );

            } else {

                comando.setString(
                        2,
                        cliente.getCpf()
                );
            }

            if (cliente.getTelefone() == null) {

                comando.setNull(
                        3,
                        java.sql.Types.VARCHAR
                );

            } else {

                comando.setString(
                        3,
                        cliente.getTelefone()
                );
            }

            if (cliente.getEmail() == null) {

                comando.setNull(
                        4,
                        java.sql.Types.VARCHAR
                );

            } else {

                comando.setString(
                        4,
                        cliente.getEmail()
                );
            }

            comando.setInt(
                    5,
                    cliente.isAtivo()
                            ? 1
                            : 0
            );

            comando.setInt(
                    6,
                    cliente.getId()
            );

            int linhasAlteradas =
                    comando.executeUpdate();

            if (linhasAlteradas != 1) {

                throw new SQLException(
                        "Não foi possível atualizar o cliente."
                );
            }
        }
    }

    private Cliente criarCliente(
            ResultSet resultado
    ) throws SQLException {

        Cliente cliente =
                new Cliente();

        cliente.setId(
                resultado.getInt("id")
        );

        cliente.setNome(
                resultado.getString("nome")
        );

        cliente.setCpf(
                resultado.getString("cpf")
        );

        cliente.setTelefone(
                resultado.getString("telefone")
        );

        cliente.setEmail(
                resultado.getString("email")
        );

        String dataCadastro =
                resultado.getString(
                        "data_cadastro"
                );

        if (dataCadastro != null) {

            cliente.setDataCadastro(
                    LocalDateTime.parse(
                            dataCadastro.replace(
                                    " ",
                                    "T"
                            )
                    )
            );
        }

        cliente.setAtivo(
                resultado.getInt("ativo")
                        == 1
        );

        return cliente;
    }
}