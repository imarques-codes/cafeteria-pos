package br.com.pdv.repositorio;

import br.com.pdv.dominio.Usuario;
import br.com.pdv.infraestrutura.ConexaoBanco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioRepositorio {

    public Usuario buscarPorLogin(String login) throws SQLException {

        String sql = """
                SELECT
                    id,
                    nome,
                    login,
                    senha_hash,
                    perfil,
                    ativo
                FROM usuario
                WHERE login = ?
                LIMIT 1;
                """;

        try (
                Connection conexao = ConexaoBanco.conectar();
                PreparedStatement comando = conexao.prepareStatement(sql)
        ) {

            comando.setString(1, login);

            try (ResultSet resultado = comando.executeQuery()) {

                if (resultado.next()) {

                    Usuario usuario = new Usuario();

                    usuario.setId(
                            resultado.getInt("id")
                    );

                    usuario.setNome(
                            resultado.getString("nome")
                    );

                    usuario.setLogin(
                            resultado.getString("login")
                    );

                    usuario.setSenhaHash(
                            resultado.getString("senha_hash")
                    );

                    usuario.setPerfil(
                            resultado.getString("perfil")
                    );

                    usuario.setAtivo(
                            resultado.getInt("ativo") == 1
                    );

                    return usuario;
                }
            }
        }

        return null;
    }


    public int contarUsuarios() throws SQLException {

        String sql = """
                SELECT COUNT(*) AS total
                FROM usuario;
                """;

        try (
                Connection conexao = ConexaoBanco.conectar();
                PreparedStatement comando = conexao.prepareStatement(sql);
                ResultSet resultado = comando.executeQuery()
        ) {

            if (resultado.next()) {
                return resultado.getInt("total");
            }
        }

        return 0;
    }


    public void salvar(Usuario usuario) throws SQLException {

        String sql = """
                INSERT INTO usuario (
                    nome,
                    login,
                    senha_hash,
                    perfil,
                    ativo
                )
                VALUES (?, ?, ?, ?, ?);
                """;

        try (
                Connection conexao = ConexaoBanco.conectar();
                PreparedStatement comando = conexao.prepareStatement(sql)
        ) {

            comando.setString(
                    1,
                    usuario.getNome()
            );

            comando.setString(
                    2,
                    usuario.getLogin()
            );

            comando.setString(
                    3,
                    usuario.getSenhaHash()
            );

            comando.setString(
                    4,
                    usuario.getPerfil()
            );

            comando.setInt(
                    5,
                    usuario.isAtivo() ? 1 : 0
            );

            comando.executeUpdate();
        }
    }
}