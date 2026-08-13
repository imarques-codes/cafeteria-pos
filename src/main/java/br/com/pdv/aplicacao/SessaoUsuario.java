package br.com.pdv.aplicacao;

import br.com.pdv.dominio.Usuario;

public class SessaoUsuario {

    private static Usuario usuarioLogado;

    private SessaoUsuario() {
    }

    public static void iniciar(Usuario usuario) {
        usuarioLogado = usuario;
    }

    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public static boolean estaLogado() {
        return usuarioLogado != null;
    }

    public static void encerrar() {
        usuarioLogado = null;
    }
}