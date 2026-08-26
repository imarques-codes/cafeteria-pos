package br.com.pdv.utilitario;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class FormatadorCpf {

    private static final String CHAVE_DIGITOS =
            "cpfDigitos";

    private static final int TAMANHO_MAXIMO =
            11;

    private FormatadorCpf() {
    }

    public static void aplicarMascara(
            TextField campo
    ) {

        campo.getProperties().put(
                CHAVE_DIGITOS,
                ""
        );

        campo.addEventFilter(
                KeyEvent.KEY_TYPED,
                evento -> {

                    String caractere =
                            evento.getCharacter();

                    if (
                            caractere != null
                                    && caractere.matches("\\d")
                    ) {

                        evento.consume();

                        String digitos =
                                obterDigitosInternos(
                                        campo
                                );

                        if (
                                digitos.length()
                                        < TAMANHO_MAXIMO
                        ) {

                            digitos += caractere;

                            atualizarCampo(
                                    campo,
                                    digitos
                            );
                        }

                    } else {

                        evento.consume();
                    }
                }
        );

        campo.addEventFilter(
                KeyEvent.KEY_PRESSED,
                evento -> {

                    if (
                            evento.getCode()
                                    == KeyCode.BACK_SPACE
                                    || evento.getCode()
                                    == KeyCode.DELETE
                    ) {

                        evento.consume();

                        String digitos =
                                obterDigitosInternos(
                                        campo
                                );

                        if (!digitos.isEmpty()) {

                            digitos =
                                    digitos.substring(
                                            0,
                                            digitos.length() - 1
                                    );
                        }

                        atualizarCampo(
                                campo,
                                digitos
                        );
                    }
                }
        );
    }

    public static void definirValor(
            TextField campo,
            String cpf
    ) {

        String digitos =
                somenteNumeros(cpf);

        if (
                digitos.length()
                        > TAMANHO_MAXIMO
        ) {

            digitos =
                    digitos.substring(
                            0,
                            TAMANHO_MAXIMO
                    );
        }

        atualizarCampo(
                campo,
                digitos
        );
    }

    public static String obterSomenteNumeros(
            TextField campo
    ) {

        String digitos =
                obterDigitosInternos(
                        campo
                );

        if (digitos.isBlank()) {
            return "";
        }

        return digitos;
    }

    private static String obterDigitosInternos(
            TextField campo
    ) {

        Object valor =
                campo
                        .getProperties()
                        .get(CHAVE_DIGITOS);

        if (valor instanceof String) {
            return (String) valor;
        }

        return "";
    }

    private static void atualizarCampo(
            TextField campo,
            String digitos
    ) {

        campo.getProperties().put(
                CHAVE_DIGITOS,
                digitos
        );

        campo.setText(
                formatar(digitos)
        );

        campo.positionCaret(
                campo.getText().length()
        );
    }

    private static String formatar(
            String digitos
    ) {

        if (digitos.length() <= 3) {
            return digitos;
        }

        if (digitos.length() <= 6) {

            return digitos.substring(0, 3)
                    + "."
                    + digitos.substring(3);
        }

        if (digitos.length() <= 9) {

            return digitos.substring(0, 3)
                    + "."
                    + digitos.substring(3, 6)
                    + "."
                    + digitos.substring(6);
        }

        return digitos.substring(0, 3)
                + "."
                + digitos.substring(3, 6)
                + "."
                + digitos.substring(6, 9)
                + "-"
                + digitos.substring(9);
    }

    private static String somenteNumeros(
            String valor
    ) {

        if (valor == null) {
            return "";
        }

        return valor.replaceAll(
                "\\D",
                ""
        );
    }
}