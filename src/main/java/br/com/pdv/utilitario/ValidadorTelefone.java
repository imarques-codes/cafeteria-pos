package br.com.pdv.utilitario;

public class ValidadorTelefone {

    private ValidadorTelefone() {
    }

    public static boolean isValido(
            String telefone
    ) {

        if (
                telefone == null
                        || telefone.isBlank()
        ) {

            return true;
        }

        String numeros =
                telefone.replaceAll(
                        "\\D",
                        ""
                );

        return numeros.length() == 10
                || numeros.length() == 11;
    }
}