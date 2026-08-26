package br.com.pdv.utilitario;

import java.util.regex.Pattern;

public class ValidadorEmail {

    private static final Pattern PADRAO_EMAIL =
            Pattern.compile(
                    "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
            );

    private ValidadorEmail() {
    }

    public static boolean isValido(
            String email
    ) {

        if (
                email == null
                        || email.isBlank()
        ) {

            return true;
        }

        return PADRAO_EMAIL
                .matcher(
                        email.trim()
                )
                .matches();
    }
}