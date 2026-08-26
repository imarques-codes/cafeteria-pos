package br.com.pdv.servico;

import br.com.pdv.dominio.Produto;
import br.com.pdv.repositorio.ProdutoRepositorio;

import java.sql.SQLException;
import java.util.List;

public class ServicoProduto {

    private final ProdutoRepositorio produtoRepositorio;

    public ServicoProduto() {
        this.produtoRepositorio =
                new ProdutoRepositorio();
    }

    public List<Produto> listarTodos()
            throws SQLException {

        return produtoRepositorio
                .listarTodos();
    }

    public Produto buscarPorId(
            int id
    ) throws SQLException {

        return produtoRepositorio
                .buscarPorId(id);
    }

    public void cadastrar(
            Produto produto
    ) throws SQLException {

        validarProduto(produto);

        String codigoBarras =
                normalizarCodigoBarras(
                        produto.getCodigoBarras()
                );

        produto.setCodigoBarras(
                codigoBarras
        );

        if (
                codigoBarras != null
                        && produtoRepositorio
                        .existeCodigoBarras(
                                codigoBarras
                        )
        ) {

            throw new IllegalStateException(
                    "Já existe um produto cadastrado com esse código de barras."
            );
        }

        if (!produto.isControlaEstoque()) {

            produto.setEstoqueAtual(0);
        }

        produtoRepositorio.salvar(
                produto
        );
    }

    public void atualizar(
            Produto produto
    ) throws SQLException {

        if (produto.getId() <= 0) {

            throw new IllegalStateException(
                    "Produto inválido para atualização."
            );
        }

        validarProduto(produto);

        String codigoBarras =
                normalizarCodigoBarras(
                        produto.getCodigoBarras()
                );

        produto.setCodigoBarras(
                codigoBarras
        );

        if (
                codigoBarras != null
                        && produtoRepositorio
                        .existeCodigoBarrasEmOutroProduto(
                                codigoBarras,
                                produto.getId()
                        )
        ) {

            throw new IllegalStateException(
                    "Já existe outro produto cadastrado com esse código de barras."
            );
        }

        if (!produto.isControlaEstoque()) {

            produto.setEstoqueAtual(0);
        }

        produtoRepositorio.atualizar(
                produto
        );
    }

    private void validarProduto(
            Produto produto
    ) {

        if (produto == null) {

            throw new IllegalStateException(
                    "Produto não informado."
            );
        }

        if (
                produto.getNome() == null
                        || produto
                        .getNome()
                        .isBlank()
        ) {

            throw new IllegalStateException(
                    "Informe o nome do produto."
            );
        }

        produto.setNome(
                produto
                        .getNome()
                        .trim()
        );

        if (
                produto.getPrecoCentavos()
                        <= 0
        ) {

            throw new IllegalStateException(
                    "O preço do produto deve ser maior que zero."
            );
        }

        if (
                produto.isControlaEstoque()
                        && produto.getEstoqueAtual() < 0
        ) {

            throw new IllegalStateException(
                    "O estoque do produto não pode ser negativo."
            );
        }

        if (
                produto.getDescricao() != null
                        && produto
                        .getDescricao()
                        .isBlank()
        ) {

            produto.setDescricao(
                    null
            );
        }
    }

    private String normalizarCodigoBarras(
            String codigoBarras
    ) {

        if (
                codigoBarras == null
                        || codigoBarras.isBlank()
        ) {

            return null;
        }

        return codigoBarras.trim();
    }
}