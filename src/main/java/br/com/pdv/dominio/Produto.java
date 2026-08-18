package br.com.pdv.dominio;

public class Produto {

    private int id;
    private String codigoBarras;
    private String nome;
    private String descricao;
    private long precoCentavos;
    private boolean controlaEstoque;
    private int estoqueAtual;
    private boolean ativo;

    public Produto() {
    }

    public Produto(
            int id,
            String codigoBarras,
            String nome,
            String descricao,
            long precoCentavos,
            boolean controlaEstoque,
            int estoqueAtual,
            boolean ativo
    ) {
        this.id = id;
        this.codigoBarras = codigoBarras;
        this.nome = nome;
        this.descricao = descricao;
        this.precoCentavos = precoCentavos;
        this.controlaEstoque = controlaEstoque;
        this.estoqueAtual = estoqueAtual;
        this.ativo = ativo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public long getPrecoCentavos() {
        return precoCentavos;
    }

    public void setPrecoCentavos(long precoCentavos) {
        this.precoCentavos = precoCentavos;
    }

    public boolean isControlaEstoque() {
        return controlaEstoque;
    }

    public void setControlaEstoque(boolean controlaEstoque) {
        this.controlaEstoque = controlaEstoque;
    }

    public int getEstoqueAtual() {
        return estoqueAtual;
    }

    public void setEstoqueAtual(int estoqueAtual) {
        this.estoqueAtual = estoqueAtual;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}