package br.com.pdv.dominio;

public class ItemVenda {

    private Produto produto;
    private int quantidade;
    private long precoUnitarioCentavos;

    public ItemVenda() {
    }

    public ItemVenda(
            Produto produto,
            int quantidade,
            long precoUnitarioCentavos
    ) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitarioCentavos = precoUnitarioCentavos;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public long getPrecoUnitarioCentavos() {
        return precoUnitarioCentavos;
    }

    public void setPrecoUnitarioCentavos(
            long precoUnitarioCentavos
    ) {
        this.precoUnitarioCentavos = precoUnitarioCentavos;
    }

    public long getTotalCentavos() {
        return precoUnitarioCentavos * quantidade;
    }
}