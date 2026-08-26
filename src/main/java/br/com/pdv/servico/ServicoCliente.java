package br.com.pdv.servico;

import br.com.pdv.dominio.Cliente;
import br.com.pdv.repositorio.ClienteRepositorio;
import br.com.pdv.utilitario.ValidadorCpf;
import br.com.pdv.utilitario.ValidadorTelefone;
import br.com.pdv.utilitario.ValidadorEmail;
import java.sql.SQLException;
import java.util.List;

public class ServicoCliente {

    private final ClienteRepositorio clienteRepositorio;

    public ServicoCliente() {

        this.clienteRepositorio =
                new ClienteRepositorio();
    }

    public List<Cliente> listarTodos()
            throws SQLException {

        return clienteRepositorio
                .listarTodos();
    }

    public void cadastrar(
            Cliente cliente
    ) throws SQLException {

        validarCliente(
                cliente
        );

        normalizarDados(
                cliente
        );

        if (
                cliente.getCpf() != null
                        && clienteRepositorio
                        .existeCpf(
                                cliente.getCpf()
                        )
        ) {

            throw new IllegalStateException(
                    "Já existe um cliente cadastrado com esse CPF."
            );
        }

        clienteRepositorio.salvar(
                cliente
        );
    }

    public void atualizar(
            Cliente cliente
    ) throws SQLException {

        if (cliente.getId() <= 0) {

            throw new IllegalStateException(
                    "Cliente inválido para atualização."
            );
        }

        validarCliente(
                cliente
        );

        normalizarDados(
                cliente
        );

        if (
                cliente.getCpf() != null
                        && clienteRepositorio
                        .existeCpfEmOutroCliente(
                                cliente.getCpf(),
                                cliente.getId()
                        )
        ) {

            throw new IllegalStateException(
                    "Já existe outro cliente cadastrado com esse CPF."
            );
        }

        clienteRepositorio.atualizar(
                cliente
        );
    }

    private void validarCliente(
            Cliente cliente
    ) {

        if (cliente == null) {

            throw new IllegalStateException(
                    "Cliente não informado."
            );
        }

        if (
                cliente.getNome() == null
                        || cliente
                        .getNome()
                        .isBlank()
        ) {

            throw new IllegalStateException(
                    "Informe o nome do cliente."
            );
        }

        if (
                cliente.getCpf() != null
                        && !cliente
                        .getCpf()
                        .isBlank()
                        && !ValidadorCpf.isValido(
                        cliente.getCpf()
                )
        ) {

            throw new IllegalStateException(
                    "Informe um CPF válido."
            );
        }

        if (
                cliente.getTelefone() != null
                        && !cliente
                        .getTelefone()
                        .isBlank()
                        && !ValidadorTelefone.isValido(
                        cliente.getTelefone()
                )
        ) {

            throw new IllegalStateException(
                    "Informe um telefone válido com DDD."
            );
        }

        if (
                cliente.getEmail() != null
                        && !cliente
                        .getEmail()
                        .isBlank()
                        && !ValidadorEmail.isValido(
                        cliente.getEmail()
                )
        ) {

            throw new IllegalStateException(
                    "Informe um e-mail válido."
            );
        }
    }

    private void normalizarDados(
            Cliente cliente
    ) {

        cliente.setNome(
                cliente
                        .getNome()
                        .trim()
        );

        cliente.setCpf(
                normalizarTexto(
                        cliente.getCpf()
                )
        );

        cliente.setTelefone(
                normalizarTexto(
                        cliente.getTelefone()
                )
        );

        cliente.setEmail(
                normalizarTexto(
                        cliente.getEmail()
                )
        );
    }

    private String normalizarTexto(
            String valor
    ) {

        if (
                valor == null
                        || valor.isBlank()
        ) {

            return null;
        }

        return valor.trim();
    }
}