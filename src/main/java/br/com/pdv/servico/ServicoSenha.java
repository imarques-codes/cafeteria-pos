package br.com.pdv.servico;

import com.password4j.Hash;
import com.password4j.Password;

public class ServicoSenha {

    public static String gerarHash(String senha) {

        Hash hash = Password
                .hash(senha)
                .addRandomSalt()
                .withArgon2();

        return hash.getResult();
    }

    public static boolean verificar(
            String senha,
            String hashArmazenado
    ) {

        return Password
                .check(senha, hashArmazenado)
                .withArgon2();
    }
}