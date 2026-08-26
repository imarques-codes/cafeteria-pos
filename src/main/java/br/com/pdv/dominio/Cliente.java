package br.com.pdv.dominio;

import java.time.LocalDateTime;

public class Cliente {

    private int id;
    private String nome;
    private String cpf;
    private String telefone;
    private String email;
    private LocalDateTime dataCadastro;
    private boolean ativo;

    public Cliente() {
    }

    public Cliente(
            int id,
            String nome,
            String cpf,
            String telefone,
            String email,
            LocalDateTime dataCadastro,
            boolean ativo
    ) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
        this.dataCadastro = dataCadastro;
        this.ativo = ativo;
    }

    public int getId() {
        return id;
    }

    public void setId(
            int id
    ) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(
            String nome
    ) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(
            String cpf
    ) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(
            String telefone
    ) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(
            String email
    ) {
        this.email = email;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(
            LocalDateTime dataCadastro
    ) {
        this.dataCadastro = dataCadastro;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(
            boolean ativo
    ) {
        this.ativo = ativo;
    }
}