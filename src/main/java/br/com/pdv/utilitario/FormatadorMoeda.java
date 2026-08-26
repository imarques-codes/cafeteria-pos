package br.com.pdv.utilitario;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class FormatadorMoeda {

    private static final Locale LOCAL_BRASIL =
            Locale.of("pt", "BR");

    private static final NumberFormat FORMATO_MOEDA =
            NumberFormat.getCurrencyInstance(
                    LOCAL_BRASIL
            );

    private static final String CHAVE_CENTAVOS =
            "valorCentavos";

    private FormatadorMoeda() {
    }

    public static void aplicarMascara(
            TextField campo
    ) {

        campo.getProperties().put(
                CHAVE_CENTAVOS,
                0L
        );

        atualizarCampo(
                campo,
                0L
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

                        int digito =
                                Integer.parseInt(
                                        caractere
                                );

                        long centavos =
                                obterCentavos(campo);

                        if (
                                centavos
                                        <= (Long.MAX_VALUE - digito) / 10
                        ) {

                            centavos =
                                    centavos * 10
                                            + digito;

                            atualizarCampo(
                                    campo,
                                    centavos
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

                        long centavos =
                                obterCentavos(campo);

                        centavos =
                                centavos / 10;

                        atualizarCampo(
                                campo,
                                centavos
                        );
                    }
                }
        );
    }

    public static long converterParaCentavos(
            String valorFormatado
    ) {

        if (
                valorFormatado == null
                        || valorFormatado.isBlank()
        ) {
            return 0;
        }

        String somenteNumeros =
                valorFormatado.replaceAll(
                        "\\D",
                        ""
                );

        if (somenteNumeros.isEmpty()) {
            return 0;
        }

        return Long.parseLong(
                somenteNumeros
        );
    }

    public static void definirValor(
            TextField campo,
            long centavos
    ) {

        if (centavos < 0) {
            centavos = 0;
        }

        atualizarCampo(
                campo,
                centavos
        );
    }

    private static long obterCentavos(
            TextField campo
    ) {

        Object valor =
                campo
                        .getProperties()
                        .get(CHAVE_CENTAVOS);

        if (valor instanceof Long) {
            return (Long) valor;
        }

        return 0;
    }

    private static void atualizarCampo(
            TextField campo,
            long centavos
    ) {

        campo.getProperties().put(
                CHAVE_CENTAVOS,
                centavos
        );

        campo.setText(
                formatarCentavos(
                        centavos
                )
        );

        campo.positionCaret(
                campo.getText().length()
        );
    }

    private static String formatarCentavos(
            long centavos
    ) {

        BigDecimal valor =
                BigDecimal
                        .valueOf(centavos)
                        .movePointLeft(2);

        return FORMATO_MOEDA.format(
                valor
        );
    }
}