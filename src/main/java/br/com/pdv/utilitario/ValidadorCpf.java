package br.com.pdv.utilitario;

public class ValidadorCpf {

    private ValidadorCpf() {
    }

    public static boolean isValido(
            String cpf
    ) {

        if (cpf == null || cpf.isBlank()) {
            return true;
        }

        String numeros =
                cpf.replaceAll(
                        "\\D",
                        ""
                );

        if (numeros.length() != 11) {
            return false;
        }

        if (todosDigitosIguais(numeros)) {
            return false;
        }

        int primeiroDigito =
                calcularDigito(
                        numeros.substring(0, 9),
                        10
                );

        int segundoDigito =
                calcularDigito(
                        numeros.substring(0, 9)
                                + primeiroDigito,
                        11
                );

        return primeiroDigito
                == Character.getNumericValue(
                numeros.charAt(9)
        )
                && segundoDigito
                == Character.getNumericValue(
                numeros.charAt(10)
        );
    }

    private static int calcularDigito(
            String numeros,
            int pesoInicial
    ) {

        int soma = 0;

        for (
                int indice = 0;
                indice < numeros.length();
                indice++
        ) {

            int numero =
                    Character.getNumericValue(
                            numeros.charAt(indice)
                    );

            soma +=
                    numero
                            * (pesoInicial - indice);
        }

        int resto =
                (soma * 10) % 11;

        if (resto == 10) {
            resto = 0;
        }

        return resto;
    }

    private static boolean todosDigitosIguais(
            String cpf
    ) {

        char primeiro =
                cpf.charAt(0);

        for (
                int indice = 1;
                indice < cpf.length();
                indice++
        ) {

            if (
                    cpf.charAt(indice)
                            != primeiro
            ) {

                return false;
            }
        }

        return true;
    }
}