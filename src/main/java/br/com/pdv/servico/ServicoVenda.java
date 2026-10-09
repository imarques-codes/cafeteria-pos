package br.com.pdv.servico;

import br.com.pdv.aplicacao.SessaoUsuario;
import br.com.pdv.dominio.ItemVenda;
import br.com.pdv.dominio.PagamentoVenda;
import br.com.pdv.dominio.SessaoCaixa;
import br.com.pdv.dominio.Usuario;
import br.com.pdv.dominio.Venda;
import br.com.pdv.repositorio.VendaRepositorio;
import br.com.pdv.dominio.ResumoVenda;
import java.sql.SQLException;
import java.util.List;

public class ServicoVenda {

    private final VendaRepositorio vendaRepositorio;
    private final ServicoCaixa servicoCaixa;

    public ServicoVenda() {

        this.vendaRepositorio =
                new VendaRepositorio();

        this.servicoCaixa =
                new ServicoCaixa();

    }

        public List<ResumoVenda> listarVendas()
        throws SQLException {

            // Aqui eu deixo o serviço responsável por buscar o histórico sem acessar o banco diretamente pela tela.
            return vendaRepositorio.listarVendas();
        }


    public Venda finalizarVenda(
            List<ItemVenda> itens,
            List<PagamentoVenda> pagamentos
    ) throws SQLException {

        Usuario usuarioLogado =
                SessaoUsuario.getUsuarioLogado();

        if (usuarioLogado == null) {

            throw new IllegalStateException(
                    "Nenhum usuário está autenticado."
            );
        }

        if (
                itens == null
                        || itens.isEmpty()
        ) {

            throw new IllegalStateException(
                    "A venda não possui itens."
            );
        }

        if (
                pagamentos == null
                        || pagamentos.isEmpty()
        ) {

            throw new IllegalStateException(
                    "Informe pelo menos uma forma de pagamento."
            );
        }

        SessaoCaixa sessaoCaixa =
                servicoCaixa.buscarSessaoAberta(
                        "01"
                );

        if (sessaoCaixa == null) {

            throw new IllegalStateException(
                    "Não existe caixa aberto."
            );
        }

        Venda venda =
                new Venda();

        venda.setSessaoCaixaId(
                sessaoCaixa.getId()
        );

        venda.setUsuarioId(
                usuarioLogado.getId()
        );

        venda.setStatus(
                "FINALIZADA"
        );

        for (ItemVenda item : itens) {

            if (item.getQuantidade() <= 0) {

                throw new IllegalStateException(
                        "Existe item com quantidade inválida."
                );
            }

            ItemVenda itemVenda =
                    new ItemVenda(
                            item.getProduto(),
                            item.getQuantidade(),
                            item.getPrecoUnitarioCentavos()
                    );

            venda.adicionarItem(
                    itemVenda
            );
        }

        long totalVenda =
                venda.getTotalCentavos();

        if (totalVenda <= 0) {

            throw new IllegalStateException(
                    "O total da venda deve ser maior que zero."
            );
        }

        for (
                PagamentoVenda pagamento :
                pagamentos
        ) {

            if (
                    pagamento.getValorCentavos()
                            <= 0
            ) {

                throw new IllegalStateException(
                        "Existe pagamento com valor inválido."
                );
            }

            PagamentoVenda pagamentoVenda =
                    new PagamentoVenda();

            pagamentoVenda.setFormaPagamento(
                    pagamento.getFormaPagamento()
            );

            pagamentoVenda.setValorCentavos(
                    pagamento.getValorCentavos()
            );

            pagamentoVenda.setValorRecebidoCentavos(
                    pagamento.getValorRecebidoCentavos()
            );

            pagamentoVenda.setTrocoCentavos(
                    pagamento.getTrocoCentavos()
            );

            venda.adicionarPagamento(
                    pagamentoVenda
            );
        }

        long totalPagamentos =
                venda.getTotalPagamentosCentavos();

        if (totalPagamentos < totalVenda) {

            throw new IllegalStateException(
                    "O valor pago é menor que o total da venda."
            );
        }

        vendaRepositorio.salvar(
                venda
        );

        return venda;
    }
}