package br.com.pdv.aplicacao;

import br.com.pdv.dominio.Usuario;
import br.com.pdv.repositorio.UsuarioRepositorio;
import br.com.pdv.servico.ServicoSenha;

import java.sql.SQLException;

public class InicializadorAdministrador {

    public static void criarSeNecessario() throws SQLException {

        UsuarioRepositorio repositorio = new UsuarioRepositorio();

        if (repositorio.contarUsuarios() > 0) {
            return;
        }

        String nome = System.getenv("PDV_ADMIN_NOME");
        String login = System.getenv("PDV_ADMIN_LOGIN");
        String senha = System.getenv("PDV_ADMIN_SENHA");

        if (
                nome == null || nome.isBlank()
                        || login == null || login.isBlank()
                        || senha == null || senha.isBlank()
        ) {
            throw new IllegalStateException(
                    "Administrador inicial não configurado. " +
                            "Defina PDV_ADMIN_NOME, PDV_ADMIN_LOGIN e PDV_ADMIN_SENHA."
            );
        }

        Usuario administrador = new Usuario();

        administrador.setNome(nome);
        administrador.setLogin(login);
        administrador.setSenhaHash(
                ServicoSenha.gerarHash(senha)
        );
        administrador.setPerfil("ADMINISTRADOR");
        administrador.setAtivo(true);

        repositorio.salvar(administrador);

        System.out.println(
                "Administrador inicial criado com sucesso."
        );
    }
}