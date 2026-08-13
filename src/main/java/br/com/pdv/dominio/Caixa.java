package br.com.pdv.dominio;

public class Caixa {

    private int id;
    private String codigo;
    private String descricao;
    private boolean ativo;

    public Caixa() {
    }

    public Caixa(
            int id,
            String codigo,
            String descricao,
            boolean ativo
    ) {
        this.id = id;
        this.codigo = codigo;
        this.descricao = descricao;
        this.ativo = ativo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}