package br.com.pdv.servico;

import br.com.pdv.dominio.Usuario;
import br.com.pdv.repositorio.UsuarioRepositorio;

import java.sql.SQLException;

public class ServicoAutenticacao {

    private final UsuarioRepositorio usuarioRepositorio;

    public ServicoAutenticacao() {
        this.usuarioRepositorio = new UsuarioRepositorio();
    }

    public Usuario autenticar(
            String login,
            String senha
    ) throws SQLException {

        Usuario usuario =
                usuarioRepositorio.buscarPorLogin(login);

        if (usuario == null) {
            return null;
        }

        if (!usuario.isAtivo()) {
            return null;
        }

        boolean senhaCorreta =
                ServicoSenha.verificar(
                        senha,
                        usuario.getSenhaHash()
                );

        if (!senhaCorreta) {
            return null;
        }

        return usuario;
    }
}