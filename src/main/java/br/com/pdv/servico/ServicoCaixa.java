package br.com.pdv.servico;

import br.com.pdv.aplicacao.SessaoUsuario;
import br.com.pdv.dominio.Caixa;
import br.com.pdv.dominio.SessaoCaixa;
import br.com.pdv.dominio.Usuario;
import br.com.pdv.repositorio.CaixaRepositorio;
import br.com.pdv.repositorio.SessaoCaixaRepositorio;

import java.sql.SQLException;

public class ServicoCaixa {

    private final CaixaRepositorio caixaRepositorio;
    private final SessaoCaixaRepositorio sessaoCaixaRepositorio;

    public ServicoCaixa() {
        this.caixaRepositorio = new CaixaRepositorio();
        this.sessaoCaixaRepositorio = new SessaoCaixaRepositorio();
    }

    public SessaoCaixa abrirCaixa(
            String codigoCaixa,
            long saldoInicialCentavos
    ) throws SQLException {

        Usuario usuarioLogado =
                SessaoUsuario.getUsuarioLogado();

        if (usuarioLogado == null) {
            throw new IllegalStateException(
                    "Nenhum usuário está autenticado."
            );
        }

        if (saldoInicialCentavos < 0) {
            throw new IllegalArgumentException(
                    "O saldo inicial não pode ser negativo."
            );
        }

        Caixa caixa =
                caixaRepositorio.buscarPorCodigo(codigoCaixa);

        if (caixa == null) {
            throw new IllegalStateException(
                    "Caixa não encontrado."
            );
        }

        if (!caixa.isAtivo()) {
            throw new IllegalStateException(
                    "Este caixa está inativo."
            );
        }

        SessaoCaixa sessaoExistente =
                sessaoCaixaRepositorio
                        .buscarSessaoAbertaPorCaixa(
                                caixa.getId()
                        );

        if (sessaoExistente != null) {
            return sessaoExistente;
        }

        SessaoCaixa novaSessao =
                new SessaoCaixa();

        novaSessao.setCaixaId(
                caixa.getId()
        );

        novaSessao.setUsuarioAberturaId(
                usuarioLogado.getId()
        );

        novaSessao.setSaldoInicialCentavos(
                saldoInicialCentavos
        );

        novaSessao.setStatus("ABERTO");

        sessaoCaixaRepositorio.abrirSessao(
                novaSessao
        );

        return sessaoCaixaRepositorio
                .buscarSessaoAbertaPorCaixa(
                        caixa.getId()
                );
    }

    public SessaoCaixa buscarSessaoAberta(
            String codigoCaixa
    ) throws SQLException {

        Caixa caixa =
                caixaRepositorio.buscarPorCodigo(
                        codigoCaixa
                );

        if (caixa == null) {
            return null;
        }

        return sessaoCaixaRepositorio
                .buscarSessaoAbertaPorCaixa(
                        caixa.getId()
                );
    }
    public void fecharCaixa(
            String codigoCaixa,
            long saldoFinalCentavos
    ) throws SQLException {

        Usuario usuarioLogado =
                SessaoUsuario.getUsuarioLogado();

        if (usuarioLogado == null) {
            throw new IllegalStateException(
                    "Nenhum usuário está autenticado."
            );
        }

        if (saldoFinalCentavos < 0) {
            throw new IllegalArgumentException(
                    "O saldo final não pode ser negativo."
            );
        }

        Caixa caixa =
                caixaRepositorio.buscarPorCodigo(
                        codigoCaixa
                );

        if (caixa == null) {
            throw new IllegalStateException(
                    "Caixa não encontrado."
            );
        }

        SessaoCaixa sessaoAberta =
                sessaoCaixaRepositorio
                        .buscarSessaoAbertaPorCaixa(
                                caixa.getId()
                        );

        if (sessaoAberta == null) {
            throw new IllegalStateException(
                    "Não existe sessão aberta para este caixa."
            );
        }

        boolean fechado =
                sessaoCaixaRepositorio.fecharSessao(
                        sessaoAberta.getId(),
                        usuarioLogado.getId(),
                        saldoFinalCentavos
                );

        if (!fechado) {
            throw new IllegalStateException(
                    "Não foi possível fechar o caixa."
            );
        }
    }
}